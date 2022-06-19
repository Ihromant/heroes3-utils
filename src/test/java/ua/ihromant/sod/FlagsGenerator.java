package ua.ihromant.sod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlagsGenerator {
    private static final int[][] FLAG_LEFT = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][] FLAG_RIGHT = {
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    @Test
    public void generateFlags() throws IOException {
        //BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/conflux_town.png")));
        for (Castle castle : Castle.values()) {
            for (String type : new String[] {"town", "fort"}) {
                BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/maptowns/"
                        + castle.name().toLowerCase() + "_" + type + ".png"));
                Point left = determineFlagCoords(img, 64 + 13, 160, FLAG_LEFT);
                Point right = determineFlagCoords(img, 128, 160, FLAG_RIGHT);
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
                    img.setRGB(corner.getX() + i, corner.getY() + j, (0xFF << 24) | kingdom.getRgbFlag());
                }
            }
        }
    }

    private static Point determineFlagCoords(BufferedImage img, int xStart, int yStart, int[][] flag) {
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
        throw new NullPointerException();
    }

    @RequiredArgsConstructor
    @Getter
    @ToString
    private static class Point {
        private final int x;
        private final int y;

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
