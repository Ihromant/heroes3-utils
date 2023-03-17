package ua.ihromant.sod;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageMerger {
    private final List<String> castles = Stream.concat(Arrays.stream(Castle.values()).map(v -> v.name().toLowerCase()), Stream.of("neutrals")).toList();
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
        mergeImage("/home/ihromant/Games/units/images/aabuttons", "adoplfa", "/home/ihromant/workspace/ihromant.github.io/img/buttons/11x24/", "left_arr");
        mergeImage("/home/ihromant/Games/units/images/aabuttons", "adoprta", "/home/ihromant/workspace/ihromant.github.io/img/buttons/11x24/", "right_arr");
        //mergeImage("/home/ihromant/Games/units/images/aabuttons", "icancel", "/home/ihromant/workspace/ihromant.github.io/img/buttons/64x30", "icancel");
        //mergeImage("/home/ihromant/Games/units/images-shadow/", "hsbtns9", "/home/ihromant/workspace/ihromant.github.io/img/buttons/52x36", "split_troops");
        //mergeImage("/home/ihromant/Games/units/images-shadow/", "icm005", "/home/ihromant/workspace/ihromant.github.io/img/buttons/x36", "battle_cast");
        //mergeImage("/home/ihromant/Games/units/images-shadow/", "icm006", "/home/ihromant/workspace/ihromant.github.io/img/buttons/x36", "battle_wait");
        //mergeImage("/home/ihromant/Games/units/images-shadow/", "icm007", "/home/ihromant/workspace/ihromant.github.io/img/buttons/x36", "battle_defend");
    }

    public static ImageMetadata mergeImage(String rootFolder, String destFolder, String animName) throws IOException {
        return mergeImage(rootFolder, animName, destFolder, animName);
    }

    @Test
    public void generateTransparent() throws IOException {
        BufferedImage img = new BufferedImage(11, 24, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(img, "png", new File("/home/ihromant/tr.png"));
    }

    public static ImageMetadata mergeImage(String rootFolder, String animName, String destFolder, String destName) throws IOException {
        int xMax = 0;
        int yMax = 0;
        File root = new File(rootFolder, animName);
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
        ImageIO.write(Objects.requireNonNull(result), "PNG", new File(destFolder, destName + ".png"));
        return meta;
    }

    public enum HeroAnimStage {
        BASE, STANDING, UNHAPPY, HAPPY, CAST;
    }

    private static final int HERO_MAP_WIDTH = 96;
    private static final int HERO_MAP_HEIGHT = 64;

    private static final String[] STAGES = {"TOP", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM"};

    @Test
    public void mergeHeroAnimations() throws IOException {
        File root = new File("/home/ihromant/Games/units/images-shadow");
        for (int i = 0; i < ObjectNumberConstants.HERO_TYPES.length; i++) {
            File dir = new File(root, "ah" + (i < 10 ? "0" : "") + i + "_");
            File newDir = new File("/home/ihromant/workspace/ihromant.github.io/img/map/heroes/" + ObjectNumberConstants.HERO_TYPES[i].toLowerCase());
            newDir.mkdir();
            BufferedImage result = new BufferedImage(HERO_MAP_WIDTH, HERO_MAP_HEIGHT * 5, BufferedImage.TYPE_INT_ARGB);
            for (int j = 0; j < 5; j++) {
                BufferedImage img = ImageIO.read(new File(dir, "0" + j + "_00.png"));
                result.getGraphics().drawImage(img, 0, j * HERO_MAP_HEIGHT, null);
            }
            ImageIO.write(result, "png", new File(newDir, "staying.png"));
            for (int j = 5; j < 10; j++) {
                BufferedImage res = new BufferedImage(HERO_MAP_WIDTH, HERO_MAP_HEIGHT * 8, BufferedImage.TYPE_INT_ARGB);
                for (int k = 0; k < 8; k++) {
                    BufferedImage img = ImageIO.read(new File(dir, "0" + j + "_0" + k + ".png"));
                    res.getGraphics().drawImage(img, 0, k * HERO_MAP_HEIGHT, null);
                }
                ImageIO.write(res, "png", new File(newDir, "move_" + STAGES[j - 5].toLowerCase() + ".png"));
            }
        }
    }

    @Test
    public void mergeFlagAnimations() throws IOException {
        File root = new File("/home/ihromant/Games/units/images-shadow/");
        for (int i = 0; i < ObjectNumberConstants.KINGDOMS.length; i++) {
            File dir = new File(root, "af0" + i);
            File newDir = new File("/home/ihromant/workspace/ihromant.github.io/img/map/hero_flags/" + ObjectNumberConstants.KINGDOMS[i].toLowerCase());
            newDir.mkdir();
            for (int j = 0; j < 10; j++) {
                BufferedImage res = new BufferedImage(HERO_MAP_WIDTH, HERO_MAP_HEIGHT * 8, BufferedImage.TYPE_INT_ARGB);
                for (int k = 0; k < 8; k++) {
                    BufferedImage img = ImageIO.read(new File(dir, "0" + j + "_0" + k + ".png"));
                    res.getGraphics().drawImage(img, 0, k * HERO_MAP_HEIGHT, null);
                }
                ImageIO.write(res, "png", new File(newDir, j + "stage" + ".png"));
            }
        }
    }

    @Test
    public void removeBorder() throws IOException {
        File root = new File("/home/ihromant/workspace/ihromant.github.io/img/icons/48x32/bonus");
        for (File f : Objects.requireNonNull(root.listFiles())) {
            BufferedImage img = ImageIO.read(f);
            BufferedImage res = new BufferedImage(img.getWidth() - 2, img.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < res.getWidth(); i++) {
                for (int j = 0; j < res.getHeight(); j++) {
                    res.setRGB(i, j, img.getRGB(i + 1, j + 1));
                }
            }
            ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/icons/46x30/bonus", f.getName()));
        }
    }

    @Test
    public void transferCreatureIcons() throws IOException {
        File root = new File("/home/ihromant/Games/units/images-shadow/cprsmall");
        for (File f : Objects.requireNonNull(root.listFiles())) {
            String name = f.getName();
            int idx = Integer.parseInt(name.substring(name.indexOf('_') + 1, name.indexOf('.')), 10) - 2;
            if (idx >= 0 && idx < ObjectNumberConstants.CREATURES.length && ObjectNumberConstants.CREATURES[idx] != null) {
                String newName = ObjectNumberConstants.CREATURES[idx];
                FileUtils.copyFile(f, new File("/home/ihromant/workspace/ihromant.github.io/img/icons/32x32/creature", newName.toLowerCase() + ".png"));
            }
        }
    }

    private static final int TRANSPARENT = -16711681;

    @Test
    public void reconvertImages() throws IOException {
        for (File f : Objects.requireNonNull(new File("/home/ihromant/Games/units/images/hdicons").listFiles())) {
            BufferedImage img = ImageIO.read(f);
            String name = f.getName();
            BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    if (img.getRGB(i, j) != TRANSPARENT) {
                        res.setRGB(i, j, img.getRGB(i, j));
                    }
                }
            }
            ImageIO.write(res, "png", new File(f.getParent(), name.substring(0, name.indexOf('.')) + ".png"));
        }
    }

    @Test
    public void bmpToPng() throws IOException {
        String name = "checkbox.png";
        BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/icons/26x18", name));
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int transparent = img.getRGB(0, 0);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                if (img.getRGB(i, j) != transparent) {
                    res.setRGB(i, j, img.getRGB(i, j));
                }
            }
        }
        ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/icons/26x18", name.substring(0, name.indexOf('.')) + "1.png"));
    }

    @Test
    public void prepareForGraph() {
        System.out.println(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/stat.txt"))).lines()
                .map(l -> {
                    String[] parts = l.split(" ");
                    String[] dateParts = parts[0].split("-");
                    return "[new Date(" + dateParts[0] + ", " + dateParts[1] + ", " + dateParts[2] + "), " + parts[1] + "]";
                }).collect(Collectors.joining(", ", "[", "]")));
    }

    @Test
    public void generateArtifacts() throws IOException {
        for (int i = 1; i < ObjectNumberConstants.ARTIFACTS.length; i++) {
            String name = ObjectNumberConstants.ARTIFACTS[i];
            if (name == null) {
                continue;
            }
            String root = "/home/ihromant/Games/units/images/artifact";
            String art = "ava0" + (i < 10 ? "00" + i : i < 100 ? "0" + i : String.valueOf(i));
            File dir = new File(root, art);
            File[] files = Objects.requireNonNull(dir.listFiles());
            String destFdr = "/home/ihromant/workspace/ihromant.github.io/img/map/artifacts";
            String destNm = name.toLowerCase() + ".png";
            if (files.length == 1) {
                FileUtils.copyFile(files[0], new File(destFdr, destNm));
            } else {
                mergeImage(root, art, destFdr, destNm);
            }
        }
    }
}
