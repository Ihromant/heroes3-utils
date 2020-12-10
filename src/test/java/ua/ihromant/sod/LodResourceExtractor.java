package ua.ihromant.sod;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class LodResourceExtractor {
    private static final String baseFolder = "/tmp/images/";
    private static final String lodPosition = "/home/ihromant/Games/Heroes III/Heroes III Complete/Data/H3sprite.lod";
    @Test
    public void extractFromLod() throws IOException, DataFormatException {
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
            n = n.substring(0, n.indexOf((char) 0)).toLowerCase();
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
                String extension = fileName.substring(fileName.indexOf('.') + 1);
                if ("def".equals(extension)) {
                    if ("sgtwmta.def".equals(meta.getName()) || "sgtwmtb.def".equals(meta.getName())) {
                        continue;
                    }
                    readDef(bytes, meta.getName());
                } else {
                    IOUtils.write(bytes, new FileOutputStream(fileName));
                }
            }
        }
        files.forEach(f -> System.out.println(f.getName() + " " + f.getOffset() + " " + f.getSize() + " " + f.getCSize()));
    }

    private void readDef(byte[] bytes, String fileName) throws IOException {
        String folderName = fileName.substring(0, fileName.indexOf("."));
        File dir = new File(baseFolder, folderName);
        dir.mkdir();
        ByteWrapper str = new ByteWrapper(bytes);
        int t = str.readInt();
        str.readInt();
        str.readInt();
        int blocks = str.readInt();
        int[] palette = new int[256];
        for (int i = 0; i < 256; i++) {
            palette[i] = str.readRGB();
        }
        Map<Integer, List<Integer>> offsets = new HashMap<>();
        for (int i = 0; i < blocks; i++) {
            int blockId = str.readInt();
            List<Integer> list = new ArrayList<>();
            offsets.put(blockId, list);
            int entries = str.readInt();
            str.readInt();
            str.readInt();
            for (int j = 0; j < entries; j++) {
                String name = str.readString(13);
                System.out.println(name);
            }
            for (int j = 0; j < entries; j++) {
                list.add(str.readInt());
            }
        }
        int firstfw = -1;
        int firstfh = -1;
        for (Map.Entry<Integer, List<Integer>> e : offsets.entrySet()) {
            for (int j = 0; j < e.getValue().size(); j++) {
                int offs = e.getValue().get(j);
                str.seek(offs);
                str.readInt();
                int fmt = str.readInt();
                int fw = str.readInt();
                int fh = str.readInt();
                int w = str.readInt();
                int h = str.readInt();
                int lm = str.readInt();
                int tm = str.readInt();
                if (firstfw == -1 && firstfh == -1) {
                    firstfw = fw;
                    firstfh = fh;
                } else {
                    if (firstfw > fw) {
                        fw = firstfw;
                    }
                    if (firstfh > fh) {
                        fh = firstfh;
                    }
                }
                File result = new File(dir, String.format("%02d_%02d.png", e.getKey(), j));
                if (w != 0 && h != 0) {
                    ByteArrayOutputStream pixelData = new ByteArrayOutputStream();
                    switch (fmt) {
                        case 0:
                            pixelData.write(str.readBytes(w * h));
                            break;
                        case 1:
                            int[] lineOffs = new int[h];
                            for (int i = 0; i < h; i++) {
                                lineOffs[i] = str.readInt();
                            }
                            for (int lineOff : lineOffs) {
                                str.seek(offs + 32 + lineOff);
                                int totalRowLength = 0;
                                while (totalRowLength < w) {
                                    int code = str.readUnsignedByte();
                                    int length = str.readUnsignedByte() + 1;
                                    if (code == 0xff) {
                                        pixelData.write(str.readBytes(length));
                                    } else {
                                        byte[] res = new byte[length];
                                        Arrays.fill(res, (byte) code);
                                        pixelData.write(res);
                                    }
                                    totalRowLength = totalRowLength + length;
                                }
                            }
                            break;
                        case 2:
                            lineOffs = new int[h];
                            for (int i = 0; i < h; i++) {
                                lineOffs[i] = str.readUnsignedShort();
                            }
                            str.readUnsignedShort();
                            for (int lineOff : lineOffs) {
                                str.seek(offs + 32 + lineOff);
                                int totalRowLength = 0;
                                while (totalRowLength < w) {
                                    int segment = str.readUnsignedByte();
                                    int code = segment >> 5;
                                    int length = (segment & 0x1f) + 1;
                                    if (code == 7) {
                                        pixelData.write(str.readBytes(length));
                                    } else {
                                        byte[] res = new byte[length];
                                        Arrays.fill(res, (byte) code);
                                        pixelData.write(res);
                                    }
                                    totalRowLength = totalRowLength + length;
                                }
                            }
                            break;
                        case 3:
                            int[][] lineoffs = new int[h][w / 32];
                            for (int i = 0; i < h; i++) {
                                for (int k = 0; k < w / 32; k++) {
                                    lineoffs[i][k] = str.readUnsignedShort();
                                }
                            }
                            for (int[] lineoff : lineoffs) {
                                for (int i : lineoff) {
                                    str.seek(offs + 32 + i);
                                    int totalBlockLength = 0;
                                    while (totalBlockLength < 32) {
                                        int segment = str.readUnsignedByte();
                                        int code = segment >> 5;
                                        int length = (segment & 0x1f) + 1;
                                        if (code == 7) {
                                            pixelData.write(str.readBytes(length));
                                        } else {
                                            byte[] res = new byte[length];
                                            Arrays.fill(res, (byte) code);
                                            pixelData.write(res);
                                        }
                                        totalBlockLength = totalBlockLength + length;
                                    }
                                }
                            }
                            break;
                        default:
                            throw new IllegalStateException();
                    }
                    BufferedImage img = readPalettedImageWithSpecial(w, h, palette, new ByteWrapper(pixelData.toByteArray()));
                    //ImageIO.write(img, "png", result);
                    BufferedImage im = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                    im.getGraphics().drawImage(img, lm, tm, null);
                    im.getGraphics().dispose();
                    ImageIO.write(im, "png", result);
                } else {
                    BufferedImage img = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                    ImageIO.write(img, "png", result);
                }
            }
        }
    }

    private int readInt(RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[4];
        file.read(bytes);
        return new ByteWrapper(bytes).readInt();
    }

    private void readPcx(byte[] bytes, String fileName) throws IOException {
        ByteWrapper stream = new ByteWrapper(bytes);
        int size = stream.readInt();
        int width = stream.readInt();
        int height = stream.readInt();
        BufferedImage img;
        if (size == width * height) {
            int[] palette = new int[256];
            for (int i = 12 + size; i < 12 + size + 3 * 256; i = i + 3) {
                palette[(i - 12 - size) / 3] = (Byte.toUnsignedInt(bytes[i]) << 16) | (Byte.toUnsignedInt(bytes[i + 1]) << 8) | (Byte.toUnsignedInt(bytes[i + 2]));
            }
            img = readPalettedImage(width, height, palette, stream);
        } else {
            img = readRGBImage(width, height, stream);
        }
        ImageIO.write(img, "bmp", new File(fileName.replace("pcx", "bmp")));
    }

    private BufferedImage readPalettedImageWithSpecial(int width, int height, int[] palette, ByteWrapper stream) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int idx = stream.readUnsignedByte();
                switch (idx) {
                    case 0:
                        //img.setRGB(j, i, 0);
                        break; // full transparency
                    case 1:
                        //img.setRGB(j, i, palette[idx]);
                        break; // shadow border
                    case 4:
                        //img.setRGB(j, i, palette[idx]);
                        break; // shadow body
                    case 5:
                        //img.setRGB(j, i, palette[idx]);
                        break; // selection highlight
                    case 6:
                        //img.setRGB(j, i, palette[idx]);
                        break; // shadow body below selection
                    case 7:
                        //img.setRGB(j, i, palette[idx]);
                        break; // shadow border below selection
                    default:
                        img.setRGB(j, i, (0xff << 24) | palette[idx]);
                        break;
                }
            }
        }
        return img;
    }

    private BufferedImage readPalettedImage(int width, int height, int[] palette, ByteWrapper stream) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int idx = stream.readUnsignedByte();
                img.setRGB(j, i, palette[idx]);
            }
        }
        return img;
    }

    private BufferedImage readRGBImage(int width, int height, ByteWrapper stream) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                img.setRGB(j, i, stream.readRGB());
            }
        }
        return img;
    }

    private boolean isPcx(byte[] array) throws IOException {
        ByteWrapper stream = new ByteWrapper(array);
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

    public static class ByteWrapper {
        private final LittleEndianDataInputStream str;

        public ByteWrapper(byte[] bytes) {
            this.str = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes));
        }

        public void seek(int position) throws IOException {
            str.reset();
            str.skipBytes(position);
        }

        public int readInt() throws IOException {
            return str.readInt();
        }

        public String readString(int characters) throws IOException {
            char[] nm = new char[characters];
            for (int k = 0; k < characters; k++) {
                nm[k] = (char) str.readByte();
            }
            return new String(nm);
        }

        public int readUnsignedByte() throws IOException {
            return str.readUnsignedByte();
        }

        public int readRGB() throws IOException {
            return (str.readUnsignedByte() << 16) | (str.readUnsignedByte() << 8) | str.readUnsignedByte();
        }

        public byte[] readBytes(int size) throws IOException {
            byte[] bytes = new byte[size];
            str.read(bytes);
            return bytes;
        }

        public byte readByte() throws IOException {
            return str.readByte();
        }

        public int readUnsignedShort() throws IOException {
            return str.readUnsignedShort();
        }
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
