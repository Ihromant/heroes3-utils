package ua.ihromant.sod.utils;

import static ua.ihromant.sod.utils.ObjectNumberConstants.*;

public enum ObjectType {
    META_OBJECT_ABANDONED_MINE_ABSOD,
    META_OBJECT_ARTIFACT,
    META_OBJECT_ARTIFACT_AB,
    META_OBJECT_ARTIFACT_SOD,
    META_OBJECT_DWELLING,
    META_OBJECT_DWELLING_ABSOD,
    META_OBJECT_EVENT,
    META_OBJECT_GARRISON,
    META_OBJECT_GARRISON_ABSOD,
    META_OBJECT_GENERIC_BOAT,
    META_OBJECT_GENERIC_IMPASSABLE_TERRAIN,
    META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD,
    META_OBJECT_GENERIC_PASSABLE_TERRAIN,
    META_OBJECT_GENERIC_PASSABLE_TERRAIN_SOD,
    META_OBJECT_GENERIC_TREASURE,
    META_OBJECT_GENERIC_VISITABLE,
    META_OBJECT_GENERIC_VISITABLE_ABSOD,
    META_OBJECT_GRAIL,
    META_OBJECT_HERO,
    META_OBJECT_HERO_AB,
    META_OBJECT_LIGHTHOUSE,
    META_OBJECT_MONOLITH_TWO_WAY,
    META_OBJECT_MONSTER,
    META_OBJECT_MONSTER_ABSOD,
    META_OBJECT_OCEAN_BOTTLE,
    META_OBJECT_PANDORAS_BOX,
    META_OBJECT_PLACEHOLDER_HERO,
    META_OBJECT_PRISON,
    META_OBJECT_QUEST_GUARD,
    META_OBJECT_RANDOM_DWELLING_ABSOD,
    META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD,
    META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD,
    META_OBJECT_RANDOM_HERO,
    META_OBJECT_RESOURCE,
    META_OBJECT_RESOURCE_GENERATOR,
    META_OBJECT_SCHOLAR,
    META_OBJECT_SEERS_HUT,
    META_OBJECT_SHIPYARD,
    META_OBJECT_SHRINE,
    META_OBJECT_SIGN,
    META_OBJECT_SPELL_SCROLL,
    META_OBJECT_SUBTERRANEAN_GATE,
    META_OBJECT_TOWN,
    META_OBJECT_TOWN_ABSOD,
    META_OBJECT_WITCH_HUT;

    public static ObjectType objectNumberToType(int objectClass) {
        switch (objectClass) {
            // RoE
            case H3M_OA_CLASS_NONE_0:
            case H3M_OA_CLASS_BLANK_40:
            case H3M_OA_CLASS_IMPASSABLE_BRUSH:
            case H3M_OA_CLASS_IMPASSABLE_BUSH:
            case H3M_OA_CLASS_IMPASSABLE_CACTUS:
            case H3M_OA_CLASS_IMPASSABLE_CANYON:
            case H3M_OA_CLASS_IMPASSABLE_CRATER:
            case H3M_OA_CLASS_IMPASSABLE_DEADVEGETATION:
            case H3M_OA_CLASS_IMPASSABLE_FLOWERS:
            case H3M_OA_CLASS_IMPASSABLE_FROZENLIKE:
            case H3M_OA_CLASS_IMPASSABLE_HEDGE:
            case H3M_OA_CLASS_IMPASSABLE_HILL:
            case H3M_OA_CLASS_IMPASSABLE_LAKE:
            case H3M_OA_CLASS_IMPASSABLE_LAVAFLOW:
            case H3M_OA_CLASS_IMPASSABLE_LAVALAKE:
            case H3M_OA_CLASS_IMPASSABLE_MUSHROOMS:
            case H3M_OA_CLASS_IMPASSABLE_LOG:
            case H3M_OA_CLASS_IMPASSABLE_MANDRAKE:
            case H3M_OA_CLASS_IMPASSABLE_MOSS:
            case H3M_OA_CLASS_IMPASSABLE_MOUND:
            case H3M_OA_CLASS_IMPASSABLE_MOUNTAIN:
            case H3M_OA_CLASS_IMPASSABLE_OAKTREES:
            case H3M_OA_CLASS_IMPASSABLE_OUTCROPPING:
            case H3M_OA_CLASS_IMPASSABLE_PINETREES:
            case H3M_OA_CLASS_IMPASSABLE_PLANT:
            case H3M_OA_CLASS_IMPASSABLE_RIVERDELTA:
            case H3M_OA_CLASS_IMPASSABLE_ROCK:
            case H3M_OA_CLASS_IMPASSABLE_SANDDUNE:
            case H3M_OA_CLASS_IMPASSABLE_SANDPIT:
            case H3M_OA_CLASS_IMPASSABLE_SHRUB:
            case H3M_OA_CLASS_IMPASSABLE_SKULL:
            case H3M_OA_CLASS_IMPASSABLE_STALAGMITE:
            case H3M_OA_CLASS_IMPASSABLE_STUMP:
            case H3M_OA_CLASS_IMPASSABLE_TARPIT:
            case H3M_OA_CLASS_IMPASSABLE_TREES:
            case H3M_OA_CLASS_IMPASSABLE_VINE:
            case H3M_OA_CLASS_IMPASSABLE_VOLCANICVENT:
            case H3M_OA_CLASS_IMPASSABLE_VOLCANO:
            case H3M_OA_CLASS_IMPASSABLE_WILLOWTREES:
            case H3M_OA_CLASS_IMPASSABLE_YUCCATREES:
            case H3M_OA_CLASS_IMPASSABLE_REEF:
                return META_OBJECT_GENERIC_IMPASSABLE_TERRAIN;
            // AB
            case H3M_OA_CLASS_IMPASSABLE_BRUSH2:
            case H3M_OA_CLASS_IMPASSABLE_BUSH2:
            case H3M_OA_CLASS_IMPASSABLE_CACTUS2:
            case H3M_OA_CLASS_IMPASSABLE_CANYON2:
            case H3M_OA_CLASS_IMPASSABLE_CRATER2:
            case H3M_OA_CLASS_IMPASSABLE_DEADVEGETATION2:
            case H3M_OA_CLASS_IMPASSABLE_FLOWERS2:
            case H3M_OA_CLASS_IMPASSABLE_FROZENLIKE2:
            case H3M_OA_CLASS_IMPASSABLE_HEDGE2:
            case H3M_OA_CLASS_IMPASSABLE_HILL2:
            case H3M_OA_CLASS_IMPASSABLE_LAKE2:
            case H3M_OA_CLASS_IMPASSABLE_LAVAFLOW2:
            case H3M_OA_CLASS_IMPASSABLE_LAVALAKE2:
            case H3M_OA_CLASS_IMPASSABLE_MUSHROOMS2:
            case H3M_OA_CLASS_IMPASSABLE_LOG2:
            case H3M_OA_CLASS_IMPASSABLE_MANDRAKE2:
            case H3M_OA_CLASS_IMPASSABLE_MOSS2:
            case H3M_OA_CLASS_IMPASSABLE_MOUND2:
            case H3M_OA_CLASS_IMPASSABLE_MOUNTAIN2:
            case H3M_OA_CLASS_IMPASSABLE_OAKTREES2:
            case H3M_OA_CLASS_IMPASSABLE_OUTCROPPING2:
            case H3M_OA_CLASS_IMPASSABLE_PINETREES2:
            case H3M_OA_CLASS_IMPASSABLE_PLANT2:
            case H3M_OA_CLASS_IMPASSABLE_RIVERDELTA2:
            case H3M_OA_CLASS_IMPASSABLE_ROCK2:
            case H3M_OA_CLASS_IMPASSABLE_SANDDUNE2:
            case H3M_OA_CLASS_IMPASSABLE_SANDPIT2:
            case H3M_OA_CLASS_IMPASSABLE_SHRUB2:
            case H3M_OA_CLASS_IMPASSABLE_SKULL2:
            case H3M_OA_CLASS_IMPASSABLE_STALAGMITE2:
            case H3M_OA_CLASS_IMPASSABLE_STUMP2:
            case H3M_OA_CLASS_IMPASSABLE_TARPIT2:
            case H3M_OA_CLASS_IMPASSABLE_TREES2:
            case H3M_OA_CLASS_IMPASSABLE_VINE2:
            case H3M_OA_CLASS_IMPASSABLE_VOLCANICVENT2:
            case H3M_OA_CLASS_IMPASSABLE_VOLCANO2:
            case H3M_OA_CLASS_IMPASSABLE_WILLOWTREES2:
            case H3M_OA_CLASS_IMPASSABLE_YUCCATREES2:
            case H3M_OA_CLASS_IMPASSABLE_REEF2:
            case H3M_OA_CLASS_IMPASSABLE_DESERTHILLS:
            case H3M_OA_CLASS_IMPASSABLE_DIRTHILLS:
            case H3M_OA_CLASS_IMPASSABLE_GRASSHILLS:
            case H3M_OA_CLASS_IMPASSABLE_ROUGHHILLS:
            case H3M_OA_CLASS_IMPASSABLE_SUBTERRANEANROCKS:
            case H3M_OA_CLASS_IMPASSABLE_SWAMPFOLIAGE:
                return META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD;
            case H3M_OA_CLASS_ALTAR_OF_SACRIFICE:
            case H3M_OA_CLASS_ANCHOR_POINT:
            case H3M_OA_CLASS_ARENA:
            case H3M_OA_CLASS_BLACK_MARKET:
            case H3M_OA_CLASS_CARTOGRAPHER:
            case H3M_OA_CLASS_BUOY:
            case H3M_OA_CLASS_SWAN_POND:
            case H3M_OA_CLASS_COVER_OF_DARKNESS:
            case H3M_OA_CLASS_CREATURE_BANK:
            case H3M_OA_CLASS_CORPSE:
            case H3M_OA_CLASS_MARLETTO_TOWER:
            case H3M_OA_CLASS_DERELICT_SHIP:
            case H3M_OA_CLASS_DRAGON_UTOPIA:
            case H3M_OA_CLASS_EYE_OF_MAGI:
            case H3M_OA_CLASS_FAERIE_RING:
            case H3M_OA_CLASS_FOUNTAIN_OF_FORTUNE:
            case H3M_OA_CLASS_FOUNTAIN_OF_YOUTH:
            case H3M_OA_CLASS_GARDEN_OF_REVELATION:
            case H3M_OA_CLASS_HILL_FORT:
            case H3M_OA_CLASS_HUT_OF_MAGI:
            case H3M_OA_CLASS_IDOL_OF_FORTUNE:
            case H3M_OA_CLASS_LEAN_TO:
            case H3M_OA_CLASS_LIBRARY_OF_ENLIGHTENMENT:

            case H3M_OA_CLASS_SCHOOL_OF_MAGIC:
            case H3M_OA_CLASS_MAGIC_SPRING:
            case H3M_OA_CLASS_MAGIC_WELL:
            case H3M_OA_CLASS_MERCENARY_CAMP:
            case H3M_OA_CLASS_MERMAID:
            case H3M_OA_CLASS_MYSTICAL_GARDEN:
            case H3M_OA_CLASS_OASIS:
            case H3M_OA_CLASS_OBELISK:
            case H3M_OA_CLASS_REDWOOD_OBSERVATORY:
            case H3M_OA_CLASS_PILLAR_OF_FIRE:
            case H3M_OA_CLASS_STAR_AXIS:
            case H3M_OA_CLASS_RALLY_FLAG:
            case H3M_OA_CLASS_BORDERGUARD:
            case H3M_OA_CLASS_KEYMASTER:
            case H3M_OA_CLASS_REFUGEE_CAMP:
            case H3M_OA_CLASS_SANCTUARY:
            case H3M_OA_CLASS_CRYPT:
            case H3M_OA_CLASS_SHIPWRECK:
            case H3M_OA_CLASS_SIRENS:
            case H3M_OA_CLASS_STABLES:
            case H3M_OA_CLASS_TAVERN:
            case H3M_OA_CLASS_TEMPLE:
            case H3M_OA_CLASS_DEN_OF_THIEVES:
            case H3M_OA_CLASS_TRADING_POST:
            case H3M_OA_CLASS_LEARNING_STONE:
            case H3M_OA_CLASS_TREE_OF_KNOWLEDGE:
            case H3M_OA_CLASS_UNIVERSITY:
            case H3M_OA_CLASS_WAGON:
            case H3M_OA_CLASS_WAR_MACHINE_FACTORY:
            case H3M_OA_CLASS_SCHOOL_OF_WAR:
            case H3M_OA_CLASS_WARRIORS_TOMB:
            case H3M_OA_CLASS_WATER_WHEEL:
            case H3M_OA_CLASS_WATERING_HOLE:
            case H3M_OA_CLASS_WHIRLPOOL:
            case H3M_OA_CLASS_WINDMILL:
            case H3M_OA_CLASS_MARKET_OF_TIME:
            case H3M_OA_CLASS_DECORATIVE_TOWN:
                return META_OBJECT_GENERIC_VISITABLE;
            case H3M_OA_CLASS_TRADING_POST_SNOW:
            case H3M_OA_CLASS_PYRAMID:
            case H3M_OA_CLASS_BORDER_GATE:
            case H3M_OA_CLASS_FREELANCERS_GUILD:
                return META_OBJECT_GENERIC_VISITABLE_ABSOD;
            case H3M_OA_CLASS_ARTIFACT:
            case H3M_OA_CLASS_RANDOM_ART:
            case H3M_OA_CLASS_RANDOM_TREASURE_ART:
            case H3M_OA_CLASS_RANDOM_MINOR_ART:
            case H3M_OA_CLASS_RANDOM_MAJOR_ART:
            case H3M_OA_CLASS_RANDOM_RELIC_ART:
                return META_OBJECT_ARTIFACT;
            case H3M_OA_CLASS_ABANDONED_MINE:   // TODO check object_number
                return META_OBJECT_ABANDONED_MINE_ABSOD;
            case H3M_OA_CLASS_CREATURE_GENERATOR1:
            case H3M_OA_CLASS_CREATURE_GENERATOR2:
            case H3M_OA_CLASS_CREATURE_GENERATOR3:
            case H3M_OA_CLASS_CREATURE_GENERATOR4:
                return META_OBJECT_DWELLING;
            case H3M_OA_CLASS_EVENT:
                return META_OBJECT_EVENT;
            case H3M_OA_CLASS_GARRISON:
                return META_OBJECT_GARRISON;
            case H3M_OA_CLASS_GARRISON2:
                return META_OBJECT_GARRISON_ABSOD;
            case H3M_OA_CLASS_BOAT:
                return META_OBJECT_GENERIC_BOAT;
            case H3M_OA_CLASS_CLOVER_FIELD:
            case H3M_OA_CLASS_EVIL_FOG:
            case H3M_OA_CLASS_FAVORABLE_WINDS:
            case H3M_OA_CLASS_FIERY_FIELDS:
            case H3M_OA_CLASS_HOLY_GROUNDS:
            case H3M_OA_CLASS_LUCID_POOLS:
            case H3M_OA_CLASS_MAGIC_CLOUDS:
            case H3M_OA_CLASS_ROCKLANDS:
            case H3M_OA_CLASS_CURSED_GROUND2:
            case H3M_OA_CLASS_MAGIC_PLAINS2:
            case H3M_OA_CLASS_PASSABLE_139:
            case H3M_OA_CLASS_PASSABLE_140:
            case H3M_OA_CLASS_PASSABLE_141:
            case H3M_OA_CLASS_PASSABLE_142:
            case H3M_OA_CLASS_PASSABLE_144:
            case H3M_OA_CLASS_PASSABLE_145:
            case H3M_OA_CLASS_PASSABLE_146:
                return META_OBJECT_GENERIC_PASSABLE_TERRAIN_SOD;
            case H3M_OA_CLASS_HOLE:
            case H3M_OA_CLASS_CURSED_GROUND1:
            case H3M_OA_CLASS_MAGIC_PLAINS1:
            case H3M_OA_CLASS_PASSABLE_KELP:
            case H3M_OA_CLASS_PASSABLE_KELP2:
            case H3M_OA_CLASS_PASSABLE_HOLE2:
                return META_OBJECT_GENERIC_PASSABLE_TERRAIN;
            case H3M_OA_CLASS_PANDORAS_BOX:
                return META_OBJECT_PANDORAS_BOX;
            case H3M_OA_CLASS_GRAIL:
                return META_OBJECT_GRAIL;
            case H3M_OA_CLASS_HERO:
                return META_OBJECT_HERO;
            case H3M_OA_CLASS_LIGHTHOUSE:
                return META_OBJECT_LIGHTHOUSE;
            case H3M_OA_CLASS_MONOLITH_TWO_WAY:  // TODO once monoliths have been divided fix this
                //return META_OBJECTMONOLITH_TWO_WAY;
            case H3M_OA_CLASS_MONOLITH_ONE_WAY_ENTRANCE:
            case H3M_OA_CLASS_MONOLITH_ONE_WAY_EXIT:
                return META_OBJECT_GENERIC_VISITABLE_ABSOD;
            case H3M_OA_CLASS_MONSTER:
            case H3M_OA_CLASS_RANDOM_MONSTER:
            case H3M_OA_CLASS_RANDOM_MONSTER_L1:
            case H3M_OA_CLASS_RANDOM_MONSTER_L2:
            case H3M_OA_CLASS_RANDOM_MONSTER_L3:
            case H3M_OA_CLASS_RANDOM_MONSTER_L4:
            case H3M_OA_CLASS_RANDOM_MONSTER_L5:
            case H3M_OA_CLASS_RANDOM_MONSTER_L6:
            case H3M_OA_CLASS_RANDOM_MONSTER_L7:
                return META_OBJECT_MONSTER;
            case H3M_OA_CLASS_OCEAN_BOTTLE:
                return META_OBJECT_OCEAN_BOTTLE;
            case H3M_OA_CLASS_PRISON:
                return META_OBJECT_PRISON;
            case H3M_OA_CLASS_QUEST_GUARD:
                return META_OBJECT_QUEST_GUARD;
            case H3M_OA_CLASS_RANDOM_DWELLING:
                return META_OBJECT_RANDOM_DWELLING_ABSOD;
            case H3M_OA_CLASS_RANDOM_DWELLING_LVL:
                return META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD;
            case H3M_OA_CLASS_RANDOM_DWELLING_FACTION:
                return META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD;
            case H3M_OA_CLASS_RANDOM_HERO:
                return META_OBJECT_RANDOM_HERO;
            case H3M_OA_CLASS_HERO_PLACEHOLDER:
                return META_OBJECT_PLACEHOLDER_HERO;
            case H3M_OA_CLASS_RESOURCE:
            case H3M_OA_CLASS_RANDOM_RESOURCE:
                return META_OBJECT_RESOURCE;
            case H3M_OA_CLASS_MINE:
                return META_OBJECT_RESOURCE_GENERATOR;
            case H3M_OA_CLASS_SCHOLAR:
                return META_OBJECT_SCHOLAR;
            case H3M_OA_CLASS_SEER_HUT:
                return META_OBJECT_SEERS_HUT;
            case H3M_OA_CLASS_SHIPYARD:
                return META_OBJECT_SHIPYARD;
            case H3M_OA_CLASS_SHRINE_OF_MAGIC_INCANTATION:
            case H3M_OA_CLASS_SHRINE_OF_MAGIC_GESTURE:
            case H3M_OA_CLASS_SHRINE_OF_MAGIC_THOUGHT:
                return META_OBJECT_SHRINE;
            case H3M_OA_CLASS_SIGN:
                return META_OBJECT_SIGN;
            case H3M_OA_CLASS_SPELL_SCROLL:
                return META_OBJECT_SPELL_SCROLL;
            case H3M_OA_CLASS_SUBTERRANEAN_GATE:
                return META_OBJECT_SUBTERRANEAN_GATE;
            case H3M_OA_CLASS_TOWN:
            case H3M_OA_CLASS_RANDOM_TOWN:
                return META_OBJECT_TOWN;
            case H3M_OA_CLASS_WITCH_HUT:
                return META_OBJECT_WITCH_HUT;
            case H3M_OA_CLASS_CAMPFIRE:
            case H3M_OA_CLASS_FLOTSAM:
            case H3M_OA_CLASS_SEA_CHEST:
            case H3M_OA_CLASS_SHIPWRECK_SURVIVOR:
            case H3M_OA_CLASS_TREASURE_CHEST:
                return META_OBJECT_GENERIC_TREASURE;
            default:
                throw new IllegalArgumentException("Received attribute " + objectClass);
        }
    }
}
