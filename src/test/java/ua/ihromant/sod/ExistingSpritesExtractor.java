package ua.ihromant.sod;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExistingSpritesExtractor {
    //private static final SpriteMetadata TEST = new SpriteMetadata("AND", 7, 3, 20, 21, 34);

    @Test
    public void extractExisting() throws IOException {
        List<String> lines = IOUtils.readLines(Objects.requireNonNull(getClass().getResourceAsStream("/existing_sprites.txt")), Charset.defaultCharset());
        lines.forEach(line -> {
            int indexOfLeftBr = line.indexOf('(');
            int indexOfRightBr = line.indexOf(')');
            String name = line.substring(0, indexOfLeftBr).trim();
            int[] numbers = Arrays.stream(line.substring(indexOfLeftBr + 1, indexOfRightBr).split(", "))
                    .mapToInt(Integer::parseInt).toArray();
            System.out.println("new SpriteMetadata(\"" + name + "\", Main.Source.ANIMATIONS, " + numbers[0] + ", " + numbers[1]
                + ", " + numbers[2] + ", " + numbers[1] / 2 + ", " + (numbers[2] + numbers[3]) + "),");
        });
    }
}
