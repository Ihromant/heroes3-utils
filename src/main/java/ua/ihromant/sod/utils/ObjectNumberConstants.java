package ua.ihromant.sod.utils;

import ua.ihromant.sod.utils.map.SecondarySkill;

import static ua.ihromant.sod.utils.map.SecondarySkill.*;

public interface ObjectNumberConstants {
    String[] ARTIFACTS = {"SPELL_BOOK", null, null, "CATAPULT", "AMMO_CART", "BALLISTA", "FIRST_AID_TENT", "CENTAURS_AXE",
            "BLACKSHARD_OF_THE_DEAD_KNIGHT", "GREATER_GNOLLS_FLAIL", "OGRES_CLUB_OF_HAVOC", "SWORD_OF_HELLFIRE",
            "TITANS_GLADIUS", "SHIELD_OF_THE_DWARVEN_LORDS", "SHIELD_OF_THE_YAWNING_DEAD", "BUCKLER_OF_THE_GNOLL_KING",
            "TARG_OF_THE_RAMPAGING_OGRE", "SHIELD_OF_THE_DAMNED", "SENTINELS_SHIELD", "HELM_OF_THE_ALABASTER_UNICORN",
            "SKULL_HELMET", "HELM_OF_CHAOS", "CROWN_OF_THE_SUPREME_MAGI", "HELLSTORM_HELMET", "THUNDER_HELMET",
            "BREASTPLATE_OF_PETRIFIED_WOOD", "RIB_CAGE", "SCALES_OF_THE_GREATER_BASILISK", "TUNIC_OF_THE_CYCLOPS_KING",
            "BREASTPLATE_OF_BRIMSTONE", "TITANS_CUIRASS", "ARMOR_OF_WONDER", "SANDALS_OF_THE_SAINT", "CELESTIAL_NECKLACE_OF_BLISS",
            "LIONS_SHIELD_OF_COURAGE", "SWORD_OF_JUDGEMENT", "HELM_OF_HEAVENLY_ENLIGHTENMENT", "QUIET_EYE_OF_THE_DRAGON",
            "RED_DRAGON_FLAME_TONGUE", "DRAGON_SCALE_SHIELD", "DRAGON_SCALE_ARMOR", "DRAGONBONE_GREAVES", "DRAGON_WING_TABARD",
            "NECKLACE_OF_DRAGONTEETH", "CROWN_OF_DRAGONTOOTH", "STILL_EYE_OF_THE_DRAGON", "CLOVER_OF_FORTUNE", "CARDS_OF_PROPHECY",
            "LADYBIRD_OF_LUCK", "BADGE_OF_COURAGE", "CREST_OF_VALOR", "GLYPH_OF_GALLANTRY", "SPECULUM", "SPYGLASS",
            "AMULET_OF_THE_UNDERTAKER", "VAMPIRES_COWL", "DEAD_MANS_BOOTS", "GARNITURE_OF_INTERFERENCE", "SURCOAT_OF_COUNTERPOISE",
            "BOOTS_OF_POLARITY", "BOW_OF_ELVEN_CHERRYWOOD", "BOWSTRING_OF_THE_UNICORNS_MANE", "ANGEL_FEATHER_ARROWS",
            "BIRD_OF_PERCEPTION", "STOIC_WATCHMAN", "EMBLEM_OF_COGNIZANCE", "STATESMANS_MEDAL", "DIPLOMATS_RING",
            "AMBASSADORS_SASH", "RING_OF_THE_WAYFARER", "EQUESTRIANS_GLOVES", "NECKLACE_OF_OCEAN_GUIDANCE", "ANGEL_WINGS",
            "CHARM_OF_MANA", "TALISMAN_OF_MANA", "MYSTIC_ORB_OF_MANA", "COLLAR_OF_CONJURING", "RING_OF_CONJURING", "CAPE_OF_CONJURING",
            "ORB_OF_THE_FIRMAMENT", "ORB_OF_SILT", "ORB_OF_TEMPESTUOUS_FIRE", "ORB_OF_DRIVING_RAIN", "RECANTERS_CLOAK",
            "SPIRIT_OF_OPPRESSION", "HOURGLASS_OF_THE_EVIL_HOUR", "TOME_OF_FIRE", "TOME_OF_AIR", "TOME_OF_WATER", "TOME_OF_EARTH",
            "BOOTS_OF_LEVITATION", "GOLDEN_BOW", "SPHERE_OF_PERMANENCE", "ORB_OF_VULNERABILITY", "RING_OF_VITALITY", "RING_OF_LIFE",
            "VIAL_OF_LIFEBLOOD", "NECKLACE_OF_SWIFTNESS", "BOOTS_OF_SPEED", "CAPE_OF_VELOCITY", "PENDANT_OF_DISPASSION",
            "PENDANT_OF_SECOND_SIGHT", "PENDANT_OF_HOLINESS", "PENDANT_OF_LIFE", "PENDANT_OF_DEATH", "PENDANT_OF_FREE_WILL",
            "PENDANT_OF_NEGATIVITY", "PENDANT_OF_TOTAL_RECALL", "PENDANT_OF_COURAGE", "EVERFLOWING_CRYSTAL_CLOAK",
            "RING_OF_INFINITE_GEMS", "EVERPOURING_VIAL_OF_MERCURY", "INEXHAUSTIBLE_CART_OF_ORE", "EVERSMOKING_RING_OF_SULFUR",
            "INEXHAUSTIBLE_CART_OF_LUMBER", "ENDLESS_SACK_OF_GOLD", "ENDLESS_BAG_OF_GOLD", "ENDLESS_PURSE_OF_GOLD", "LEGS_OF_LEGION",
            "LOINS_OF_LEGION", "TORSO_OF_LEGION", "ARMS_OF_LEGION", "HEAD_OF_LEGION", "SEA_CAPTAINS_HAT", "SPELLBINDERS_HAT",
            "SHACKLES_OF_WAR", "ORB_OF_INHIBITION", "VIAL_OF_DRAGON_BLOOD", null, "ARMAGEDDONS_BLADE", "ANGELIC_ALLIANCE",
            "CLOAK_OF_THE_UNDEAD_KING", "ELIXIR_OF_LIFE", "ARMOR_OF_THE_DAMNED", "STATUE_OF_LEGION", "POWER_OF_THE_DRAGON_FATHER",
            "TITANS_THUNDER", "ADMIRALS_HAT", "BOW_OF_THE_SHARPSHOOTER", "WIZARDS_WELL", "RING_OF_THE_MAGI", "CORNUCOPIA"};

    String[] CREATURES = {"PIKEMAN", "HALBERDIER", "ARCHER", "MARKSMAN", "GRIFFIN", "ROYAL_GRIFFIN", "SWORDSMAN", "CRUSADER",
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

    String[] DWELLINGS = {"BASILISK_PIT", "BEHEMOTH_LAIR", "PILLAR_OF_EYES", "HALL_OF_DARKNESS", "DRAGON_VAULT", "TRAINING_GROUNDS",
            "CENTAUR_STABLES", "ALTAR_OF_AIR", "PORTAL_OF_GLORY", "CYCLOPS_CAVE", "FORSAKEN_PALACE", "SERPENT_FLY_HIVE",
            "DWARF_COTTAGE", "ALTAR_OF_EARTH", "FIRE_LAKE", "HOMESTEAD", "ALTAR_OF_FIRE", "PARAPET",
            "ALTAR_OF_WISHES", "WOLF_PEN", "GNOLL_HUT", "GOBLIN_BARRACKS", "HALL_OF_SINS", "GORGON_LAIR",
            "DRAGON_CLIFFS", "GRIFFIN_TOWER", "HARPY_LOFT", "KENNELS", "HYDRA_POND", "IMP_CRUCIBLE",
            "LIZARD_DEN", "MAGE_TOWER", "MANTICORE_LAIR", "CHAPEL_OF_STILLED_VOICES", "LABYRINTH", "MONASTERY",
            "GOLDEN_PAVILION", "DEMON_GATE", "OGRE_FORT", "ORC_TOWER", "HELL_HOLE", "DRAGON_CAVE",
            "CLIFF_NEST", "WORKSHOP", "CLOUD_TEMPLE", "DENDROID_ARCHES", "WARREN", "ALTAR_OF_WATER",
            "TOMB_OF_SOULS", "WYVERN_NEST", "ENCHANTED_SPRING", "UNICORN_GLADE_LARGE", "MAUSOLEUM", "ESTATE",
            "CURSED_TEMPLE", "GRAVEYARD", "GUARDHOUSE", "ARCHERS_TOWER", "BARRACKS", "MAGIC_LANTERN",
            "ALTAR_OF_THOUGHT", "PYRE", "FROZEN_CLIFFS", "CRYSTAL_CAVE", "MAGIC_FOREST", "SULFUROUS_LAIR",
            "ENCHANTERS_HOLLOW", "TREETOP_TOWER", "UNICORN_GLADE", "ALTAR_OF_AIR_ALT", "ALTAR_OF_EARTH_ALT", "ALTAR_OF_FIRE_ALT",
            "ALTAR_OF_WATER_ALT", "THATCHED_HUT", "HOVEL", "BOAR_GLEN", "TOMB_OF_CURSES", "NOMAD_TENT",
            "ROUGE_CAVERN", "TROLL_BRIDGE", null, null, "ALTAR_OF_WATER_ALT", "FORSAKEN_PALACE"};

    SecondarySkill[] SECONDARY = {
                            PATHFINDING, ARCHERY, LOGISTICS, SCOUTING,
                            DIPLOMACY, NAVIGATION, LEADERSHIP, WISDOM,
                            MYSTICISM, LUCK, BALLISTICS, EAGLE_EYE,
                            NECROMANCY, ESTATES, FIRE_MAGIC, AIR_MAGIC,
                            WATER_MAGIC, EARTH_MAGIC, SCHOLAR, TACTICS,
                            ARTILLERY, LEARNING, OFFENSE, ARMORER,
                            INTELLIGENCE, SORCERY, RESISTANCE, FIRST_AID};


    String[] SPELLS = {"SUMMON_BOAT", "SCUTTLE_BOAT", "VISIONS", "VIEW_EARTH", "DISGUISE", "VIEW_AIR", "FLY", "WATER_WALK",
            "DIMENSION_DOOR", "TOWN_PORTAL", "QUICKSAND", "LAND_MINES", "FORCE_FIELD", "FIRE_WALL", "EARTHQUAKE",
            "MAGIC_ARROW", "ICE_BOLT", "LIGHTNING_BOLT", "IMPLOSION", "CHAIN_LIGHTNING", "FROST_RING", "FIREBALL",
            "INFERNO", "METEOR_SHOWER", "DEATH_RIPPLE", "DESTROY_UNDEAD", "DESTROY_UNDEAD", "SHIELD", "AIR_SHIELD",
            "FIRE_SHIELD", "PROTECTION_FROM_AIR", "PROTECTION_FROM_FIRE", "PROTECTION_FROM_WATER", "PROTECTION_FROM_EARTH",
            "ANTIMAGIC", "DISPEL", "MAGIC_MIRROR", "CURE", "RESURRECTION", "ANIMATE_DEAD", "SACRIFICE", "BLESS", "CURSE",
            "BLOODLUST", "PRECISION", "WEAKNESS", "STONE_SKIN", "DISRUPTING_RAY", "PRAYER", "MIRTH", "SORROW", "FORTUNE",
            "MISFORTUNE", "HASTE", "SLOW", "SLAYER", "FRENZY", "TITANS_LIGHTNING_BOLT", "COUNTERSTRIKE", "BERSERK",
            "HYPNOTIZE", "FORGETFULNESS", "BLIND", "TELEPORT", "REMOVE_OBSTACLE", "CLONE", "FIRE_ELEMENTAL",
            "EARTH_ELEMENTAL", "WATER_ELEMENTAL", "AIR_ELEMENTAL"};

    String[] RESOURCES = {"WOOD", "MERCURY", "ORE", "SULFUR", "CRYSTAL", "GEMS", "GOLD"};

    String[] CASTLES = {"CASTLE", "RAMPART", "TOWER", "INFERNO", "NECROPOLIS", "DUNGEON", "STRONGHOLD", "FORTRESS", "CONFLUX"};

    String[] KINGDOMS = {"RED", "BLUE", "TAN", "GREEN", "ORANGE", "PURPLE", "TEAL", "PINK"};

    String[] HEROES = {
            "ORRIN", "VALESKA", "EDRIC", "SYLVIA", "LORD_HAART", "SORSHA", "CHRISTIAN", "TYRIS",
            "RION", "ADELA", "CUTHBERT", "ADELAIDE", "INGHAM", "SANYA", "LOYNIS", "CAITLIN",
            "MEPHALA", "UFRETIN", "JENOVA", "RYLAND", "THORGRIM", "IVOR", "CLANCY", "KYRRE",
            "CORONIUS", "ULAND", "ELLESHAR", "GEM", "MALCOM", "MELODIA", "ALAGAR", "AERIS",
            "PIQUEDRAM", "THANE", "JOSEPHINE", "NEELA", "TOROSAR", "FAFNER", "RISSA", "IONA",
            "ASTRAL", "HALON", "SERENA", "DAREMYTH", "THEODORUS", "SOLMYR", "CYRA", "AINE",
            "FIONA", "RASHKA", "MARIUS", "IGNATIUS", "OCTAVIA", "CALH", "PYRE", "NYMUS",
            "AYDEN", "XYRON", "AXSIS", "OLEMA", "CALID", "ASH", "ZYDAR", "XARFAX",
            "STRAKER", "VOKIAL", "MOANDOR", "CHARNA", "TAMIKA", "ISRA", "CLAVIUS", "GALTHRAN",
            "SEPTIENNA", "AISLINN", "SANDRO", "NIMBUS", "THANT", "XSI", "VIDOMINA", "NAGASH",
            "LORELEI", "ARLACH", "DACE", "AJIT", "DAMACON", "GUNNAR", "SYNCA", "SHAKTI",
            "ALAMAR", "JAEGAR", "MALEKITH", "JEDDITE", "GEON", "DEEMER", "SEPHINROTH", "DARKSTORN",
            "YOG", "GURNISSON", "JABARKAS", "SHIVA", "GRETCHIN", "KRELLION", "CRAG_HACK", "TYRAXOR",
            "GIRD", "VEY", "DESSA", "TEREK", "ZUBIN", "GUNDULA", "ORIS", "SAURUG",
            "BRON", "DRAKON", "WYSTAN", "TAZAR", "ALKIN", "KORBAC", "GERWULF", "BROGHILD",
            "MIRLANDA", "ROSIC", "VOY", "VERDISH", "MERIST", "STYG", "ANDRA", "TIVA",
            "PASIS", "THUNAR", "IGNISSA", "LACUS", "MONERE", "ERDAMON", "FIUR", "KALT",
            "LUNA", "BRISSA", "CIELE", "LABETHA", "INTEUS", "AENAIN", "GELARE", "GRINDAN",
            "SIR_MULLICH"};

    String[] HERO_TYPES = {
            "KNIGHT", "CLERIC", "RANGER", "DRUID", "ALCHEMIST", "WIZARD",
            "DEMONIAC", "HERETIC", "DEATH_KNIGHT", "NECROMANCER", "OVERLORD", "WARLOCK",
            "BARBARIAN", "BATTLE_MAGE", "BEASTMASTER", "WITCH", "PLANESWALKER", "ELEMENTALIST"
    };
}
