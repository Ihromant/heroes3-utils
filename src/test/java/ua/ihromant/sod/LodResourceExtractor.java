package ua.ihromant.sod;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class LodResourceExtractor {
    private static final String baseFolder = "/tmp/images/";
    private static final String lodPosition = "/home/ihromant/Games/Heroes III/Heroes III Complete/Data/H3bitmap.lod";
    @Test
    public void extractFromLod() throws IOException, DataFormatException, ImageReadException {
        RandomAccessFile file = new RandomAccessFile(new File(lodPosition), "r");
        byte[] header = new byte[4];
        file.read(header);
        for (byte b : header) {
            System.out.print((char) b);
        }
        System.out.println();
        file.seek(8);
        int total = readInt(file);
        System.out.println("Total: " + total);
        file.seek(92);
        List<FileMetadata> files = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            char[] name = new char[16];
            for (int j = 0; j < 16; j++) {
                name[j] = (char) file.readByte();
            }
            FileMetadata fileMeta = new FileMetadata();
            String n = new String(name);
            n = n.substring(0, n.indexOf((char) 0));
            fileMeta.setName(n);
            fileMeta.setOffset(readInt(file));
            fileMeta.setSize(readInt(file));
            readInt(file);
            fileMeta.setCSize(readInt(file));
            files.add(fileMeta);
        }
        for (FileMetadata meta : files) {
            String fileName = baseFolder + meta.getName();
            System.out.println(fileName);
            file.seek(meta.getOffset());
            int size = meta.getCSize() != 0 ? meta.getCSize() : meta.getSize();
            byte[] bytes = meta.getCSize() != 0 ? readCompressed(file, size, meta.getSize()) : readUncompressed(file, size);
            if (isPcx(bytes)) {
                readPcx(bytes, fileName);
            } else {
                IOUtils.write(bytes, new FileOutputStream(fileName));
            }
        }
        files.forEach(f -> System.out.println(f.getName() + " " + f.getOffset() + " " + f.getSize() + " " + f.getCSize()));
    }

    private int readInt(RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[4];
        file.read(bytes);
        return new LittleEndianDataInputStream(new ByteArrayInputStream(bytes)).readInt();
    }

    private void readPcx(byte[] bytes, String fileName) throws IOException, ImageReadException {
        LittleEndianDataInputStream stream = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes));
        int size = stream.readInt();
        int width = stream.readInt();
        int height = stream.readInt();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        if (size == width * height) {
            int[] palette = new int[256];
            for (int i = 12 + size; i < 12 + size + 3 * 256; i = i + 3) {
                palette[(i - 12 - size) / 3] = (Byte.toUnsignedInt(bytes[i]) << 16) | (Byte.toUnsignedInt(bytes[i + 1]) << 8) | (Byte.toUnsignedInt(bytes[i + 2]));
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int idx = stream.readUnsignedByte();
                    img.setRGB(j, i, palette[idx]);
                }
            }
        } else {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int r = stream.readUnsignedByte() << 16;
                    int g = stream.readUnsignedByte() << 8;
                    int b = stream.readUnsignedByte();
                    img.setRGB(j, i, r | g | b);
                }
            }
        }
        ImageIO.write(img, "bmp", new File(fileName.toLowerCase().replace("pcx", "bmp")));
    }

    private boolean isPcx(byte[] array) throws IOException {
        LittleEndianDataInputStream stream = new LittleEndianDataInputStream(new ByteArrayInputStream(array));
        int size = stream.readInt();
        int width = stream.readInt();
        int height = stream.readInt();
        return size == width * height || size == 3 * width * height;
    }

    private byte[] readCompressed(RandomAccessFile file, int cSize, int size) throws IOException, DataFormatException {
        byte[] compressed = new byte[cSize];
        file.read(compressed);
        Inflater defl = new Inflater();
        defl.setInput(compressed);
        ByteBuffer output = ByteBuffer.allocate(size);
        defl.inflate(output);
        return output.array();
    }

    private byte[] readUncompressed(RandomAccessFile file, int size) throws IOException {
        byte[] result = new byte[size];
        file.read(result);
        return result;
    }

    @Setter
    @Getter
    private static class FileMetadata {
        private String name;
        private int offset;
        private int size;
        private int cSize;
    }
}
