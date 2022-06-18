package ua.ihromant.sod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FlagsGenerator {
    @Test
    public void generateFlags() throws IOException {
        BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/conflux_town.png")));
        for (Kingdom k : Kingdom.values()) {
            BufferedImage flags = new BufferedImage(96, 32, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < 96; x++) {
                for (int y = 0; y < 16; y++) {
                    int rgba = img.getRGB(x + 64, y + 160);
                    if (rgba == 0) {
                        flags.setRGB(x, y, (0xFF << 24) | k.getRgbFlag());
                    }
                }
            }
            ImageIO.write(flags, "png", new File("/home/ihromant/temp/" + k.name().toLowerCase() + ".png"));
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
