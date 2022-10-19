package ua.ihromant.sod.utils;

import ua.ihromant.sod.utils.bytes.ByteWrapper;
import ua.ihromant.sod.utils.entities.H3MHero;
import ua.ihromant.sod.utils.entities.H3MReward;
import ua.ihromant.sod.utils.entities.H3MSecondarySkill;
import ua.ihromant.sod.utils.entities.Coordinate;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;
import ua.ihromant.sod.utils.entities.H3MHeroArtifacts;
import ua.ihromant.sod.utils.entities.H3MMapMonster;
import ua.ihromant.sod.utils.entities.MapTile;
import ua.ihromant.sod.utils.entities.H3MMessageAndTreasure;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;
import ua.ihromant.sod.utils.entities.H3MPlayer;
import ua.ihromant.sod.utils.entities.H3MPrimarySkills;
import ua.ihromant.sod.utils.entities.H3MStartingTown;
import ua.ihromant.sod.utils.entities.H3MMapTown;
import ua.ihromant.sod.utils.entities.H3MTownEvent;
import ua.ihromant.sod.utils.map.H3MObjectGroup;
import ua.ihromant.sod.utils.map.RiverType;
import ua.ihromant.sod.utils.map.RoadType;

import java.nio.BufferUnderflowException;
import java.util.BitSet;

public class H3MReader {
    private static final int H3M_FORMAT_ROE = 0x0000000E;
    private static final int H3M_FORMAT_AB = 0x00000015;
    private static final int H3M_FORMAT_SOD = 0x0000001C;
    private static final int H3M_FORMAT_CHR = 0x0000001D;
    private static final int H3M_FORMAT_WOG = 0x00000033;

    public void parse(ByteWrapper wrap, ParserInterceptor interceptor) {
        int format = wrap.readInt();
        boolean isROE = format == H3M_FORMAT_ROE;
        boolean isSoD = format == H3M_FORMAT_SOD;
        wrap.readBoolean(); // at least 1 hero ?? wtf
        int side = wrap.readInt();
        Coordinate size = new Coordinate(side, side, wrap.readBoolean() ? 2 : 1);
        interceptor.interceptBasics(size, wrap.readString(), wrap.readString());
        wrap.readUnsigned(); // map difficulty
        if (!isROE) {
            wrap.readUnsigned(); // masteryLevelCap
        }
        for (int i = 0; i < 8; i++) {
            boolean ai;
            H3MPlayer player = new H3MPlayer().setCanBeHuman(wrap.readBoolean())
                    .setCanBeComputer(wrap.readBoolean())
                    .setBehavior(wrap.readUnsigned())
                    .setAllowedAlignments(isSoD ? wrap.readUnsigned() : null)
                    .setTownTypes(isROE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                    .setOwnsRandomTown(wrap.readBoolean());
            boolean hasMainTown = wrap.readBoolean();
            player.setStartingTown(hasMainTown ? new H3MStartingTown().setStartingTownCreateHero(isROE || wrap.readBoolean())
                    .setStartingTownType(isROE ? null : wrap.readUnsigned())
                    .setCoordinate(readCoordinate(wrap)) : null);
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
                if (!isROE || ai) {
                    player.setStartingHeroIsRandom(startingHeroIsRandom)
                            .setStartingHeroType(startingHeroType)
                            .setStartingHeroFace(wrap.readUnsigned())
                            .setStartingHeroName(wrap.readString());
                }
            }
            if (!isROE && ai) {
                wrap.readUnsigned(); // unknown1
                int heroesCount = wrap.readInt();
                for (int j = 0 ; j < heroesCount; j++) {
                    wrap.readUnsigned(); // type
                    wrap.readString(); // name
                }
            }
            if (player.isCanBeComputer() || player.isCanBeHuman()) {
                interceptor.interceptKingdomInfo(i, player);
            }
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
                new H3MHero().setType(wrap.readUnsigned())
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
                int[] availableSkills = wrap.readUnsigned(4);
            }
        }
        int rumorsCount = wrap.readInt();
        for (int i = 0; i < rumorsCount; i++) {
            wrap.readString(); // ai rumor name
            wrap.readString(); // ai rumor description
        }
        for (int i = 0; i < (isSoD ? 156 : 0); i++) {
            if (!wrap.readBoolean()) {
                continue;
            }
            H3MHero hero = new H3MHero();
            if (wrap.readBoolean()) {
                hero.setExperience(wrap.readInt());
            }
            if (wrap.readBoolean()) {
                hero.setSecondarySkills(readSecondarySkills(wrap, wrap.readInt()));
            }
            if (wrap.readBoolean()) {
                hero.setHeroAtrifacts(parseHeroArtifacts(wrap, isROE, isSoD));
            }
            if (wrap.readBoolean()) {
                hero.setBiography(wrap.readString());
            }
            hero.setGender(wrap.readUnsigned());
            if (wrap.readBoolean()) {
                hero.setSodSpells(wrap.readUnsigned(9));
            }
            if (wrap.readBoolean()) {
                hero.setPrimarySkills(readPrimarySkills(wrap));
            }
        }
        for (int z = 0; z < size.getZ(); z++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int x = 0; x < size.getX(); x++) {
                    interceptor.interceptTile(new Coordinate(x, y, z),
                            new MapTile().setTerrainType(wrap.readUnsigned())
                                    .setTerrainSprite(wrap.readUnsigned())
                                    .setRiverType(RiverType.values()[wrap.readUnsigned()])
                                    .setRiverSprite(wrap.readUnsigned())
                                    .setRoadType(RoadType.values()[wrap.readUnsigned()])
                                    .setRoadSprite(wrap.readUnsigned())
                                    .setMirroring(wrap.readUnsigned()));
                }
            }
        }
        H3MObjectAttribute[] objectAttributes = new H3MObjectAttribute[wrap.readInt()];
        for (int i = 0; i < objectAttributes.length; i++) {
            objectAttributes[i] = new H3MObjectAttribute().setDef(wrap.readString())
                    .setPassable(wrap.readUnsigned(6))
                    .setActive(wrap.readUnsigned(6))
                    .setAllowedLandsapes(wrap.readUnsignedShort())
                    .setLandscapeGroup(wrap.readUnsignedShort())
                    .setObjectClass(wrap.readInt())
                    .setObjectNumber(wrap.readInt())
                    .setObjectGroup(H3MObjectGroup.values()[wrap.readUnsigned()])
                    .setAbove(wrap.readUnsigned())
                    .setUnknown(wrap.readUnsigned(16));
        }
        int dataLength = wrap.readInt();
        for (int i = 0; i < dataLength; i++) {
            Coordinate coords = readCoordinate(wrap);
            H3MObjectAttribute attribute = objectAttributes[wrap.readInt()];
            wrap.readUnsigned(5); // unknown1
            H3MObjectType type = attribute.type();
            switch (type) {
                case META_OBJECT_PLACEHOLDER_HERO:
                    wrap.readUnsigned(); // placeholder hero owner
                    if (wrap.readUnsigned() == 0xFF) { // placeholder hero type
                        wrap.readUnsigned(); // placeholder hero power rating
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
                    wrap.readString(); // message text
                    wrap.readUnsigned(4); // unknown1
                    break;
                case META_OBJECT_GARRISON:
                case META_OBJECT_GARRISON_ABSOD:
                    wrap.readInt(); // garrison owner
                    readArmy(wrap, isROE); // garrison creatures
                    int remUnits = isROE ? 0 : wrap.readUnsigned(); // removable units
                    wrap.readUnsigned(8); // unknown1
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
                case META_OBJECT_GENERIC_IMPASSABLE_TERRAIN:
                case META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD:
                    interceptor.interceptImpassable(coords, attribute);
                    break; // Generic objects have no body
                case META_OBJECT_GENERIC_BOAT:
                case META_OBJECT_GENERIC_PASSABLE_TERRAIN:
                case META_OBJECT_GENERIC_PASSABLE_TERRAIN_SOD:
                case META_OBJECT_GENERIC_VISITABLE:
                case META_OBJECT_GENERIC_VISITABLE_ABSOD:
                case META_OBJECT_GENERIC_TREASURE:
                case META_OBJECT_MONOLITH_TWO_WAY:
                case META_OBJECT_SUBTERRANEAN_GATE:
                    break; // Generic objects have no body
                case META_OBJECT_TOWN:
                case META_OBJECT_TOWN_ABSOD:
                    interceptor.interceptTown(coords, attribute, readTown(wrap, isROE, isSoD));
                    break;
                case META_OBJECT_RANDOM_DWELLING_ABSOD:
                case META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD:
                case META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD:
                    wrap.readInt(); // owner
                    if (type != H3MObjectType.META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD) {
                        int abSodId = wrap.readInt();
                        if (abSodId == 0) {
                            wrap.readUnsignedShort(); // alignment
                        }
                    }
                    if (type != H3MObjectType.META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD) {
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
                    H3MCreatureSlot[] grd = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null; // only optional guardian here
                    break;
                case META_OBJECT_SHRINE:
                    wrap.readInt(); // spell
                    break;
                case META_OBJECT_SPELL_SCROLL:
                    H3MCreatureSlot[] grd1 = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null;
                    wrap.readInt(); // spell
                    break;
                case META_OBJECT_RESOURCE:
                    H3MCreatureSlot[] grd2 = wrap.readBoolean() ? readCommonGuardian(wrap, isROE) : null;
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
            return; // normal
        }
        wrap.readUnsigned(115);
        try {
            wrap.readUnsigned();
        } catch (BufferUnderflowException e) {
            return; // normal
        }
        throw new IllegalStateException();
    }

    private Coordinate readCoordinate(ByteWrapper wrap) {
        return new Coordinate(wrap.readUnsigned(), wrap.readUnsigned(), wrap.readUnsigned());
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
                readCoordinate(wrap);
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
                new H3MCreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                        .setQuantity(wrap.readInt()); // can't use readCreature
                return;
            case 2: // accumulate resources
                wrap.readUnsigned(); // 0 - wood, 1 - mercury, 2 - ore, 3 - sulfur, 4 - crystal, 5 - gems, 6 - gold
                wrap.readInt(); // amount
                return;
            case 8: // flag dwellings
            case 9: // flag mines
                return;
        }
        switch (winCond) {
            case 3: // upgrade town
                readCoordinate(wrap);
                wrap.readUnsigned(); // 0 - town, 1 - city, 2 - capitol
                wrap.readUnsigned(); // 0 - fort, 1 - citadel, 2 - castle
                break;
            case 4: // build grail
            case 5: // defeat hero
            case 6: // capture town
            case 7: // defeat monster
                readCoordinate(wrap);
                break;
            case 10: // transport artifact
                wrap.readUnsigned(); // type
                readCoordinate(wrap);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private H3MReward parseReward(ByteWrapper wrap, boolean isRoE) {
        int rewardType = wrap.readUnsigned();
        switch (rewardType) {
            case 0: // none
                return new H3MReward();
            case 1: // experience
                return new H3MReward().setExperience(wrap.readInt());
            case 2: // spell points
                return new H3MReward().setSpellPoints(wrap.readInt());
            case 3: // morale
                return new H3MReward().setMorale(wrap.readUnsigned());
            case 4: // luck
                return new H3MReward().setLuck(wrap.readUnsigned());
            case 5: // resource
                int[] resources = new int[7];
                resources[wrap.readUnsigned()] = wrap.readInt();
                return new H3MReward().setResources(resources);
            case 6: // primary skill
                H3MPrimarySkills skills = new H3MPrimarySkills();
                switch (wrap.readUnsigned()) {
                    case 0:
                        skills.setAttack(wrap.readUnsigned());
                        break;
                    case 1:
                        skills.setDefense(wrap.readUnsigned());
                        break;
                    case 2:
                        skills.setSpellPower(wrap.readUnsigned());
                        break;
                    case 3:
                        skills.setKnowledge(wrap.readUnsigned());
                        break;
                    default: // TODO assumption, remove after check
                        throw new IllegalArgumentException();
                }
                return new H3MReward().setSkills(skills);
            case 7: // secondary skill
                return new H3MReward().setSecondarySkills(new H3MSecondarySkill[]{readSecondarySkill(wrap)});
            case 8: // artifact
                return new H3MReward().setArtifacts(new int[]{readArtifact(wrap, isRoE)});
            case 9: // spell
                return new H3MReward().setSpells(new int[]{wrap.readUnsigned()});
            case 10:
                return new H3MReward().setCreatures(new H3MCreatureSlot[]{readCreature(wrap, isRoE)});
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

    private H3MMapMonster parseMonster(ByteWrapper wrap, boolean isRoE) {
        return new H3MMapMonster().setAbSodId(isRoE ? null : wrap.readInt())
                .setQuantity(wrap.readUnsignedShort())
                .setDisposition(wrap.readUnsigned())
                .setMessTreasure(wrap.readBoolean() ? new H3MMessageAndTreasure().setMessage(wrap.readString())
                        .setResources(readResources(wrap))
                        .setArtifact(readArtifact(wrap, isRoE)) : null)
                .setNeverFlees(wrap.readUnsigned())
                .setDoesNotGrow(wrap.readUnsigned())
                .setUnknown1(wrap.readUnsigned(2));
    }

    private H3MHero parseHero(ByteWrapper wrap, boolean isRoE, boolean isSoD) {
        H3MHero result = new H3MHero().setAbSodId(isRoE ? null : wrap.readInt())
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

    private H3MHeroArtifacts parseHeroArtifacts(ByteWrapper wrap, boolean isRoE, boolean isSoD) {
        return new H3MHeroArtifacts().setHeadwear(readArtifact(wrap, isRoE))
                .setCape(readArtifact(wrap, isRoE))
                .setNecklace(readArtifact(wrap, isRoE))
                .setRightHand(readArtifact(wrap, isRoE))
                .setLeftHand(readArtifact(wrap, isRoE))
                .setTorso(readArtifact(wrap, isRoE))
                .setRightRing(readArtifact(wrap, isRoE))
                .setLeftRing(readArtifact(wrap, isRoE))
                .setFeet(readArtifact(wrap, isRoE))
                .setMisc0(readArtifact(wrap, isRoE))
                .setMisc1(readArtifact(wrap, isRoE))
                .setMisc2(readArtifact(wrap, isRoE))
                .setMisc3(readArtifact(wrap, isRoE))
                .setBallista(readArtifact(wrap, isRoE))
                .setAmmoCart(readArtifact(wrap, isRoE))
                .setFirstAidTent(readArtifact(wrap, isRoE))
                .setCatapult(readArtifact(wrap, isRoE))
                .setSpellbook(readArtifact(wrap, isRoE))
                .setMisc4(isSoD ? wrap.readUnsignedShort() : null)
                .setBackpack(isRoE ? wrap.readUnsigned(wrap.readUnsignedShort()) : wrap.readUnsignedShort(wrap.readUnsignedShort()));
    }

    private H3MTownEvent[] readTownEvents(ByteWrapper wrap, boolean isSoD) {
        H3MTownEvent[] result = new H3MTownEvent[wrap.readInt()];
        for (int i = 0; i < result.length; i++) {
            result[i] = new H3MTownEvent().setName(wrap.readString())
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

    private H3MMapTown readTown(ByteWrapper wrap, boolean isRoe, boolean isSoD) {
        H3MMapTown town = new H3MMapTown().setAbSodId(isRoe ? null : wrap.readInt())
                .setOwner(wrap.readUnsigned())
                .setName(wrap.readBoolean() ? wrap.readString() : null)
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoe) : null)
                .setFormation(wrap.readUnsigned());
        if (wrap.readBoolean()) {
            town.setBuilt(BitSet.valueOf(wrap.readBytes(6)))
                    .setDisabled(BitSet.valueOf(wrap.readBytes(6)));
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

    private static String binary(int i) {
        return Integer.toString(i, 2);
    }

    private H3MReward readCommonReward(ByteWrapper wrap, boolean isRoE) {
        return new H3MReward().setExperience(wrap.readInt())
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

    private H3MCreatureSlot[] readCommonGuardian(ByteWrapper wrap, boolean isRoE) {
        wrap.readString(); // message
        H3MCreatureSlot[] creatures = wrap.readBoolean() ? readArmy(wrap, isRoE) : null;
        wrap.readUnsigned(4); // unknown1
        return creatures;
    }

    private H3MSecondarySkill[] readSecondarySkills(ByteWrapper wrap, int size) {
        H3MSecondarySkill[] result = new H3MSecondarySkill[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = readSecondarySkill(wrap);
        }
        return result;
    }

    private H3MSecondarySkill readSecondarySkill(ByteWrapper wrap) {
        return new H3MSecondarySkill().setType(wrap.readUnsigned()).setLevel(wrap.readUnsigned());
    }

    private H3MCreatureSlot readCreature(ByteWrapper wrap, boolean isRoE) {
        return new H3MCreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                .setQuantity(wrap.readUnsignedShort());
    }

    private H3MCreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE, int count) {
        H3MCreatureSlot[] creatures = new H3MCreatureSlot[count];
        for (int j = 0; j < creatures.length; j++) {
            creatures[j] = readCreature(wrap, isRoE);
        }
        return creatures;
    }

    private H3MCreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE) {
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

    private H3MPrimarySkills readPrimarySkills(ByteWrapper wrap) {
        return new H3MPrimarySkills().setAttack(wrap.readUnsigned())
                .setDefense(wrap.readUnsigned())
                .setSpellPower(wrap.readUnsigned())
                .setKnowledge(wrap.readUnsigned());
    }
}
