package ua.ihromant.sod;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class DollExtractor {
    int[] colors = {ARGBColor.from(136,110,36), ARGBColor.from(165,132,49), ARGBColor.from(189,148,57)};
    @Test
    public void extractDoll() throws IOException {
        BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/img/heroDoll.png")));
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int color = img.getRGB(i, j);
                if (Arrays.stream(colors).anyMatch(c -> ARGBColor.dist(c, color) < 20)) {
                    result.setRGB(i, j, colors[0]);
                }
            }
        }
        ImageIO.write(result, "png", new File("/tmp/test.png"));
    }
}
