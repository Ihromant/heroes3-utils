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

    private static final int[][] FLAG_LEFT_DWELLING = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][] IMP_CACHE_FLAG = {
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1}
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
            FlagData gfg = determineGenCrd(f.getName(), img, height);
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

    @Test
    public void generateDwellingFlags() throws IOException {
        paintFlagData("altar_of_air_alt.png", 96, new Point(50, 80), FLAG_LEFT_DWELLING);
        paintFlagData("altar_of_earth_alt.png", 96, new Point(40, 66), FLAG_LEFT_DWELLING);
        paintFlagData("altar_of_fire_alt.png", 96, new Point(36, 70), FLAG_LEFT_DWELLING);
        paintFlagData("altar_of_thought.png", 96, new Point(60, 36), FLAG_LEFT_DWELLING);
        paintFlagData("altar_of_water_alt.png", 96, new Point(26, 26), FLAG_LEFT_DWELLING);
        paintFlagData("altar_of_wishes.png", 160, new Point(48, 130), FLAG_LEFT_DWELLING);
        paintFlagData("archers_tower.png", 96, new Point(48, 92), FLAG_LEFT_DWELLING);
        paintFlagData("barracks.png", 96, new Point(44, 90), FLAG_LEFT);
        paintFlagData("basilisk_pit.png", 64, new Point(34, 52), FLAG_LEFT);
        paintFlagData("behemoth_lair.png", 96, new Point(40, 76), FLAG_LEFT_DWELLING);
        paintFlagData("boar_glen.png", 64, new Point(64, 35), FLAG_LEFT_DWELLING);
        paintFlagData("centaur_stables.png", 64, new Point(36, 60), FLAG_LEFT_DWELLING);
        paintFlagData("chapel_of_stilled_voices.png", 96, new Point(46, 92), FLAG_LEFT);
        paintFlagData("cliff_nest.png", 128, new Point(53, 114), FLAG_LEFT);
        paintFlagData("cloud_temple.png", 96, new Point(36, 56), FLAG_LEFT);
        paintFlagData("crystal_cave.png", 96, new Point(22, 36), FLAG_LEFT);
        paintFlagData("cursed_temple.png", 64, new Point(40, 60), FLAG_LEFT);
        paintFlagData("cyclops_cave.png", 96, new Point(46, 80), FLAG_LEFT_DWELLING);
        paintFlagData("demon_gate.png", 96, new Point(50, 72), FLAG_LEFT_DWELLING);
        paintFlagData("dendroid_arches.png", 96, new Point(54, 35), FLAG_LEFT);
        paintFlagData("dragon_cave.png", 64, new Point(60, 35), FLAG_LEFT);
        paintFlagData("dragon_cliffs.png", 128, new Point(50, 114), FLAG_LEFT_DWELLING);
        paintFlagData("dragon_vault.png", 128, new Point(48, 105), FLAG_LEFT);
        paintFlagData("dwarf_cottage.png", 96, new Point(44, 84), FLAG_LEFT_DWELLING);
        paintFlagData("enchanted_spring.png", 128, new Point(66, 78), FLAG_LEFT);
        paintFlagData("enchanters_hollow.png", 96, new Point(60, 35), FLAG_LEFT_DWELLING);
        paintFlagData("estate.png", 64, new Point(40, 35), FLAG_LEFT);
        paintFlagData("fire_lake.png", 96, new Point(54, 70), FLAG_LEFT_DWELLING);
        paintFlagData("forsaken_palace.png", 96, new Point(48, 90), FLAG_LEFT_DWELLING);
        paintFlagData("frozen_cliffs.png", 96, new Point(30, 35), FLAG_LEFT_DWELLING);
        paintFlagData("gnoll_hut.png", 64, new Point(48, 54), FLAG_LEFT);
        paintFlagData("goblin_barracks.png", 96, new Point(46, 70), FLAG_LEFT_DWELLING);
        paintFlagData("golden_pavilion.png", 128, new Point(46, 80), FLAG_LEFT);
        paintFlagData("gorgon_lair.png", 96, new Point(66, 35), FLAG_LEFT);
        paintFlagData("graveyard.png", 64, new Point(63, 50), FLAG_LEFT);
        paintFlagData("griffin_tower.png", 96, new Point(48, 95), FLAG_LEFT_DWELLING);
        paintFlagData("guardhouse.png", 96, new Point(50, 82), FLAG_LEFT);
        paintFlagData("hall_of_darkness.png", 128, new Point(50, 90), FLAG_LEFT);
        paintFlagData("hall_of_sins.png", 64, new Point(63, 40), FLAG_LEFT_DWELLING);
        paintFlagData("harpy_loft.png", 96, new Point(66, 35), FLAG_LEFT);
        paintFlagData("hell_hole.png", 64, new Point(66, 40), FLAG_LEFT);
        paintFlagData("homestead.png", 96, new Point(60, 60), FLAG_LEFT_DWELLING);
        paintFlagData("hovel.png", 96, new Point(60, 35), FLAG_LEFT_DWELLING);
        paintFlagData("hydra_pond.png", 64, new Point(60, 50), FLAG_LEFT);
        paintFlagData("imp_crucible.png", 96, new Point(44, 66), IMP_CACHE_FLAG);
        paintFlagData("kennels.png", 64, new Point(64, 50), FLAG_LEFT_DWELLING);
        paintFlagData("labyrinth.png", 64, new Point(36, 35), FLAG_LEFT);
        paintFlagData("lizard_den.png", 96, new Point(46, 70), FLAG_LEFT);
        paintFlagData("mage_tower.png", 160, new Point(48, 136), FLAG_LEFT);
        paintFlagData("magic_forest.png", 96, new Point(76, 35), FLAG_LEFT_DWELLING);
        paintFlagData("magic_lantern.png", 96, new Point(63, 36), FLAG_LEFT);
        paintFlagData("manticore_lair.png", 64, new Point(24, 42), FLAG_LEFT);
        paintFlagData("mausoleum.png", 96, new Point(63, 35), FLAG_LEFT);
        paintFlagData("monastery.png", 96, new Point(48, 72), FLAG_LEFT);
        paintFlagData("nomad_tent.png", 96, new Point(62, 35), FLAG_LEFT_DWELLING);
        paintFlagData("ogre_fort.png", 128, new Point(48, 112), FLAG_LEFT);
        paintFlagData("orc_tower.png", 128, new Point(46, 116), FLAG_LEFT);
        paintFlagData("parapet.png", 128, new Point(64, 35), FLAG_LEFT_DWELLING);
        paintFlagData("pillar_of_eyes.png", 96, new Point(68, 48), FLAG_LEFT);
        paintFlagData("portal_of_glory.png", 64, new Point(90, 35), FLAG_LEFT_DWELLING);
        paintFlagData("pyre.png", 96, new Point(20, 19), FLAG_LEFT_DWELLING);
        paintFlagData("rogue_cavern.png", 96, new Point(60, 56), FLAG_LEFT_DWELLING);
        paintFlagData("serpent_fly_hive.png", 96, new Point(24, 20), FLAG_LEFT);
        paintFlagData("sulfurous_lair.png", 96, new Point(40, 60), FLAG_LEFT);
        paintFlagData("thatched_hut.png", 96, new Point(60, 35), FLAG_LEFT_DWELLING);
        paintFlagData("tomb_of_curses.png", 96, new Point(23, 35), FLAG_LEFT_DWELLING);
        paintFlagData("tomb_of_souls.png", 128, new Point(73, 44), FLAG_LEFT);
        paintFlagData("training_grounds.png", 96, new Point(66, 35), FLAG_LEFT_DWELLING);
        paintFlagData("treetop_tower.png", 96, new Point(56, 73), FLAG_LEFT_DWELLING);
        paintFlagData("troll_bridge.png", 96, new Point(64, 35), FLAG_LEFT_DWELLING);
        paintFlagData("unicorn_glade.png", 96, new Point(56, 35), FLAG_LEFT_DWELLING);
        paintFlagData("unicorn_glade_large.png", 96, new Point(24, 35), FLAG_LEFT);
        paintFlagData("warren.png", 96, new Point(52, 66), FLAG_LEFT);
        paintFlagData("wolf_pen.png", 96, new Point(44, 84), FLAG_LEFT);
        paintFlagData("workshop.png", 96, new Point(40, 94), FLAG_LEFT_DWELLING);
        paintFlagData("wyvern_nest.png", 96, new Point(56, 68), FLAG_LEFT);
    }

    private void paintFlagData(String fName, int height, Point from, int[][] flag) throws IOException {
        BufferedImage img = ImageIO.read(new File("/home/ihromant/workspace/ihromant.github.io/img/map/dwellings", fName));
        FlagData data = new FlagData(height, from, flag);
        Point p = data.determineCrd(img);
        System.out.println(fName + " " + p);
        String name = fName.substring(0, fName.indexOf('.'));
        for (Kingdom k : Kingdom.values()) {
            BufferedImage res = new BufferedImage(img.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
            paintFlag(res, p, data.flag, k);
            ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/map/dwelling_flags/"
                    + name + "_" + k.name().toLowerCase() + ".png"));
        }
    }

    private FlagData determineGenCrd(String filename, BufferedImage img, int height) {
        if (filename.startsWith("gold")) {
            return new FlagData(height, new Point(64, 57), FLAG_LEFT);
        }
        if (filename.equals("sawmill_snow.png")) {
            return new FlagData(height, new Point(50, 60), FLAG_GEN_SAWMILL_SNOW_MISALIGNED);
        }
        if (filename.startsWith("sawmill")) {
            return new FlagData(height, new Point(70, 34), FLAG_GEN_SAWMILL_MISALIGNED);
        }
        if (filename.startsWith("sulfur")) {
            return new FlagData(height, new Point(23, 37), FLAG_LEFT);
        }
        if (filename.startsWith("ore")) {
            return new FlagData(height, new Point(35, 37), FLAG_LEFT);
        }
        if (filename.startsWith("alch")) {
            return new FlagData(height, new Point(60, 73), FLAG_GEN_SAWMILL_MISALIGNED);
        }
        if (filename.startsWith("gem")) {
            return new FlagData(height, new Point(25, 37), FLAG_LEFT);
        }
        if (List.of("crystal_cavern_dirt.png", "crystal_cavern_grass.png", "crystal_cavern_lava.png",
                "crystal_cavern_subterranean.png").contains(filename)) {
            return new FlagData(height, new Point(25, 37), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_snow.png")) {
            return new FlagData(height, new Point(65, 84), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_sand.png")) {
            return new FlagData(height, new Point(65, 92), FLAG_LEFT);
        }
        if (filename.equals("crystal_cavern_swamp.png")) {
            return new FlagData(height, new Point(69, 94), FLAG_LEFT);
        }
        if (filename.startsWith("crys")) {
            return new FlagData(height, new Point(56, 70), FLAG_LEFT);
        }
        return null;
    }

    private record FlagData(int height, Point shift, int[][] flag) {
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
