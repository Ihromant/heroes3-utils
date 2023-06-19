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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        mergeImage("/home/ihromant/Games/units/images/aabuttons/", "icn6432", "/home/ihromant/workspace/ihromant.github.io/img/buttons/64x32", "cancel_hire");
        //mergeImage("/home/ihromant/Games/units/images/aabuttons", "adoprta", "/home/ihromant/workspace/ihromant.github.io/img/buttons/11x24/", "right_arr");
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
        //System.out.println("x: " + (xMax + 1) + ", y: " + (yMax + 1));
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
    public void mergeSliders() throws IOException {
        String unpressed = "/home/ihromant/Games/units/images/radio/syslb_unpressed";
        String pressed = "/home/ihromant/Games/units/images/radio/syslb";
        for (int i = 0; i < 10; i++) {
            BufferedImage unp = ImageIO.read(new File(unpressed, "00_0" + i + ".png"));
            BufferedImage pr = ImageIO.read(new File(pressed, "00_0" + i + ".png"));
            BufferedImage result = new BufferedImage(unp.getWidth(), unp.getHeight() * 4, BufferedImage.TYPE_INT_ARGB);
            result.getGraphics().drawImage(unp, 0, 0, null);
            result.getGraphics().drawImage(unp, 0, unp.getHeight(), null);
            result.getGraphics().drawImage(pr, 0, pr.getHeight() * 3, null);
            ImageIO.write(result, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/buttons/18x36", "volume" + i + ".png"));
        }
    }

    @Test
    public void removeBorder() throws IOException {
        File root = new File("/home/ihromant/workspace/ihromant.github.io/img/icons/48x32");
        for (File f : Objects.requireNonNull(root.listFiles())) {
            BufferedImage img = ImageIO.read(f);
            BufferedImage res = new BufferedImage(img.getWidth() - 2, img.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < res.getWidth(); i++) {
                for (int j = 0; j < res.getHeight(); j++) {
                    res.setRGB(i, j, img.getRGB(i + 1, j + 1));
                }
            }
            ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/icons/46x30/hero", f.getName()));
        }
    }

    @Test
    public void moveImages() throws IOException {
        File root = new File("/home/ihromant/workspace/ihromant.github.io/img/map/town_flags");
        for (File f : Objects.requireNonNull(root.listFiles())) {
            BufferedImage img = ImageIO.read(f);
            BufferedImage res = new BufferedImage(192, 192, BufferedImage.TYPE_INT_ARGB);
            res.getGraphics().drawImage(img, 64, 160, null);
            ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/map/town_flags", f.getName()));
        }
    }

    @Test
    public void removeBorder1() throws IOException {
        BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/background/SpelBk2.png"));
        BufferedImage res = new BufferedImage(img.getWidth() - 6, img.getHeight() - 27, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < res.getWidth(); i++) {
            for (int j = 0; j < res.getHeight(); j++) {
                res.setRGB(i, j, img.getRGB(i + 3, j + 3));
            }
        }
        ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/background/spell_book.png"));
    }

    @Test
    public void generateBookmarks() throws IOException {
        File folder = new File("/home/ihromant/Games/units/images/radio/speltab/");
        BufferedImage[] result = IntStream.range(0, 5).mapToObj(i -> new BufferedImage(83, 59 * 4, BufferedImage.TYPE_INT_ARGB)).toArray(BufferedImage[]::new);
        for (int i = 0; i < 5; i++) {
            File file = new File(folder, "00_0" + i + ".png");
            BufferedImage img = ImageIO.read(file);
            int notActive = (i + 1) % 5;
            result[notActive].getGraphics().drawImage(img, 0, 0, 83, 58, 0, 59 * notActive, 83, 59 * notActive + 58, null);
            result[notActive].getGraphics().drawImage(img, 0, 59, 83, 59 + 58, 0, 59 * notActive, 83, 59 * notActive + 58, null);
            result[i].getGraphics().drawImage(img, 0, 59 * 3, 83, 59 * 3 + 58, 0, 59 * i, 83, 59 * i + 58, null);
        }
        for (int i = 0; i < 5; i++) {
            ImageIO.write(result[i], "png", new File("/home/ihromant/workspace/ihromant.github.io/img/buttons/83x59", "00_0" + i + ".png"));
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
            reconvertImage(f);
        }
    }

    @Test
    public void reconvertImage() throws IOException {
        reconvertImage(new File("/home/ihromant/Games/Heroes III Complete/_HD3_Data/Common/SpelBk2.bmp"));
    }

    private static void reconvertImage(File f) throws IOException {
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
        List<DataPart> list = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/stat.txt")))).lines()
                .map(l -> {
                    String[] parts = l.split(" ");
                    return new DataPart(LocalDate.parse(parts[0]), Integer.parseInt(parts[1]));
                }).toList();
        System.out.println(list.stream()
                .map(dp -> "[new Date(" + dp.date().getYear() + ", " + dp.date().getMonthValue() + ", " + dp.date().getDayOfMonth() + "), " + dp.loc() + "]")
                .collect(Collectors.joining(", ", "[", "]")));
        List<DataPart> speed = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            DataPart prev = list.get(i);
            DataPart next = list.get(i + 1);
            int sp = (next.loc() - prev.loc()) / (int) ChronoUnit.DAYS.between(prev.date(), next.date());
            if (sp > 500) {
                sp = 500;
            }
            if (sp < -500) {
                sp = -500;
            }
            speed.add(new DataPart(prev.date(), sp));
        }
        System.out.println(speed.stream()
                .map(dp -> "[new Date(" + dp.date().getYear() + ", " + dp.date().getMonthValue() + ", " + dp.date().getDayOfMonth() + "), " + dp.loc() + "]")
                .collect(Collectors.joining(", ", "[", "]")));
    }

    private record DataPart(LocalDate date, int loc) {}

    @Test
    public void generateArtifacts() throws IOException {
        for (int i = 1; i < ObjectNumberConstants.ARTIFACTS.length; i++) {
            String name = ObjectNumberConstants.ARTIFACTS[i];
            if (name == null) {
                continue;
            }
            String root = "/home/ihromant/Games/units/images-shadow";
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

    @Test
    public void generateMapUnits() throws IOException {
        String fdrName = "icm007q";
        String dest = "battle_defend";
        mergeImage("/home/ihromant/Games/units/hd-images/", fdrName, "/home/ihromant/workspace/ihromant.github.io/img/buttons/44x36", dest.toLowerCase());
    }

    private static final Map<String, String> renames = Map.of(
            "move_bottom.png", "move_south.png",
            "move_bottom_right.png", "move_south_east.png",
            "move_right.png", "move_east.png",
            "move_top.png", "move_north.png",
            "move_top_right.png", "move_north_east.png"
    );

    @Test
    public void rename() throws IOException {
        File folder = new File("/home/ihromant/workspace/ihromant.github.io/img/map/heroes");
        for (File child : Objects.requireNonNull(folder.listFiles())) {
            for (File img : Objects.requireNonNull(child.listFiles())) {
                String renameTo = renames.get(img.getName());
                if (renameTo != null) {
                    FileUtils.moveFile(img, new File(img.getParentFile(), renameTo));
                }
            }
        }
    }
}
