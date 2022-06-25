package ua.ihromant.sod;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImagePositionFinder {
    private static final int BACK_WIDTH = 800;
    private static final int BACK_HEIGHT = 374;
    private static final int TEST_COUNT = 256;
    @Test
    public void determine() throws AWTException, IOException {
        printImageData("tbcsback", "tbcscstl");
    }

    private void printImageData(String castleBack, String bFolder) throws AWTException, IOException {
        Robot robot = new Robot();
        BufferedImage realImg = robot.createScreenCapture(new Rectangle(1920, 0, 1024, 768));
        BufferedImage background = ImageIO.read(new File("/home/ihromant/Games/units/images/zbackground/" + castleBack + ".png"));
        Point p = determineOffset(realImg, background);
        System.out.println(p);
        File folder = new File("/home/ihromant/Games/units/images-shadow", bFolder);
        BufferedImage building = ImageIO.read(new File(folder, "00_00.png"));
        Map<Point, Integer> points = readNonTransparent(building);
        List<Point> dest = determineImage(realImg, points, building.getWidth(), building.getHeight(), p);
        if (dest.size() == 1) {
            System.out.println(building.getWidth() + "," + building.getHeight() + "," + folder.listFiles().length + "," + dest.get(0).getX() + "," + dest.get(0).getY());
        } else {
            System.out.println(building.getWidth() + " " + building.getHeight() + " " + folder.listFiles().length + " " + dest);
        }
    }

    private List<Point> determineImage(BufferedImage src, Map<Point, Integer> building, int width, int height, Point offset) {
        return IntStream.rangeClosed(0, BACK_WIDTH - width).boxed().flatMap(i ->
                IntStream.rangeClosed(0, BACK_HEIGHT - height).filter(j -> building.entrySet().stream().limit(TEST_COUNT)
                        .filter(e -> e.getValue() == src.getRGB(i + e.getKey().getX() + offset.getX(), j + e.getKey().getY() + offset.getY()))
                        .count() * 100 / TEST_COUNT > 25).mapToObj(j -> new Point(i, j))).collect(Collectors.toList());
    }

    private static Map<Point, Integer> readNonTransparent(BufferedImage img) {
        Map<Point, Integer> result = new HashMap<>();
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int argb = img.getRGB(i, j);
                if (argb >> 24 != 0) {
                    result.put(new Point(i, j), argb);
                }
            }
        }
        return result;
    }

    private Point determineOffset(BufferedImage src, BufferedImage back) {
        for (int i = 50; i < 150; i++) {
            for (int j = 50; j < 150; j++) {
                boolean equals = true;
                for (int k = 0; k < 16; k++) {
                    for (int l = 0; l < 16; l++) {
                        if (src.getRGB(i + k, j + l) != back.getRGB(k, l)) {
                            equals = false;
                        }
                    }
                }
                if (equals) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalStateException();
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static class Point {
        private final int x;
        private final int y;
    }
}
