package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.BackgroundType;
import ua.ihromant.sod.ByteWrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

import static ua.ihromant.sod.h3m.ObjectNumberConstants.*;
import static ua.ihromant.sod.h3m.ObjectType.*;

public class H3MParser {
    private static final int H3M_FORMAT_ROE = 0x0000000E;
    private static final int H3M_FORMAT_AB = 0x00000015;
    private static final int H3M_FORMAT_SOD = 0x0000001C;
    private static final int H3M_FORMAT_CHR = 0x0000001D;
    private static final int H3M_FORMAT_WOG = 0x00000033;

    @Test
    public void parse() throws IOException {
        MapMetadata map = new MapMetadata();
        byte[] bytes = IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/Metataxer.h3m"))));
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
                    .setTownTypes(wrap.readUnsigned())
                    .setTownConflux(isROE ? null : wrap.readUnsigned())
                    .setOwnsRandomTown(wrap.readBoolean());
            boolean hasMainTown = wrap.readBoolean();
            player.setStartingTown(hasMainTown ? new StartingTownMetadata().setStartingTownCreateHero(isROE ? null : wrap.readBoolean())
                    .setStartingTownType(isROE ? null : wrap.readUnsigned())
                    .setCoordinates(readCoordinates(wrap)) : null);
            boolean startingHeroIsRandom = wrap.readBoolean();
            int startingHeroType = wrap.readUnsigned();
            if (!hasMainTown) {
                if (startingHeroType == 0xFF) {
                    if (player.getTownTypes() != 0) {
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
            if (ai) {
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
        int[] availableHeroes = wrap.readUnsigned(format == H3M_FORMAT_ROE ? 16 : 20);
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
            int[] avaliableArtifacts = wrap.readUnsigned(format == H3M_FORMAT_AB ? 17 : 18);
            if (isSoD) {
                int[] availableSpells = wrap.readUnsigned(9);
                int[] availableSkills = wrap.readUnsigned(4);
            }
        }
        int rumorsCount = wrap.readInt();
        for (int i = 0; i < rumorsCount; i++) {
            new AiRumor().setName(wrap.readString())
                    .setDesc(wrap.readString());
        }
        for (int i = 0; i < 156; i++) {
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
                MapTile[] column = level[y];
                for (int x = 0; x < map.getBasic().getMapSize(); x++) {
                    column[x] = new MapTile().setTerrainType(BackgroundType.values()[wrap.readUnsigned()])
                            .setTerrainSprite(wrap.readUnsigned())
                            .setRiverType(wrap.readUnsigned())
                            .setRiverSprite(wrap.readUnsigned())
                            .setRoadType(wrap.readUnsigned())
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
                    .setObjectGroup(wrap.readUnsigned())
                    .setAbove(wrap.readUnsigned())
                    .setUnknown(wrap.readUnsigned(16));
            map.getObjectAttributes()[i].setType(objectNumberToType(map.getObjectAttributes()[i].getObjectClass()));
        }
        map.setObjectData(new ObjectData[wrap.readInt()]);
        for (int i = 0; i < map.getObjectData().length; i++) {
            map.getObjectData()[i] = new ObjectData().setX(wrap.readUnsigned())
                    .setY(wrap.readUnsigned())
                    .setZ(wrap.readUnsigned())
                    .setOaIndex(wrap.readInt())
                    .setUnknown1(wrap.readUnsigned(5));
            ObjectType type = map.getObjectAttributes()[map.getObjectData()[i].getOaIndex()].getType();
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
                    readCommonReward(wrap, isROE);
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
                    int[] potentialSkills = isROE ? null : wrap.readUnsigned(4);
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
        byte[] bytez = wrap.readBytes(100);
        System.out.println();
        if (format != H3M_FORMAT_SOD) {
            throw new IllegalArgumentException("Not supported because not SoD"); // TODO for now
        }
    }

    private Coordinates readCoordinates(ByteWrapper wrap) throws IOException {
        return new Coordinates().setX(wrap.readUnsigned())
                .setY(wrap.readUnsigned())
                .setZ(wrap.readUnsigned());
    }

    private void parseAiTeams(ByteWrapper wrap) throws IOException {
        int teamsCount = wrap.readUnsigned(); // teamsCount
        if (teamsCount != 0) {
            wrap.readUnsigned(8);
        }
    }

    private void parseLoseCondition(ByteWrapper wrap) throws IOException {
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

    private void parseWinCondition(ByteWrapper wrap, boolean isRoE) throws IOException {
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
                readCreature(wrap, isRoE);
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

    private void readResource(ByteWrapper wrap) throws IOException {
        wrap.readUnsigned(); // 0 - wood, 1 - mercury, 2 - ore, 3 - sulfur, 4 - crystal, 5 - gems, 6 - gold
        wrap.readInt(); // amount
    }

    private void parseReward(ByteWrapper wrap, boolean isRoE) throws IOException { // TODO merge with common reward
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

    private void parseQuestRequest(ByteWrapper wrap, boolean isROE, int questType) throws IOException {
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

    private MapMonster parseMonster(ByteWrapper wrap, boolean isRoE) throws IOException {
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

    private CommonHero parseHero(ByteWrapper wrap, boolean isRoE, boolean isSoD) throws IOException {
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

    private int readArtifact(ByteWrapper wrap, boolean isRoE) throws IOException {
        return isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort();
    }

    private HeroArtifacts parseHeroArtifacts(ByteWrapper wrap, boolean isRoE, boolean isSoD) throws IOException {
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

    private TownEvent[] readTownEvents(ByteWrapper wrap, boolean isSoD) throws IOException {
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

    private Town readTown(ByteWrapper wrap, boolean isRoe, boolean isSoD) throws IOException {
        Town town = new Town().setAbSodId(isRoe ? null : wrap.readInt())
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

    private CommonReward readCommonReward(ByteWrapper wrap, boolean isRoE) throws IOException {
        return new CommonReward().setExperience(wrap.readInt())
                .setSpellPoints(wrap.readInt())
                .setMorale(wrap.readByte())
                .setLuck(wrap.readByte())
                .setResources(readResources(wrap))
                .setSkills(readPrimarySkills(wrap))
                .setSecondarySkills(readSecondarySkills(wrap, wrap.readUnsigned()))
                .setArtifacts(readArtifacts(wrap, isRoE))
                .setSpells(wrap.readUnsigned(wrap.readUnsigned()))
                .setCreatures(readArmy(wrap, isRoE, wrap.readUnsigned()))
                .setUnknown(wrap.readUnsigned(8));
    }

    private CommonGuardian readCommonGuardian(ByteWrapper wrap, boolean isRoE) throws IOException {
        return new CommonGuardian().setMessage(wrap.readString())
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoE) : null)
                .setUnknown1(wrap.readUnsigned(4));
    }

    private CommonSecondarySkill[] readSecondarySkills(ByteWrapper wrap, int size) throws IOException {
        CommonSecondarySkill[] result = new CommonSecondarySkill[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = new CommonSecondarySkill().setType(wrap.readUnsigned()).setLevel(wrap.readUnsigned());
        }
        return result;
    }

    private CreatureSlot readCreature(ByteWrapper wrap, boolean isRoE) throws IOException {
        return new CreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                .setQuantity(wrap.readUnsignedShort());
    }

    private CreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE, int count) throws IOException {
        CreatureSlot[] creatures = new CreatureSlot[count];
        for (int j = 0; j < creatures.length; j++) {
            creatures[j] = readCreature(wrap, isRoE);
        }
        return creatures;
    }

    private CreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE) throws IOException {
        return readArmy(wrap, isRoE, 7);
    }

    private int[] readResources(ByteWrapper wrap) throws IOException {
        return wrap.readInt(7);
    }

    private int[] readArtifacts(ByteWrapper wrap, boolean isRoE) throws IOException {
        if (isRoE) {
            return wrap.readUnsigned(wrap.readUnsigned());
        } else {
            return wrap.readUnsignedShort(wrap.readUnsigned());
        }
    }

    private PrimarySkills readPrimarySkills(ByteWrapper wrap) throws IOException {
        return new PrimarySkills().setAttack(wrap.readUnsigned())
                .setDefense(wrap.readUnsigned())
                .setSpellPower(wrap.readUnsigned())
                .setKnowledge(wrap.readUnsigned());
    }

    private static ObjectType objectNumberToType(int objectClass) {
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

    private int[] debug(ByteWrapper wrap) throws IOException {
        return wrap.readUnsigned(500);
    }
}
