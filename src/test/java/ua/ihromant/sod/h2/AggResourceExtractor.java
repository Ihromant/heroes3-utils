package ua.ihromant.sod.h2;

import org.apache.commons.io.FileUtils;
import ua.ihromant.sod.utils.bytes.ByteWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;

public class AggResourceExtractor {
	private static final String NAME = "/home/ihromant/Games/HoMM 2 Gold/DATA/HEROES2.AGG";
	private static final String BASE_FOLDER = "/tmp/images/";
    private static final String PALETTE = "H2Palette.bmp";
    private static BufferedImage pal;
    private static int[] palette;

    private static class FileInfo {
        public int hash;
        public int offset;
        public int size;
        public byte[] file;
    }

    public static void main(String[] args) throws IOException {
        ByteWrapper wrap = new ByteWrapper(FileUtils.readFileToByteArray(new File(NAME)));
        pal = ImageIO.read(Objects.requireNonNull(AggResourceExtractor.class.getResourceAsStream("/palette/" + PALETTE)));
        palette = IntStream.range(0, pal.getHeight()).flatMap(y -> IntStream.range(0, pal.getWidth()).map(x -> pal.getRGB(x, y))).toArray();

        int nofiles = wrap.readUnsignedShort();
        FileInfo[] files = new FileInfo[nofiles];

        for (int i = 0; i < nofiles; i++) {
            files[i] = new FileInfo();
            files[i].hash = wrap.readInt();
            files[i].offset = wrap.readInt();
            files[i].size = wrap.readInt();
        }

        for (int i = 0; i < nofiles; i++) {
            files[i].file = wrap.readBytes(files[i].size);
        }

        for (int i = 0; i < nofiles; i++) {
            byte[] buf = wrap.readBytes(15);
            String name = new String(buf);
            name = name.substring(0, name.indexOf((char) 0)).toLowerCase();
            String extension = name.substring(name.indexOf('.') + 1);
            switch (extension) {
                case "icn":
                    readIcn(files[i].file, name);
                    break;
                case "bmp":
                    readBmp(files[i].file, name);
                default:
                    FileUtils.writeByteArrayToFile(new File(BASE_FOLDER + "/" + name), files[i].file);
                    break;
            }
        }
    }

    private static void readIcn(byte[] bytes, String name) throws IOException {
        ByteWrapper wrap = new ByteWrapper(bytes);
        int count = wrap.readUnsignedShort();
        int size = wrap.readInt();
        if (count == 0) {
            System.out.println("Empty file " + name);
            return;
        }
        String folderName = name.substring(0, name.indexOf("."));
        File dir = new File(BASE_FOLDER, folderName);
        dir.mkdir();
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        Header[] headers = new Header[count];
        for (int i = 0; i < count; i++) {
            headers[i] = new Header();
            headers[i].x = wrap.readUnsignedShort();
            headers[i].y = wrap.readUnsignedShort();
            headers[i].width = wrap.readUnsignedShort();
            headers[i].height = wrap.readUnsignedShort();
            headers[i].bits = wrap.readUnsigned();
            headers[i].offset = wrap.readInt();
            left = Math.min(left, headers[i].x);
            top = Math.min(top, headers[i].y);
            right = Math.max(right, headers[i].x + headers[i].width);
            bottom = Math.max(bottom, headers[i].y + headers[i].height);
        }
        int height = pal.getHeight();
        for (int i = 0; i < count; i++) {
            wrap.seek(headers[i].offset + 6);
            File result = new File(dir, String.format("%02d.png", i));
            //boolean mask = i == 0 || "font.icn".equals(name);
            BufferedImage img = new BufferedImage(headers[i].width, headers[i].height, BufferedImage.TYPE_INT_ARGB);
            unpackCadre(wrap, img, headers[i].bits == 0x20);
            //BufferedImage img = readPalettedImageWithSpecial(headers[i].x, headers[i].y, headers[i].width, headers[i].height, palette, wrap);
            ImageIO.write(img, "png", result);
        }
    }

    private static void paintPixel(BufferedImage img, int pointer, int color) {
        img.setRGB(pointer % img.getWidth(), pointer / img.getWidth(), color);
    }

    public static void unpackCadre(ByteWrapper wrap, BufferedImage img, boolean mono) throws IOException {
        int x = 0;
        int y = 0;
        while (true) {
            int code = wrap.readUnsigned();
            if (mono) {
                switch (code) {
                    case 0:
                        x = ++y * img.getWidth();
                        break;
                    case 0x80:
                        return;
                    default: {
                        if (code < 0x80) {
                            for (int i = 0; i < code; i++) {
                                paintPixel(img, x++, 0xFF << 24);
                            }
                        } else {
                            x += code - 0x80;
                        }
                    }
                }
            } else {
                switch (code) {
                    case 0:
                        x = ++y * img.getWidth();
                        break;
                    case 0x80:
                        return;
                    case 0xC0:
                        int nextByte = wrap.readUnsigned();
                        if (nextByte % 4 == 0) {
                            int shcnt = wrap.readUnsigned();
                            for (int i = 0; i < shcnt; i++) {
                                paintPixel(img, x++, 0x80 << 24);
                            }
                        } else {
                            for (int i = 0; i < nextByte % 4; i++) {
                                paintPixel(img, x++, 0x80 << 24);
                            }
                        }
                        break;
                    case 0xC1:
                        int numb = wrap.readUnsigned();
                        int clr = wrap.readUnsigned();
                        for (int i = 0; i < numb; i++) {
                            paintPixel(img, x++, palette[clr]);
                        }
                        break;
                    default:
                        if (code < 0x80) {
                            int[] colors = wrap.readUnsigned(code);
                            for (int i = 0; i < code; i++) {
                                paintPixel(img, x++, palette[colors[i]]);
                            }
                        } else if (code < 0xC0) {
                            x += code - 0x80;
                        } else { // 0xC2 to 0xFF
                            int color = wrap.readUnsigned();
                            int count = code - 0xC0;
                            for (int i = 0; i < count; i++) {
                                paintPixel(img, x++, palette[color]);
                            }
                        }
                }
            }
        }
    }

    public static BufferedImage readPalettedImageWithSpecial(int x, int y,int width, int height, int[] palette, ByteWrapper stream) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = y; i < height; i++) {
            for (int j = x; j < width; j++) {
                int idx = stream.readUnsigned();
                switch (idx) {
                    // replace special colors
                    // 0 -> (0,0,0,0)    = full transparency
                    // 1 -> (0,0,0,0x40) = shadow border
                    // 2 -> ???
                    // 3 -> ???
                    // 4 -> (0,0,0,0x80) = shadow body
                    // 5 -> (0,0,0,0)    = selection highlight, treat as full transparency
                    // 6 -> (0,0,0,0x80) = shadow body below selection, treat as shadow body
                    // 7 -> (0,0,0,0x40) = shadow border below selection, treat as shadow border
                    case 0:
                        //img.setRGB(j, i, 0);
                        break; // full transparency
                    case 1:
                        img.setRGB(j, i, 0x40 << 24);
                        break; // shadow border
                    case 4:
                        img.setRGB(j, i, 0x80 << 24);
                        break; // shadow body
                    case 5:
                        //if (mode == LodResourceExtractor.GraphMode.SELECTION) {
                        //    img.setRGB(j, i, ARGBColor.YELLOW);
                        //}
                        //img.setRGB(j, i, palette[idx]);
                        break; // selection highlight
                    case 6:
                        img.setRGB(j, i, 0x80 << 24);
                        break; // shadow body below selection
                    case 7:
                        img.setRGB(j, i, 0x40 << 24);
                        break; // shadow border below selection
                    case 0x80:
                        return img;
                    default:
                        img.setRGB(j, i, (0xff << 24) | palette[idx]);
                        break;
                }
            }
        }
        return img;
    }

    private static class Header {
        int x;
        int y;
        int width;
        int height;
        int bits;
        int offset;
    }

    private static void readBmp(byte[] bytes, String name) {

    }
}