package ua.ihromant.sod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FlagsGenerator {
    private static final int[][] FLAG_LEFT = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][] FLAG_GEN_SAWMILL_SNOW_MISALIGNED = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0}
    };

    private static final int[][] FLAG_GEN_SAWMILL_MISALIGNED = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1}
    };

    private static final int[][] FLAG_RIGHT = {
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    @Test
    public void generateGeneratorFlags() throws IOException {
        File file = new File("/home/ihromant/workspace/ihromant.github.io/img/map/resgen");
        for (File f : Objects.requireNonNull(file.listFiles())) {
            BufferedImage img = ImageIO.read(f);
            int height = img.getHeight() > 1000 ? img.getHeight() / 16 : img.getHeight() > 500 ? img.getHeight() / 8 : img.getHeight();
            GenFlagData gfg = determineGenCrd(f.getName(), img, height);
            Point p = gfg.determineCrd(img);
            String name = f.getName().substring(0, f.getName().indexOf('.'));
            for (Kingdom k : Kingdom.values()) {
                BufferedImage res = new BufferedImage(img.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
                paintFlag(res, p, gfg.flag, k);
                ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/map/resgen_flags/"
                        + name + "_" + k.name().toLowerCase() + ".png"));
            }
        }
    }

    private GenFlagData determineGenCrd(String filename, BufferedImage img, int height) {
        if (filename.startsWith("gold")) {
            return new GenFlagData(height, new Point(64, 57), FLAG_LEFT);
        }
        if (filename.equals("sawmill_snow.png")) {
            return new GenFlagData(height, new Point(50, 60), FLAG_GEN_SAWMILL_SNOW_MISALIGNED);
        }
        if (filename.startsWith("sawmill")) {
            return new GenFlagData(height, new Point(70, 34), FLAG_GEN_SAWMILL_MISALIGNED);
        }
        if (filename.startsWith("sulfur")) {
            return new GenFlagData(height, new Point(23, 37), FLAG_LEFT);
        }
        if (filename.startsWith("ore")) {
            return new GenFlagData(height, new Point(35, 37), FLAG_LEFT);
        }
        if (filename.startsWith("alch")) {
            return new GenFlagData(height, new Point(60, 73), FLAG_GEN_SAWMILL_MISALIGNED);
        }
        if (filename.startsWith("gem")) {
            return new GenFlagData(height, new Point(25, 37), FLAG_LEFT);
        }
        if (List.of("crystal_cavern_dirt.png", "crystal_cavern_grass.png", "crystal_cavern_lava.png",
                "crystal_cavern_subterranean.png").contains(filename)) {
            return new GenFlagData(height, new Point(25, 37), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_snow.png")) {
            return new GenFlagData(height, new Point(65, 84), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_sand.png")) {
            return new GenFlagData(height, new Point(65, 92), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_swamp.png")) {
            return new GenFlagData(height, new Point(69, 94), FLAG_LEFT);
        }
        if (filename.startsWith("crys")) {
            return new GenFlagData(height, new Point(56, 70), FLAG_LEFT);
        }
        return null;
    }

    private record GenFlagData(int height, Point shift, int[][] flag) {
        private Point determineCrd(BufferedImage img) {
            Point pos = determineFlagCrd(img, img.getWidth() - shift.x, height - shift.y, flag);
            return new Point(img.getWidth() - shift.x + pos.x, height - shift.y + pos.y);
        }
    }

    @Test
    public void generateCastleFlags() throws IOException {
        //BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/conflux_town.png")));
        for (Castle castle : Castle.values()) {
            for (String type : new String[] {"town", "fort"}) {
                BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/maptowns/"
                        + castle.name().toLowerCase() + "_" + type + ".png"));
                Point left = determineFlagCrd(img, 64 + 13, 160, FLAG_LEFT);
                Point right = determineFlagCrd(img, 128, 160, FLAG_RIGHT);
                for (Kingdom k : Kingdom.values()) {
                    BufferedImage res = new BufferedImage(96, 32, BufferedImage.TYPE_INT_ARGB);
                    paintFlag(res, left.add(13, 0), FLAG_LEFT, k);
                    paintFlag(res, right.add(64, 0), FLAG_RIGHT, k);
                    ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/flags/town/"
                        + castle.name().toLowerCase() + "_" + type + "_" + k.name().toLowerCase() + ".png"));
                }
            }
        }
    }

    private static void paintFlag(BufferedImage img, Point corner, int[][] pattern, Kingdom kingdom) {
        for (int j = 0; j < pattern.length; j++) {
            for (int i = 0; i < pattern[j].length; i++) {
                if (pattern[j][i] == 1) {
                    img.setRGB(corner.x() + i, corner.y() + j, (0xFF << 24) | kingdom.getRgbFlag());
                }
            }
        }
    }

    private static Point determineFlagCrd(BufferedImage img, int xStart, int yStart, int[][] flag) {
        for (int j = 0; j < 28; j++) {
            for (int i = 0; i < 9; i++) {
                boolean found = true;
                for (int k = 0; k < flag.length; k++) {
                    for (int l = 0; l < flag[k].length; l++) {
                        if (flag[k][l] == 1 && img.getRGB(xStart + i + l, yStart + j + k) != 0) {
                            found = false;
                        }
                    }
                }
                if (found) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private record Point(int x, int y) {
        public Point add(int dx, int dy) {
            return new Point(x + dx, y + dy);
        }
    }

    @RequiredArgsConstructor
    @Getter
    private enum Kingdom {
        RED(0xf80000), BLUE(0x3050f8), TAN(0x987450), GREEN(0x409428),
        ORANGE(0xf88000), PURPLE(0x882ca0), TEAL(0x0898a0), PINK(0xc07888),
        NONE(0x808080);
        private final int rgbFlag;
    }
}
