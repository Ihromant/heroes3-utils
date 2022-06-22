package ua.ihromant.sod.utils;

import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.bytes.Utils;
import ua.ihromant.sod.utils.entities.AiHeroSettings;
import ua.ihromant.sod.utils.entities.AiRumor;
import ua.ihromant.sod.utils.map.BackgroundType;
import ua.ihromant.sod.utils.bytes.ByteWrapper;
import ua.ihromant.sod.utils.entities.BasicInformation;
import ua.ihromant.sod.utils.entities.CommonGuardian;
import ua.ihromant.sod.utils.entities.CommonHero;
import ua.ihromant.sod.utils.entities.CommonReward;
import ua.ihromant.sod.utils.entities.CommonSecondarySkill;
import ua.ihromant.sod.utils.entities.Coordinates;
import ua.ihromant.sod.utils.entities.CreatureSlot;
import ua.ihromant.sod.utils.entities.CustomHero;
import ua.ihromant.sod.utils.entities.HeroArtifacts;
import ua.ihromant.sod.utils.map.MapMetadata;
import ua.ihromant.sod.utils.entities.MapMonster;
import ua.ihromant.sod.utils.entities.MapTile;
import ua.ihromant.sod.utils.entities.MessageAndTreasure;
import ua.ihromant.sod.utils.entities.MessageBearer;
import ua.ihromant.sod.utils.entities.ObjectAttribute;
import ua.ihromant.sod.utils.entities.ObjectData;
import ua.ihromant.sod.utils.entities.PlaceholderHero;
import ua.ihromant.sod.utils.entities.PlayerMetadata;
import ua.ihromant.sod.utils.entities.PrimarySkills;
import ua.ihromant.sod.utils.entities.StartingTownMetadata;
import ua.ihromant.sod.utils.entities.StaticGarrison;
import ua.ihromant.sod.utils.entities.MapTown;
import ua.ihromant.sod.utils.entities.TownEvent;
import ua.ihromant.sod.utils.map.ObjectGroup;
import ua.ihromant.sod.utils.map.RiverType;
import ua.ihromant.sod.utils.map.RoadType;
import ua.ihromant.sod.utils.map.SecondarySkill;

import java.nio.BufferUnderflowException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static ua.ihromant.sod.utils.ObjectType.*;

@Setter
@Accessors(chain = true)
public class H3MParser {
    private static final int H3M_FORMAT_ROE = 0x0000000E;
    private static final int H3M_FORMAT_AB = 0x00000015;
    private static final int H3M_FORMAT_SOD = 0x0000001C;
    private static final int H3M_FORMAT_CHR = 0x0000001D;
    private static final int H3M_FORMAT_WOG = 0x00000033;

    private Consumer<ObjectData> dataInterceptor;

    public MapMetadata parse(byte[] bytes) {
        MapMetadata map = new MapMetadata();
        ByteWrapper wrap = new ByteWrapper(bytes);
        int format = wrap.readInt();
        boolean isROE = format == H3M_FORMAT_ROE;
        boolean isSoD = format == H3M_FORMAT_SOD;
        map.setBasic(new BasicInformation()
                .setAtLeast1Hero(wrap.readBoolean())
                .setMapSize(wrap.readInt())
                .setTwoLevel(wrap.readBoolean())
                .setMapName(wrap.readString())
                .setMapDescription(wrap.readString())
                .setMapDifficulty(wrap.readUnsigned()));
        if (!isROE) {
            map.getBasic().setMasteryLevelCap(wrap.readUnsigned());
        }
        for (int i = 0; i < 8; i++) {
            boolean ai;
            PlayerMetadata player = new PlayerMetadata().setCanBeHuman(wrap.readBoolean())
                    .setCanBeComputer(wrap.readBoolean())
                    .setBehavior(wrap.readUnsigned())
                    .setAllowedAlignments(isSoD ? wrap.readUnsigned() : null)
                    .setTownTypes(isROE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                    .setOwnsRandomTown(wrap.readBoolean());
            boolean hasMainTown = wrap.readBoolean();
            player.setStartingTown(hasMainTown ? new StartingTownMetadata().setStartingTownCreateHero(isROE ? null : wrap.readBoolean())
                    .setStartingTownType(isROE ? null : wrap.readUnsigned())
                    .setCoordinates(readCoordinates(wrap)) : null);
            boolean startingHeroIsRandom = wrap.readBoolean();
            int startingHeroType = wrap.readUnsigned();
            if (!hasMainTown) {
                if (startingHeroType == 0xFF) {
                    if (isROE || player.getTownTypes() != 0) {
                        ai = true;
                    } else {
                        ai = false;
                        player.setStartingHeroIsRandom(startingHeroIsRandom)
                                .setStartingHeroType(startingHeroType)
                                .setStartingHeroFace(wrap.readUnsigned())
                                .setStartingHeroName(wrap.readString());
                    }
                } else {
                    ai = true;
                    player.setStartingHeroIsRandom(startingHeroIsRandom)
                            .setStartingHeroType(startingHeroType)
                            .setStartingHeroFace(wrap.readUnsigned())
                            .setStartingHeroName(wrap.readString());
                }
            } else {
                ai = startingHeroType != 0xFF;
                player.setStartingHeroIsRandom(startingHeroIsRandom)
                        .setStartingHeroType(startingHeroType)
                        .setStartingHeroFace(wrap.readUnsigned())
                        .setStartingHeroName(wrap.readString());
            }
            if (!isROE && ai) {
                wrap.readUnsigned(); // unknown1
                int heroesCount = wrap.readInt();
                for (int j = 0 ; j < heroesCount; j++) {
                    wrap.readUnsigned(); // type
                    wrap.readString(); // name
                }
            }
            map.getPlayersMetadata()[i] = player;
        }
        parseWinCondition(wrap, isROE);
        parseLoseCondition(wrap);
        parseAiTeams(wrap);
        int[] availableHeroes = wrap.readUnsigned(isROE ? 16 : 20);
        if (!isROE) {
            int empty = wrap.readInt();
        }
        if (isSoD) {
            int customHeroesCount = wrap.readUnsigned();
            for (int i = 0; i < customHeroesCount; i++) {
                new CustomHero().setType(wrap.readUnsigned())
                        .setFace(wrap.readUnsigned())
                        .setName(wrap.readString())
                        .setAllowedPlayers(wrap.readUnsigned());
            }
        }
        wrap.readUnsigned(31); // 31 empty bytes
        if (!isROE) {
            int[] avaliableArtifacts = wrap.readUnsigned(isSoD ? 18 : 17);
            if (isSoD) {
                int[] availableSpells = wrap.readUnsigned(9);
                SecondarySkill[] availableSkills = readSkills(wrap.readUnsigned(4));
            }
        }
        int rumorsCount = wrap.readInt();
        for (int i = 0; i < rumorsCount; i++) {
            new AiRumor().setName(wrap.readString())
                    .setDesc(wrap.readString());
        }
        for (int i = 0; i < (isSoD ? 156 : 0); i++) {
            if (!wrap.readBoolean()) {
                continue;
            }
            AiHeroSettings heroSettings = new AiHeroSettings();
            if (wrap.readBoolean()) {
                heroSettings.setExperience(wrap.readInt());
            }
            if (wrap.readBoolean()) {
                heroSettings.setSecondarySkills(readSecondarySkills(wrap, wrap.readInt()));
            }
            if (wrap.readBoolean()) {
                heroSettings.setArtConf(parseHeroArtifacts(wrap, isROE, isSoD));
            }
            if (wrap.readBoolean()) {
                heroSettings.setBiography(wrap.readString());
            }
            heroSettings.setGender(wrap.readUnsigned());
            if (wrap.readBoolean()) {
                heroSettings.setSpells(wrap.readUnsigned(9));
            }
            if (wrap.readBoolean()) {
                heroSettings.setPrimary(readPrimarySkills(wrap));
            }
        }
        map.setTiles(new MapTile[map.getBasic().isTwoLevel() ? 2 : 1][map.getBasic().getMapSize()][map.getBasic().getMapSize()]);
        for (int z = 0; z < map.getTiles().length; z++) {
            MapTile[][] level = map.getTiles()[z];
            for (int y = 0; y < map.getBasic().getMapSize(); y++) {
                MapTile[] row = level[y];
                for (int x = 0; x < map.getBasic().getMapSize(); x++) {
                    row[x] = new MapTile().setTerrainType(BackgroundType.values()[wrap.readUnsigned()])
                            .setTerrainSprite(wrap.readUnsigned())
                            .setRiverType(RiverType.values()[wrap.readUnsigned()])
                            .setRiverSprite(wrap.readUnsigned())
                            .setRoadType(RoadType.values()[wrap.readUnsigned()])
                            .setRoadSprite(wrap.readUnsigned())
                            .setMirroring(wrap.readUnsigned());
                }
            }
        }
        map.setObjectAttributes(new ObjectAttribute[wrap.readInt()]);
        for (int i = 0; i < map.getObjectAttributes().length; i++) {
            map.getObjectAttributes()[i] = new ObjectAttribute().setDef(wrap.readString())
                    .setPassable(wrap.readUnsigned(6))
                    .setActive(wrap.readUnsigned(6))
                    .setAllowedLandsapes(wrap.readUnsignedShort())
                    .setLandscapeGroup(wrap.readUnsignedShort())
                    .setObjectClass(wrap.readInt())
                    .setObjectNumber(wrap.readInt())
                    .setObjectGroup(ObjectGroup.values()[wrap.readUnsigned()])
                    .setAbove(wrap.readUnsigned())
                    .setUnknown(wrap.readUnsigned(16));
            map.getObjectAttributes()[i].setType(objectNumberToType(map.getObjectAttributes()[i].getObjectClass()));
        }
        map.setObjectData(new ObjectData[wrap.readInt()]);
        for (int i = 0; i < map.getObjectData().length; i++) {
            ObjectData data = new ObjectData().setX(wrap.readUnsigned())
                    .setY(wrap.readUnsigned())
                    .setZ(wrap.readUnsigned());
            map.getObjectData()[i] = data;
            data.setOa(map.getObjectAttributes()[wrap.readInt()]);
            data.setUnknown1(wrap.readUnsigned(5));
            ObjectType type = data.getOa().getType();
            if (dataInterceptor != null) {
                dataInterceptor.accept(data);
            }
            switch (type) {
                case META_OBJECT_PLACEHOLDER_HERO:
                    PlaceholderHero hero = new PlaceholderHero().setOwner(wrap.readUnsigned())
                            .setType(wrap.readUnsigned());
                    if (hero.getType() == 0xFF) {
                        hero.setPowerRating(wrap.readUnsigned());
                    }
                    break;
                case META_OBJECT_QUEST_GUARD:
                    int questType = wrap.readUnsigned();
                    parseQuestRequest(wrap, isROE, questType);
                    break;
                case META_OBJECT_PANDORAS_BOX:
                    if (wrap.readBoolean()) {
                        readCommonGuardian(wrap, isROE);
                    }
                    CommonReward reward = readCommonReward(wrap, isROE);
                    break;
                case META_OBJECT_SIGN:
                case META_OBJECT_OCEAN_BOTTLE:
                    new MessageBearer().setMessage(wrap.readString())
                            .setUnknown1(wrap.readUnsigned(4));
                    break;
                case META_OBJECT_GARRISON:
                case META_OBJECT_GARRISON_ABSOD:
                    new StaticGarrison().setOwner(wrap.readInt())
                            .setCreatures(readArmy(wrap, isROE))
                            .setRemovableUnits(isROE ? 0 : wrap.readUnsigned())
                            .setUnknown1(wrap.readUnsigned(8));
                    break;
                case META_OBJECT_EVENT:
                    if (wrap.readBoolean()) {
                        readCommonGuardian(wrap, isROE);
                    }
                    readCommonReward(wrap, isROE);
                    wrap.readUnsigned(); // applies to players
                    wrap.readUnsigned(); // applies to computers
                    wrap.readUnsigned(); // cancel after visit
                    wrap.readUnsigned(4); // unknown1
                    break;
                case META_OBJECT_GRAIL:
                    wrap.readInt(); // allowable radius
                    break;
                case META_OBJECT_DWELLING:
                case META_OBJECT_DWELLING_ABSOD:
                case META_OBJECT_LIGHTHOUSE:
                case META_OBJECT_RESOURCE_GENERATOR:
                case META_OBJECT_SHIPYARD:
                case META_OBJECT_ABANDONED_MINE_ABSOD:
                    wrap.readInt(); // owner
                    break;
                case META_OBJECT_GENERIC_BOAT:
                case META_OBJECT_GENERIC_PASSABLE_TERRAIN:
                case META_OBJECT_GENERIC_PASSABLE_TERRAIN_SOD:
                case META_OBJECT_GENERIC_IMPASSABLE_TERRAIN:
                case META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD:
                case META_OBJECT_GENERIC_VISITABLE:
                case META_OBJECT_GENERIC_VISITABLE_ABSOD:
                case META_OBJECT_GENERIC_TREASURE:
                case META_OBJECT_MONOLITH_TWO_WAY:
                case META_OBJECT_SUBTERRANEAN_GATE:
                    // Generic objects have no body
                    break;
                case META_OBJECT_TOWN:
                case META_OBJECT_TOWN_ABSOD:
                    readTown(wrap, isROE, isSoD);
                    break;
                case META_OBJECT_RANDOM_DWELLING_ABSOD:
                case META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD:
                case META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD:
                    wrap.readInt(); // owner
                    if (type != META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD) {
                        int abSodId = wrap.readInt();
                        if (abSodId == 0) {
                            wrap.readUnsignedShort(); // alignment
                        }
                    }
                    if (type != META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD) {
                        wrap.readUnsigned(); // min level
                        wrap.readUnsigned(); // max level
                    }
                    break;
                case META_OBJECT_HERO:
                case META_OBJECT_RANDOM_HERO:
                case META_OBJECT_PRISON:
                    parseHero(wrap, isROE, isSoD);
                    break;
                case META_OBJECT_MONSTER:
                case META_OBJECT_MONSTER_ABSOD:
                    parseMonster(wrap, isROE);
                    break;
                case META_OBJECT_ARTIFACT:
                case META_OBJECT_ARTIFACT_AB:
                case META_OBJECT_ARTIFACT_SOD:
                    CommonGuardian grd = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null; // only optional guardian here
                    break;
                case META_OBJECT_SHRINE:
                    wrap.readInt(); // spell
                    break;
                case META_OBJECT_SPELL_SCROLL:
                    CommonGuardian grd1 = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null;
                    wrap.readInt(); // spell
                    break;
                case META_OBJECT_RESOURCE:
                    CommonGuardian grd2 = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null;
                    wrap.readInt(); // quantity
                    wrap.readUnsigned(4); // unknown1
                    break;
                case META_OBJECT_WITCH_HUT:
                    SecondarySkill[] potentialSkills = isROE ? null : readSkills(wrap.readUnsigned(4));
                    break;
                case META_OBJECT_SEERS_HUT:
                    int abmbigious = wrap.readUnsigned(); // in ROE it's artifact request, otherwise see code below
                    if (!isROE) {
                        parseQuestRequest(wrap, isROE, abmbigious);
                    }
                    parseReward(wrap, isROE);
                    wrap.readUnsigned(2); // unknown1
                    break;
                case META_OBJECT_SCHOLAR:
                    wrap.readUnsigned(); // reward type
                    wrap.readUnsigned(); // reward value
                    wrap.readUnsigned(6); // unknown
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        int eventCount = wrap.readInt();
        for (int i = 0; i < eventCount; i++) {
            wrap.readString(); // event name;
            wrap.readString(); // event message
            wrap.readInt(7); // resources
            wrap.readUnsigned(); // applies to players
            Integer appliesToHuman = isSoD ? wrap.readUnsigned() : null;
            wrap.readUnsigned(); // applies to computer
            wrap.readUnsignedShort(); // first occurence
            wrap.readUnsigned(); // subsequent occurences
            wrap.readUnsigned(17); // unknown1
        }
        wrap.readInt();
        wrap.readInt();
        try {
            wrap.readUnsigned(); // HD
        } catch (BufferUnderflowException e) {
            return map; // normal
        }
        wrap.readUnsigned(115);
        try {
            wrap.readUnsigned();
        } catch (BufferUnderflowException e) {
            return map; // normal
        }
        throw new IllegalStateException();
    }

    private SecondarySkill[] readSkills(int[] masks) {
        return IntStream.range(0, 4).boxed()
                .flatMap(i -> Utils.ones(masks[i], Math.min(ObjectNumberConstants.SECONDARY.length - 8 * i, 8))
                        .mapToObj(j -> ObjectNumberConstants.SECONDARY[8 * i + j])).toArray(SecondarySkill[]::new);
    }

    private Coordinates readCoordinates(ByteWrapper wrap) {
        return new Coordinates().setX(wrap.readUnsigned())
                .setY(wrap.readUnsigned())
                .setZ(wrap.readUnsigned());
    }

    private void parseAiTeams(ByteWrapper wrap) {
        int teamsCount = wrap.readUnsigned(); // teamsCount
        if (teamsCount != 0) {
            wrap.readUnsigned(8);
        }
    }

    private void parseLoseCondition(ByteWrapper wrap) {
        int loseCond = wrap.readUnsigned();
        if (loseCond == 0xFF) {
            return;
        }
        switch (loseCond) {
            case 0: // lose town
            case 1: // lose hero
                readCoordinates(wrap);
                break;
            case 2: // time
                wrap.readUnsignedShort(); // days
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void parseWinCondition(ByteWrapper wrap, boolean isRoE) {
        int winCond = wrap.readUnsigned();
        if (winCond == 0xFF) {
            return;
        }
        wrap.readUnsigned(); // allow normal win
        wrap.readUnsigned(); // applies to computer;
        switch (winCond) {
            case 0: // acquire artifact
                readArtifact(wrap, isRoE);
                return;
            case 1: // accumulate creatures
                new CreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                        .setQuantity(wrap.readInt()); // can't use readCreature
                return;
            case 2: // accumulate resources
                readResource(wrap);
                return;
            case 8: // flag dwellings
            case 9: // flag mines
                return;
        }
        switch (winCond) {
            case 3: // upgrade town
                readCoordinates(wrap);
                wrap.readUnsigned(); // 0 - town, 1 - city, 2 - capitol
                wrap.readUnsigned(); // 0 - fort, 1 - citadel, 2 - castle
                break;
            case 4: // build grail
            case 5: // defeat hero
            case 6: // capture town
            case 7: // defeat monster
                readCoordinates(wrap);
                break;
            case 10: // transport artifact
                wrap.readUnsigned(); // type
                readCoordinates(wrap);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void readResource(ByteWrapper wrap) {
        wrap.readUnsigned(); // 0 - wood, 1 - mercury, 2 - ore, 3 - sulfur, 4 - crystal, 5 - gems, 6 - gold
        wrap.readInt(); // amount
    }

    private void parseReward(ByteWrapper wrap, boolean isRoE) { // TODO merge with common reward
        int rewardType = wrap.readUnsigned();
        switch (rewardType) {
            case 0: // none
                break;
            case 1: // experience
                wrap.readInt();
                break;
            case 2: // spell points
                wrap.readInt();
                break;
            case 3: // morale
                wrap.readUnsigned();
                break;
            case 4: // luck
                wrap.readUnsigned();
                break;
            case 5: // resource
                readResource(wrap);
                break;
            case 6: // primary skill
                wrap.readUnsigned();
                wrap.readUnsigned();
                break;
            case 7: // secondary skill
                wrap.readUnsigned();
                wrap.readUnsigned();
                break;
            case 8: // artifact
                readArtifact(wrap, isRoE);
                break;
            case 9: // spell
                wrap.readUnsigned();
                break;
            case 10:
                readCreature(wrap, isRoE);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void parseQuestRequest(ByteWrapper wrap, boolean isROE, int questType) {
        switch (questType) {
            case 0: // none
                break;
            case 1: // experience
                wrap.readInt();
                break;
            case 2: // primary skills
                readPrimarySkills(wrap);
                break;
            case 3: // defeat hero
                wrap.readInt();
                break;
            case 4: // defeat monster
                wrap.readInt();
                break;
            case 5: // artifacts
                readArtifacts(wrap, isROE);
                break;
            case 6: // creatures
                readArmy(wrap, isROE, wrap.readUnsigned());
                break;
            case 7: // resources
                readResources(wrap);
                break;
            case 8: // be hero
                wrap.readUnsigned();
                break;
            case 9: // be player
                wrap.readUnsigned();
                break;
            case 0xFF:
                break;
            default:
                throw new IllegalArgumentException();
        }
        wrap.readInt(); // deadline
        if (questType != 0xFF) {
            wrap.readString(); // proposal message
            wrap.readString(); // progress message
            wrap.readString(); // completion message
        }
    }

    private MapMonster parseMonster(ByteWrapper wrap, boolean isRoE) {
        return new MapMonster().setAbSodId(isRoE ? null : wrap.readInt())
                .setQuantity(wrap.readUnsignedShort())
                .setDisposition(wrap.readUnsigned())
                .setMessTreasure(wrap.readBoolean() ? new MessageAndTreasure().setMessage(wrap.readString())
                        .setResources(readResources(wrap))
                        .setArtifact(readArtifact(wrap, isRoE)) : null)
                .setNeverFlees(wrap.readUnsigned())
                .setDoesNotGrow(wrap.readUnsigned())
                .setUnknown1(wrap.readUnsigned(2));
    }

    private CommonHero parseHero(ByteWrapper wrap, boolean isRoE, boolean isSoD) {
        CommonHero result = new CommonHero().setAbSodId(isRoE ? null : wrap.readInt())
                .setOwner(wrap.readUnsigned())
                .setType(wrap.readUnsigned())
                .setName(wrap.readBoolean() ? wrap.readString() : null);
        if (isSoD) {
            result.setExperience(wrap.readBoolean() ? wrap.readInt() : null);
        } else {
            result.setExperience(wrap.readInt());
        }
        result.setFace(wrap.readBoolean() ? wrap.readUnsigned() : null)
                .setSecondarySkills(wrap.readBoolean() ? readSecondarySkills(wrap, wrap.readInt()) : null)
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoE) : null)
                .setFormation(wrap.readUnsigned())
                .setHeroAtrifacts(wrap.readBoolean() ? parseHeroArtifacts(wrap, isRoE, isSoD) : null)
                .setPatrolRadius(wrap.readUnsigned())
                .setBiography(!isRoE && wrap.readBoolean() ? wrap.readString() : null)
                .setGender(!isRoE ? wrap.readUnsigned() : null)
                .setSodSpells(isSoD && wrap.readBoolean() ? wrap.readUnsigned(9) : null)
                .setAbSpell(!isSoD && !isRoE ? wrap.readUnsigned() : null)
                .setPrimarySkills(isSoD && wrap.readBoolean() ? readPrimarySkills(wrap) : null)
                .setUnknown2(wrap.readUnsigned(16));
        return result;
    }

    private int readArtifact(ByteWrapper wrap, boolean isRoE) {
        return isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort();
    }

    private HeroArtifacts parseHeroArtifacts(ByteWrapper wrap, boolean isRoE, boolean isSoD) {
        return new HeroArtifacts().setHeadwear(readArtifact(wrap, isRoE)) // TODO somewhere is bug, feet wrongly recognized
                .setShoulders(readArtifact(wrap, isRoE))
                .setRightHand(readArtifact(wrap, isRoE))
                .setLeftHand(readArtifact(wrap, isRoE))
                .setTorso(readArtifact(wrap, isRoE))
                .setRightRing(readArtifact(wrap, isRoE))
                .setLeftRing(readArtifact(wrap, isRoE))
                .setFeet(readArtifact(wrap, isRoE))
                .setMisc1(readArtifact(wrap, isRoE))
                .setMisc2(readArtifact(wrap, isRoE))
                .setMisc3(readArtifact(wrap, isRoE))
                .setMisc4(readArtifact(wrap, isRoE))
                .setDevice1(readArtifact(wrap, isRoE))
                .setDevice2(readArtifact(wrap, isRoE))
                .setDevice3(readArtifact(wrap, isRoE))
                .setDevice4(readArtifact(wrap, isRoE))
                .setUnknown(isSoD ? wrap.readUnsignedShort() : null)
                .setSpellbook(readArtifact(wrap, isRoE))
                .setMisc5(readArtifact(wrap, isRoE))
                .setBackpack(isRoE ? wrap.readUnsigned(wrap.readUnsignedShort()) : wrap.readUnsignedShort(wrap.readUnsignedShort()));
    }

    private TownEvent[] readTownEvents(ByteWrapper wrap, boolean isSoD) {
        TownEvent[] result = new TownEvent[wrap.readInt()];
        for (int i = 0; i < result.length; i++) {
            result[i] = new TownEvent().setName(wrap.readString())
                    .setMessage(wrap.readString())
                    .setResources(readResources(wrap))
                    .setAppliesToPlayers(wrap.readUnsigned())
                    .setAppliesToHuman(isSoD ? wrap.readUnsigned() : null)
                    .setApplicesToComputer(wrap.readUnsigned())
                    .setFirstOccurence(wrap.readUnsignedShort())
                    .setSubsequentOccurences(wrap.readUnsigned())
                    .setUnknown1(wrap.readUnsigned(17))
                    .setBuildings(wrap.readUnsigned(6))
                    .setCreatureQuantities(wrap.readUnsignedShort(7))
                    .setUnknown2(wrap.readUnsigned(4));
        }
        return result;
    }

    private MapTown readTown(ByteWrapper wrap, boolean isRoe, boolean isSoD) {
        MapTown town = new MapTown().setAbSodId(isRoe ? null : wrap.readInt())
                .setOwner(wrap.readUnsigned())
                .setName(wrap.readBoolean() ? wrap.readString() : null)
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoe) : null)
                .setFormation(wrap.readUnsigned());
        if (wrap.readBoolean()) {
            town.setBuilt(wrap.readUnsigned(6))
                    .setDisabled(wrap.readUnsigned(6));
        } else {
            town.setHasFort(wrap.readBoolean());
        }
        town.setMustHaveSpells(isRoe ? null : wrap.readUnsigned(9))
                .setMayHaveSpells(wrap.readUnsigned(9))
                .setTownEvents(readTownEvents(wrap, isSoD))
                .setAlignment(isSoD ? wrap.readUnsigned() : null)
                .setUnknown1(wrap.readUnsigned(3));
        return town;
    }

    private CommonReward readCommonReward(ByteWrapper wrap, boolean isRoE) {
        return new CommonReward().setExperience(wrap.readInt())
                .setSpellPoints(wrap.readInt())
                .setMorale(wrap.readByte())
                .setLuck(wrap.readByte())
                .setResources(readResources(wrap))
                .setSkills(readPrimarySkills(wrap))
                .setSecondarySkills(readSecondarySkills(wrap, wrap.readUnsigned()))
                .setArtifacts(readArtifacts(wrap, isRoE))
                .setSpells(Arrays.stream(wrap.readUnsigned(wrap.readUnsigned())).mapToObj(i -> ObjectNumberConstants.SPELLS[i]).toArray(String[]::new))
                .setCreatures(readArmy(wrap, isRoE, wrap.readUnsigned()))
                .setUnknown(wrap.readUnsigned(8));
    }

    private CommonGuardian readCommonGuardian(ByteWrapper wrap, boolean isRoE) {
        return new CommonGuardian().setMessage(wrap.readString())
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoE) : null)
                .setUnknown1(wrap.readUnsigned(4));
    }

    private CommonSecondarySkill[] readSecondarySkills(ByteWrapper wrap, int size) {
        CommonSecondarySkill[] result = new CommonSecondarySkill[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = new CommonSecondarySkill().setType(wrap.readUnsigned()).setLevel(wrap.readUnsigned());
        }
        return result;
    }

    private CreatureSlot readCreature(ByteWrapper wrap, boolean isRoE) {
        return new CreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                .setQuantity(wrap.readUnsignedShort());
    }

    private CreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE, int count) {
        CreatureSlot[] creatures = new CreatureSlot[count];
        for (int j = 0; j < creatures.length; j++) {
            creatures[j] = readCreature(wrap, isRoE);
        }
        return creatures;
    }

    private CreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE) {
        return readArmy(wrap, isRoE, 7);
    }

    private int[] readResources(ByteWrapper wrap) {
        return wrap.readInt(7);
    }

    private int[] readArtifacts(ByteWrapper wrap, boolean isRoE) {
        if (isRoE) {
            return wrap.readUnsigned(wrap.readUnsigned());
        } else {
            return wrap.readUnsignedShort(wrap.readUnsigned());
        }
    }

    private PrimarySkills readPrimarySkills(ByteWrapper wrap) {
        return new PrimarySkills().setAttack(wrap.readUnsigned())
                .setDefense(wrap.readUnsigned())
                .setSpellPower(wrap.readUnsigned())
                .setKnowledge(wrap.readUnsigned());
    }
}
