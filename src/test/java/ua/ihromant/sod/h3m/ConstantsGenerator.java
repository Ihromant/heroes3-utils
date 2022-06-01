package ua.ihromant.sod.h3m;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConstantsGenerator {
    @Test
    public void generateConstants() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/objectclasses.txt")))).lines()
                .filter(l -> l.startsWith("#define"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[2]), arr -> arr[1], (a, b) -> a, TreeMap::new));
        values.forEach((val, str) -> System.out.println("int " + str + " = " + val + ";"));
    }

    @Test
    public void generateArtifactsLegacy() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("/legacyIds/artifacts.properties"));
        int max = new HashMap<>(prop).values().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        Map<Integer, String> reversed = new HashMap<>(prop).entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getValue().toString()), e -> e.getKey().toString()));
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> reversed.get(i) == null ? "null" : '"' + reversed.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }

    @Test
    public void generateDwellingsLegacy() {
        new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.txt")))).lines()
                .filter(l -> !l.startsWith("#"))
                .forEach(line -> {
                    int separator = line.indexOf('"');
                    String left = line.substring(0, separator);
                    String right = line.substring(separator + 1);
                    String[] split = left.split("\\s+");
                    String name = right.substring(0, right.indexOf('"')).replace(' ', '_')
                            .replace("'", "").toUpperCase();
                    System.out.println(String.valueOf(Integer.parseInt(split[1])) + ';' + split[4] + ';' + name);
                });
    }

    @Test
    public void generateCreatures() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/creatures.txt")))).lines()
                .filter(l -> !l.startsWith("#"))
                .map(l -> {
                    int separator = l.indexOf('"');
                    String left = l.substring(0, separator);
                    String right = l.substring(separator + 1);
                    return new String[] { left.split("\\s+")[0],
                            right.substring(0, right.indexOf('"')).replace(' ', '_').toUpperCase()};
                })
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> arr[1]));
        values.forEach((val, str) -> System.out.println(str + "=" + val));
    }

    @Test
    public void generateCreaturesLegacy() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("/legacyIds/creatures.properties"));
        int max = new HashMap<>(prop).values().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        Map<Integer, String> reversed = new HashMap<>(prop).entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getValue().toString()), e -> e.getKey().toString()));
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> reversed.get(i) == null ? "null" : '"' + reversed.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }

    @Test
    public void generateDwellings() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.csv")))).lines()
            .skip(1)
                .map(s -> s.split(";"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> arr[2]));
        int max = values.keySet().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> values.get(i) == null ? "null" : '"' + values.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }

    @Test
    public void generateDwellingsSQL() {
        Map<Integer, Integer> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.csv")))).lines()
                .skip(1)
                .map(s -> s.split(";"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> Integer.parseInt(arr[1])));
        IntStream.range(0, 12)
                .forEach(i -> {
                    AtomicInteger counter = new AtomicInteger();
                    IntStream.range(i * 14, Math.min(i * 14 + 14, 145)).forEach(un -> values.entrySet().stream().filter(e -> e.getValue() == un)
                            .forEach(e -> System.out.println("("
                            + "(SELECT MAX(id) + " + counter.incrementAndGet() + " FROM dwelling_type),"
                            + "'" + DWELLINGS[e.getKey()] + "',"
                            + "(SELECT id from creature where full_name = '" + CREATURES[un] + "')"
                            + "),"))
                    );
                    System.out.println();
                });
    }

    public static final String[] CREATURES = {"PIKEMAN", "HALBERDIER", "ARCHER", "MARKSMAN", "GRIFFIN", "ROYAL_GRIFFIN", "SWORDSMAN", "CRUSADER",
            "MONK", "ZEALOT", "CAVALIER", "CHAMPION", "ANGEL", "ARCHANGEL", "CENTAUR", "CENTAUR_CAPTAIN", "DWARF", "BATTLE_DWARF",
            "WOOD_ELF", "GRAND_ELF", "PEGASUS", "SILVER_PEGASUS", "DENDROID_GUARD", "DENDROID_SOLDIER", "UNICORN", "WAR_UNICORN",
            "GREEN_DRAGON", "GOLD_DRAGON", "GREMLIN", "MASTER_GREMLIN", "STONE_GARGOYLE", "OBSIDIAN_GARGOYLE", "STONE_GOLEM",
            "IRON_GOLEM", "MAGE", "ARCH_MAGE", "GENIE", "MASTER_GENIE", "NAGA", "NAGA_QUEEN", "GIANT", "TITAN", "IMP", "FAMILIAR",
            "GOG", "MAGOG", "HELL_HOUND", "CERBERUS", "DEMON", "HORNED_DEMON", "PIT_FIEND", "PIT_LORD", "EFREET", "EFREET_SULTAN",
            "DEVIL", "ARCH_DEVIL", "SKELETON", "SKELETON_WARRIOR", "WALKING_DEAD", "ZOMBIE", "WIGHT", "WRAITH", "VAMPIRE",
            "VAMPIRE_LORD", "LICH", "POWER_LICH", "BLACK_KNIGHT", "DREAD_KNIGHT", "BONE_DRAGON", "GHOST_DRAGON", "TROGLODYTE",
            "INFERNAL_TROGLODYTE", "HARPY", "HARPY_HAG", "BEHOLDER", "EVIL_EYE", "MEDUSA", "MEDUSA_QUEEN", "MINOTAUR",
            "MINOTAUR_KING", "MANTICORE", "SCORPICORE", "RED_DRAGON", "BLACK_DRAGON", "GOBLIN", "HOBGOBLIN", "WOLF_RIDER",
            "WOLF_RAIDER", "ORC", "ORC_CHIEFTAIN", "OGRE", "OGRE_MAGE", "ROC", "THUNDER_BIRD", "CYCLOPS", "CYCLOPS_KING", "BEHEMOTH",
            "ANCIENT_BEHEMOTH", "GNOLL", "GNOLL_MARAUDER", "LIZARDMAN", "LIZARD_WARRIOR", "GORGON", "MIGHTY_GORGON", "SERPENT_FLY",
            "DRAGON_FLY", "BASILISK", "GREATER_BASILISK", "WYVERN", "WYVERN_MONARCH", "HYDRA", "CHAOS_HYDRA", "AIR_ELEMENTAL",
            "EARTH_ELEMENTAL", "FIRE_ELEMENTAL", "WATER_ELEMENTAL", "GOLD_GOLEM", "DIAMOND_GOLEM", "PIXIE", "SPRITE",
            "PSYCHIC_ELEMENTAL", "MAGIC_ELEMENTAL", null, "ICE_ELEMENTAL", null, "MAGMA_ELEMENTAL", null, "STORM_ELEMENTAL", null,
            "ENERGY_ELEMENTAL", "FIREBIRD", "PHOENIX", "AZURE_DRAGON", "CRYSTAL_DRAGON", "FAERIE_DRAGON", "RUST_DRAGON", "ENCHANTER",
            "SHARPSHOOTER", "HALFLING", "PEASANT", "BOAR", "MUMMY", "NOMAD", "ROGUE", "TROLL"};

    public static final String[] DWELLINGS = {"BASILISK_PIT", "BEHEMOTH_LAIR", "PILLAR_OF_EYES", "HALL_OF_DARKNESS", "DRAGON_VAULT", "TRAINING_GROUNDS",
            "CENTAUR_STABLES", "ALTAR_OF_AIR", "PORTAL_OF_GLORY", "CYCLOPS_CAVE", "FORSAKEN_PALACE", "SERPENT_FLY_HIVE", "DWARF_COTTAGE",
            "ALTAR_OF_EARTH", "FIRE_LAKE", "HOMESTEAD", "ALTAR_OF_FIRE", "PARAPET", "ALTAR_OF_WISHES", "WOLF_PEN", "GNOLL_HUT",
            "GOBLIN_BARRACKS", "HALL_OF_SINS", "GORGON_LAIR", "DRAGON_CLIFFS", "GRIFFIN_TOWER", "HARPY_LOFT", "KENNELS", "HYDRA_POND",
            "IMP_CRUCIBLE", "LIZARD_DEN", "MAGE_TOWER", "MANTICORE_LAIR", "CHAPEL_OF_STILLED_VOICES", "LABYRINTH", "MONASTERY",
            "GOLDEN_PAVILION", "DEMON_GATE", "OGRE_FORT", "ORC_TOWER", "HELL_HOLE", "DRAGON_CAVE", "CLIFF_NEST", "WORKSHOP",
            "CLOUD_TEMPLE", "DENDROID_ARCHES", "WARREN", "ALTAR_OF_WATER", "TOMB_OF_SOULS", "WYVERN_NEST", "ENCHANTED_SPRING",
            "UNICORN_GLADE_LARGE", "MAUSOLEUM", "ESTATE", "CURSED_TEMPLE", "GRAVEYARD", "GUARDHOUSE", "ARCHERS_TOWER", "BARRACKS",
            "MAGIC_LANTERN", "ALTAR_OF_THOUGHT", "PYRE", "FROZEN_CLIFFS", "CRYSTAL_CAVE", "MAGIC_FOREST", "SULFUROUS_LAIR",
            "ENCHANTERS_HOLLOW", "TREETOP_TOWER", "UNICORN_GLADE", "ALTAR_OF_AIR_ALT", "ALTAR_OF_EARTH_ALT", "ALTAR_OF_FIRE_ALT", "ALTAR_OF_WATER_ALT",
            "THATCHED_HUT", "HOVEL", "BOAR_GLEN", "TOMB_OF_CURSES", "NOMAD_TENT", "ROUGE_CAVERN", "TROLL_BRIDGE", null, null,
            "ALTAR_OF_WATER_ALT", "FORSAKEN_PALACE"};
}
