package ua.ihromant.sod;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ImageMerger {
    private final List<String> castles = Stream.concat(Arrays.stream(Castle.values()).map(v -> v.name().toLowerCase()), Stream.of("neutrals")).collect(Collectors.toList());
    private static final int WIDTH = 450;
    private static final int HEIGHT = 400;
    private static final int HERO_WIDTH = 150;
    private static final int HERO_HEIGHT = 175;

    @Test
    public void mergeImages() throws IOException {
        File root = new File("/home/ihromant/workspace/github.io/img/creatures");
        for (String castle : castles) {
            for (File dirUnit : Objects.requireNonNull(new File(root, castle).listFiles())) {
                int max = 0;
                for (File img : Objects.requireNonNull(dirUnit.listFiles())) {
                    int yIdx = Integer.parseInt(img.getName().substring(3, 5), 10);
                    if (max < yIdx) {
                        max = yIdx;
                    }
                }
                BufferedImage result = new BufferedImage(WIDTH * 22, HEIGHT * max + HEIGHT, BufferedImage.TYPE_INT_ARGB);
                for (File img : Objects.requireNonNull(dirUnit.listFiles())) {
                    String name = img.getName();
                    int xIdx = Integer.parseInt(name.substring(0, 2), 10);
                    int yIdx = Integer.parseInt(name.substring(3, 5), 10);
                    BufferedImage toDraw = ImageIO.read(img);
                    result.getGraphics().drawImage(toDraw, xIdx * WIDTH, yIdx * HEIGHT, null);
                }
                ImageIO.write(result, "PNG", new File(root, dirUnit.getName() + ".png"));
            }
        }
    }

    @Test
    public void mergeImage() throws IOException {
        mergeImage("/home/ihromant/Games/units/images-shadow/", "avgmumy");
    }

    public static ImageMetadata mergeImage(String rootFolder, String animName) throws IOException {
        int xMax = 0;
        int yMax = 0;
        File root = new File(rootFolder + animName);
        for (File img : Objects.requireNonNull(root.listFiles())) {
            int xIdx = Integer.parseInt(img.getName().substring(0, 2), 10);
            int yIdx = Integer.parseInt(img.getName().substring(3, 5), 10);
            if (yMax < yIdx) {
                yMax = yIdx;
            }
            if (xMax < xIdx) {
                xMax = xIdx;
            }
        }
        BufferedImage result = null;
        ImageMetadata meta = null;
        for (File img : Objects.requireNonNull(root.listFiles())) {
            String name = img.getName();
            int xIdx = Integer.parseInt(name.substring(0, 2), 10);
            int yIdx = Integer.parseInt(name.substring(3, 5), 10);
            BufferedImage toDraw = ImageIO.read(img);
            if (result == null) {
                //System.out.println(root.getName().toUpperCase() + "(" + (yMax + 1) + ", " + toDraw.getWidth() + ", " + toDraw.getHeight() + ", -25),");
                meta = new ImageMetadata(root.getName().toLowerCase(), yMax + 1, toDraw.getWidth(), toDraw.getHeight());
                result = new BufferedImage(toDraw.getWidth() * xMax + toDraw.getWidth(), toDraw.getHeight() * yMax + toDraw.getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
            result.getGraphics().drawImage(toDraw, xIdx * toDraw.getWidth(), yIdx * toDraw.getHeight(), null);
        }
        ImageIO.write(Objects.requireNonNull(result), "PNG", new File("/home/ihromant/workspace/ihromant.github.io/img/mapimpassable", root.getName() + ".png"));
        return meta;
    }

    public enum HeroAnimStage {
        BASE, STANDING, UNHAPPY, HAPPY, CAST;
    }

    @Test
    public void mergeHeroesImages() throws IOException {
        File root = new File("/home/ihromant/Games/units/heroes/heroes");
        for (HeroType type : HeroType.values()) {
            int max = 0;
            File dir = new File(root, type.name().toLowerCase());
            for (File img : Objects.requireNonNull(dir.listFiles())) {
                int yIdx = Integer.parseInt(img.getName().substring(3, 5), 10);
                if (max < yIdx) {
                    max = yIdx;
                }
            }
            File[][] files = new File[5][max + 1];
            //BufferedImage result = new BufferedImage(HERO_WIDTH * 5, HERO_HEIGHT * max + HERO_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            for (File img : Objects.requireNonNull(dir.listFiles())) {
                String name = img.getName();
                int xIdx = Integer.parseInt(name.substring(0, 2), 10);
                int yIdx = Integer.parseInt(name.substring(3, 5), 10);
                files[xIdx][yIdx] = img;
            }
            for (int i = 0; i < files.length; i++) {
                File[] layer = files[i];
                int colMax = IntStream.range(0, max).filter(j -> layer[j] == null).findFirst().orElse(max + 1);
                BufferedImage result = new BufferedImage(HERO_WIDTH, HERO_HEIGHT * colMax, BufferedImage.TYPE_INT_ARGB);
                for (int j = 0; j < colMax; j++) {
                    File img = layer[j];
                    if (img != null) {
                        BufferedImage toDraw = ImageIO.read(img);
                        result.getGraphics().drawImage(toDraw, 0, j * HERO_HEIGHT, null);
                    }
                }
                File newDir = new File("/home/ihromant/workspace/ihromant.github.io/img/animations/heroes/" + dir.getName());
                newDir.mkdir();
                ImageIO.write(result, "PNG", new File(newDir, dir.getName() + "_" + HeroAnimStage.values()[i].toString().toLowerCase() + ".png"));
            }
        }
    }
}
