package ua.ihromant.sod;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GradientGenerator {
    @Test
    public void rotateHue() throws IOException {
        BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/background", "blue.jpg"));
        for (int i = 0; i < 360; i++) {
            float shift = 1.0f * i / 360;
            BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int j = 0; j < img.getWidth(); j++) {
                for (int k = 0; k < img.getHeight(); k++) {
                    int c = img.getRGB(j, k);
                    float[] hsb = new float[3];
                    Color color = new Color(c);
                    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
                    result.setRGB(j, k, Color.HSBtoRGB((hsb[0] + shift) > 1 ? hsb[0] + shift - 1 : hsb[0] + shift, hsb[1], hsb[2]));
                }
            }
            ImageIO.write(result, "png", new File("/home/ihromant/Games/units/images/gradient", i + ".png"));
        }
    }

    @Test
    public void incrBr() throws IOException {
        for (int i = 0; i < 360; i++) {
            BufferedImage img = ImageIO.read(new File("/home/ihromant/Games/units/images/gradient", i + ".png"));
            BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int j = 0; j < img.getWidth(); j++) {
                for (int k = 0; k < img.getHeight(); k++) {
                    int c = img.getRGB(j, k);
                    float[] hsb = new float[3];
                    Color color = new Color(c);
                    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
                    result.setRGB(j, k, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] / 2 + 0.5f));
                }
            }
            ImageIO.write(result, "png", new File("/home/ihromant/Games/units/images/gradient", i + "br.png"));
        }
    }
}
