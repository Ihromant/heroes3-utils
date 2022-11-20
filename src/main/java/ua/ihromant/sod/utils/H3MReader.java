package ua.ihromant.sod.utils;

import ua.ihromant.sod.utils.bytes.ByteWrapper;
import ua.ihromant.sod.utils.entities.Behavior;
import ua.ihromant.sod.utils.entities.Control;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;
import ua.ihromant.sod.utils.entities.H3MHeader;
import ua.ihromant.sod.utils.entities.H3MHeroArtifacts;
import ua.ihromant.sod.utils.entities.H3MLoseType;
import ua.ihromant.sod.utils.entities.H3MMap;
import ua.ihromant.sod.utils.entities.H3MMessageAndTreasure;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;
import ua.ihromant.sod.utils.entities.H3MObjectGroup;
import ua.ihromant.sod.utils.entities.H3MObjectType;
import ua.ihromant.sod.utils.entities.H3MPlayer;
import ua.ihromant.sod.utils.entities.H3MReward;
import ua.ihromant.sod.utils.entities.H3MRumor;
import ua.ihromant.sod.utils.entities.H3MSecondarySkill;
import ua.ihromant.sod.utils.entities.H3MStartingTown;
import ua.ihromant.sod.utils.entities.H3MTimeEvent;
import ua.ihromant.sod.utils.entities.H3MTownEvent;
import ua.ihromant.sod.utils.entities.H3MWinType;
import ua.ihromant.sod.utils.entities.MapTile;
import ua.ihromant.sod.utils.entities.PrimarySkills;
import ua.ihromant.sod.utils.entities.conditions.AccumulateCreatureCondition;
import ua.ihromant.sod.utils.entities.conditions.AccumulateResourceCondition;
import ua.ihromant.sod.utils.entities.conditions.ArtifactCondition;
import ua.ihromant.sod.utils.entities.conditions.BaseLoseCondition;
import ua.ihromant.sod.utils.entities.conditions.BaseWinCondition;
import ua.ihromant.sod.utils.entities.conditions.CoordinateCondition;
import ua.ihromant.sod.utils.entities.conditions.LoseCoordinateCondition;
import ua.ihromant.sod.utils.entities.conditions.TimeElapsedCondition;
import ua.ihromant.sod.utils.entities.conditions.TransportArtifactCondition;
import ua.ihromant.sod.utils.entities.conditions.UpgradeTownCondition;
import ua.ihromant.sod.utils.entities.objects.H3MArtifact;
import ua.ihromant.sod.utils.entities.objects.H3MBaseObject;
import ua.ihromant.sod.utils.entities.objects.H3MCommonGuardian;
import ua.ihromant.sod.utils.entities.objects.H3MGarrison;
import ua.ihromant.sod.utils.entities.objects.H3MGrail;
import ua.ihromant.sod.utils.entities.objects.H3MHero;
import ua.ihromant.sod.utils.entities.objects.H3MMapEvent;
import ua.ihromant.sod.utils.entities.objects.H3MMapMonster;
import ua.ihromant.sod.utils.entities.objects.H3MMapTown;
import ua.ihromant.sod.utils.entities.objects.H3MMessage;
import ua.ihromant.sod.utils.entities.objects.H3MOwnedObject;
import ua.ihromant.sod.utils.entities.objects.H3MPandoraBox;
import ua.ihromant.sod.utils.entities.objects.H3MQuestGuard;
import ua.ihromant.sod.utils.entities.objects.H3MRandomDwelling;
import ua.ihromant.sod.utils.entities.objects.H3MResource;
import ua.ihromant.sod.utils.entities.objects.H3MScholar;
import ua.ihromant.sod.utils.entities.objects.H3MSeerHut;
import ua.ihromant.sod.utils.entities.objects.H3MShrine;
import ua.ihromant.sod.utils.entities.objects.H3MSpellScroll;
import ua.ihromant.sod.utils.entities.objects.H3MWitchHut;
import ua.ihromant.sod.utils.entities.quest.H3MArtifactsQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MBaseQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MCreaturesQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MDefeatMonsterQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MExperienceQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MHeroQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MPlayerQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MPrimarySkillsQuestRequest;
import ua.ihromant.sod.utils.entities.quest.H3MQuestType;
import ua.ihromant.sod.utils.entities.quest.H3MResourcesQuestRequest;
import ua.ihromant.sod.utils.map.RiverType;
import ua.ihromant.sod.utils.map.RoadType;

import java.nio.BufferUnderflowException;
import java.util.BitSet;
import java.util.List;
import java.util.function.Function;

public class H3MReader {
    public H3MHeader parseHeader(ByteWrapper wrap) {
        H3MHeader result = new H3MHeader();
        result.setFormat(wrap.readInt());
        result.setAtLeastOneHero(wrap.readBoolean());
        result.setSize(wrap.readInt());
        result.setUnderground(wrap.readBoolean());
        result.setName(wrap.readString());
        result.setDescription(wrap.readString());
        result.setMapDifficulty(wrap.readUnsigned());
        result.setMasteryCapLevel(!result.isRoE() ? wrap.readUnsigned() : null);
        for (int i = 0; i < 8; i++) {
            boolean ai;
            H3MPlayer player = new H3MPlayer().setControl(Control.of(wrap.readBoolean(), wrap.readBoolean()))
                    .setBehavior(Behavior.values()[wrap.readUnsigned()])
                    .setAllowedAlignments(result.isSoD() ? wrap.readUnsigned() : 0)
                    .setTownTypes(BitSet.valueOf(wrap.readBytes(result.isRoE() ? 1 : 2)))
                    .setOwnsRandomTown(wrap.readBoolean());
            boolean hasMainTown = wrap.readBoolean();
            player.setStartingTown(hasMainTown ? new H3MStartingTown()
                    .setStartingTownCreateHero(result.isRoE() || wrap.readBoolean())
                    .setStartingTownType(result.isRoE() ? null : wrap.readUnsignedOpt())
                    .setCoordinate(readCoordinate(wrap)) : null);
            player.setStartingHeroIsRandom(wrap.readBoolean());
            Integer startingHeroType = wrap.readUnsignedOpt();
            if (!hasMainTown) {
                if (startingHeroType == null) {
                    if (result.isRoE() || !player.getTownTypes().isEmpty()) {
                        ai = true;
                    } else {
                        ai = false;
                        player.setStartingHeroType(startingHeroType)
                                .setStartingHeroFace(wrap.readUnsigned())
                                .setStartingHeroName(wrap.readString());
                    }
                } else {
                    ai = true;
                    player.setStartingHeroType(startingHeroType)
                            .setStartingHeroFace(wrap.readUnsigned())
                            .setStartingHeroName(wrap.readString());
                }
            } else {
                ai = startingHeroType != null;
                if (!result.isRoE() || ai) {
                    player.setStartingHeroType(startingHeroType)
                            .setStartingHeroFace(wrap.readUnsigned())
                            .setStartingHeroName(wrap.readString());
                }
            }
            if (!result.isRoE() && ai) {
                wrap.readUnsigned(); // unknown1
                int heroesCount = wrap.readInt();
                for (int j = 0 ; j < heroesCount; j++) {
                    wrap.readUnsigned(); // type
                    wrap.readString(); // name
                }
            }
            result.getH3mPlayers()[i] = player;
        }
        result.setWinCondition(parseWinCondition(wrap, result.isRoE()));
        result.setLoseCondition(parseLoseCondition(wrap));
        result.setTeamsCount(wrap.readUnsigned());
        result.setTeams(result.getTeamsCount() != 0 ? wrap.readBytes(8) : null);
        result.setAvailableHeroes(BitSet.valueOf(wrap.readBytes(result.isRoE() ? 16 : 20)));
        return result;
    }

    public H3MMap parse(ByteWrapper wrap) {
        H3MHeader header = parseHeader(wrap);
        H3MMap result = new H3MMap().setHeader(header);
        if (!header.isRoE()) {
            wrap.readInt(); // empty
        }
        if (header.isSoD()) {
            H3MHero[] customHeroes = new H3MHero[wrap.readUnsigned()];
            for (int i = 0; i < customHeroes.length; i++) {
                customHeroes[i] = new H3MHero().setHeroType(wrap.readUnsigned())
                        .setFace(wrap.readUnsigned())
                        .setName(wrap.readString())
                        .setAllowedPlayers(wrap.readUnsigned());
            }
            result.setCustomHeroes(customHeroes);
        }
        wrap.readUnsigned(31); // 31 empty bytes
        if (!header.isRoE()) {
            result.setAvailableArtifacts(BitSet.valueOf(wrap.readBytes(header.isSoD() ? 18 : 17)));
            if (header.isSoD()) {
                result.setAvailableSpells(BitSet.valueOf(wrap.readBytes(9)));
                result.setAvailableSkills(BitSet.valueOf(wrap.readBytes(4)));
            }
        }
        H3MRumor[] rumors = new H3MRumor[wrap.readInt()];
        for (int i = 0; i < rumors.length; i++) {
            rumors[i] = new H3MRumor().setName(wrap.readString()).setDescription(wrap.readString());
        }
        result.setRumors(rumors);
        H3MHero[] sodHeroes = new H3MHero[header.isSoD() ? 156 : 0];
        for (int i = 0; i < sodHeroes.length; i++) {
            if (!wrap.readBoolean()) {
                continue;
            }
            H3MHero hero = new H3MHero().setHeroType(i);
            if (wrap.readBoolean()) {
                hero.setExperience(wrap.readInt());
            }
            if (wrap.readBoolean()) {
                hero.setSecondarySkills(readSecondarySkills(wrap, wrap.readInt()));
            }
            if (wrap.readBoolean()) {
                hero.setHeroAtrifacts(parseHeroArtifacts(wrap, header));
            }
            if (wrap.readBoolean()) {
                hero.setBiography(wrap.readString());
            }
            hero.setGender(wrap.readUnsigned());
            if (wrap.readBoolean()) {
                hero.setSodSpells(BitSet.valueOf(wrap.readBytes(9)));
            }
            if (wrap.readBoolean()) {
                hero.setPrimarySkills(readPrimarySkills(wrap));
            }
            sodHeroes[i] = hero;
        }
        result.setSodHeroes(sodHeroes);
        MapTile[][][] tiles = new MapTile[header.getSize()][header.getSize()][header.isUnderground() ? 2 : 1];
        for (int z = 0; z < (header.isUnderground() ? 2 : 1); z++) {
            for (int y = 0; y < header.getSize(); y++) {
                for (int x = 0; x < header.getSize(); x++) {
                    tiles[x][y][z] = new MapTile().setTerrainType(wrap.readUnsigned())
                            .setTerrainSprite(wrap.readUnsigned())
                            .setRiverType(RiverType.values()[wrap.readUnsigned()])
                            .setRiverSprite(wrap.readUnsigned())
                            .setRoadType(RoadType.values()[wrap.readUnsigned()])
                            .setRoadSprite(wrap.readUnsigned())
                            .setMirroring(wrap.readUnsigned());
                }
            }
        }
        result.setTiles(tiles);
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
        result.setObjectAttributes(objectAttributes);
        int dataLength = wrap.readInt();
        for (int i = 0; i < dataLength; i++) {
            Coordinate coord = readCoordinate(wrap);
            H3MObjectAttribute attribute = objectAttributes[wrap.readInt()];
            wrap.readUnsigned(5); // unknown1
            H3MObjectType type = attribute.type();
            H3MBaseObject object = switch (type) {
                case META_OBJECT_PLACEHOLDER_HERO -> append(result, H3MMap::getPlaceholders, readPlaceholder(wrap));
                case META_OBJECT_QUEST_GUARD ->
                        append(result, H3MMap::getQuestGuards, new H3MQuestGuard().setRequest(parseQuestRequest(wrap, header.isRoE(), wrap.readUnsignedOpt())));
                case META_OBJECT_PANDORAS_BOX -> append(result, H3MMap::getPandoras, new H3MPandoraBox()
                        .setGuard(wrap.readBoolean() ? readCommonGuardian(wrap, header.isRoE()) : null)
                        .setReward(readCommonReward(wrap, header.isRoE())));
                case META_OBJECT_SIGN, META_OBJECT_OCEAN_BOTTLE ->
                        append(result, H3MMap::getMessages, readMessage(wrap));
                case META_OBJECT_GARRISON, META_OBJECT_GARRISON_ABSOD ->
                        append(result, H3MMap::getGarrisons, readGarrison(wrap, header.isRoE()));
                case META_OBJECT_EVENT -> append(result, H3MMap::getMapEvents, readEvent(wrap, header.isRoE()));
                case META_OBJECT_GRAIL -> append(result, H3MMap::getGrails, new H3MGrail().setRadius(wrap.readInt()));
                case META_OBJECT_DWELLING, META_OBJECT_DWELLING_ABSOD, META_OBJECT_LIGHTHOUSE, META_OBJECT_RESOURCE_GENERATOR, META_OBJECT_SHIPYARD, META_OBJECT_ABANDONED_MINE_ABSOD ->
                        append(result, H3MMap::getOwnedObjects, new H3MOwnedObject().setOwner(wrap.readInt()));
                case META_OBJECT_GENERIC_IMPASSABLE_TERRAIN, META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD, META_OBJECT_GENERIC_BOAT, META_OBJECT_GENERIC_PASSABLE_TERRAIN, META_OBJECT_GENERIC_PASSABLE_TERRAIN_SOD, META_OBJECT_GENERIC_VISITABLE, META_OBJECT_GENERIC_VISITABLE_ABSOD, META_OBJECT_GENERIC_TREASURE, META_OBJECT_MONOLITH_TWO_WAY, META_OBJECT_SUBTERRANEAN_GATE ->
                        append(result, H3MMap::getBaseObjects, new H3MBaseObject());
                case META_OBJECT_TOWN, META_OBJECT_TOWN_ABSOD ->
                        append(result, H3MMap::getTowns, readTown(wrap, header));
                case META_OBJECT_RANDOM_DWELLING_ABSOD, META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD, META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD ->
                        append(result, H3MMap::getRandomDwellings, readRandomDwelling(wrap, type));
                case META_OBJECT_HERO, META_OBJECT_HERO_AB, META_OBJECT_RANDOM_HERO, META_OBJECT_PRISON ->
                        append(result, H3MMap::getMapHeroes, parseHero(wrap, header));
                case META_OBJECT_MONSTER, META_OBJECT_MONSTER_ABSOD ->
                        append(result, H3MMap::getMapMonsters, parseMonster(wrap, header.isRoE()));
                case META_OBJECT_ARTIFACT, META_OBJECT_ARTIFACT_AB, META_OBJECT_ARTIFACT_SOD ->
                        append(result, H3MMap::getArtifacts, new H3MArtifact().setGuard(wrap.readBoolean() ? readCommonGuardian(wrap, header.isRoE()) : null));
                case META_OBJECT_SHRINE -> append(result, H3MMap::getShrines, new H3MShrine().setSpell(wrap.readInt()));
                case META_OBJECT_SPELL_SCROLL -> append(result, H3MMap::getScrolls, new H3MSpellScroll()
                        .setGuard(wrap.readBoolean() ? readCommonGuardian(wrap, header.isRoE()) : null)
                        .setSpell(wrap.readInt()));
                case META_OBJECT_RESOURCE -> append(result, H3MMap::getResources, readResource(wrap, header.isRoE()));
                case META_OBJECT_WITCH_HUT ->
                        append(result, H3MMap::getWitchHuts, new H3MWitchHut().setPotentialSkills(header.isRoE() ? null : BitSet.valueOf(wrap.readBytes(4))));
                case META_OBJECT_SEERS_HUT -> append(result, H3MMap::getSeerHuts, readSeerHut(wrap, header.isRoE()));
                case META_OBJECT_SCHOLAR -> append(result, H3MMap::getScholars, readScholar(wrap));
            };
            object.setType(type);
            object.setCoordinate(coord);
            object.setAttribute(attribute);
        }
        H3MTimeEvent[] timeEvents = new H3MTimeEvent[wrap.readInt()];
        for (int i = 0; i < timeEvents.length; i++) {
            H3MTimeEvent event = new H3MTimeEvent();
            event.setEventName(wrap.readString());
            event.setEventMessage(wrap.readString());
            event.setResources(wrap.readInt(7));
            event.setAppliesToPlayers(wrap.readUnsigned());
            event.setAppliesToHuman(header.isSoD() ? wrap.readUnsigned() : null);
            event.setAppliesToComputer(wrap.readUnsigned());
            event.setFirstOccurence(wrap.readUnsignedShort());
            event.setSubsequentOccurences(wrap.readUnsigned());
            wrap.readUnsigned(17); // unknown1
            timeEvents[i] = event;
        }
        result.setTimeEvents(timeEvents);
        wrap.readInt();
        wrap.readInt();
        try {
            wrap.readUnsigned(); // HD
        } catch (BufferUnderflowException e) {
            return result; // normal
        }
        wrap.readUnsigned(115);
        try {
            wrap.readUnsigned();
        } catch (BufferUnderflowException e) {
            return result; // normal
        }
        throw new IllegalStateException();
    }

    private static <T extends H3MBaseObject> T append(H3MMap map, Function<H3MMap, List<T>> getter, T object) {
        getter.apply(map).add(object);
        return object;
    }

    private static H3MScholar readScholar(ByteWrapper wrap) {
        H3MScholar result = new H3MScholar();
        result.setRewardType(wrap.readUnsigned());
        result.setRewardValue(wrap.readUnsigned());
        wrap.readUnsigned(6); // unknown
        return result;
    }

    private static H3MSeerHut readSeerHut(ByteWrapper wrap, boolean isRoE) {
        H3MSeerHut hut = new H3MSeerHut();
        int ambiguous = wrap.readUnsigned(); // in ROE it's artifact request, otherwise see code below
        hut.setRequest(isRoE ? new H3MArtifactsQuestRequest().setArtifacts(new int[]{ambiguous})
                : parseQuestRequest(wrap, isRoE, ambiguous));
        hut.setReward(parseReward(wrap, isRoE));
        wrap.readUnsigned(2); // unknown1
        return hut;
    }

    private static H3MResource readResource(ByteWrapper wrap, boolean isRoE) {
        H3MResource result = new H3MResource();
        result.setGuard(wrap.readBoolean() ? readCommonGuardian(wrap, isRoE) : null);
        result.setQuantity(wrap.readInt());
        wrap.readUnsigned(4); // unknown1
        return result;
    }

    private static H3MRandomDwelling readRandomDwelling(ByteWrapper wrap, H3MObjectType type) {
        H3MRandomDwelling result = new H3MRandomDwelling().setOwner(wrap.readInt());
        if (type != H3MObjectType.META_OBJECT_RANDOM_DWELLING_PRESET_ALIGNMENT_ABSOD) {
            result.setAbSodId(wrap.readInt());
            if (result.getAbSodId() == 0) {
                result.setAlignment(wrap.readUnsignedShort());
            }
        }
        if (type != H3MObjectType.META_OBJECT_RANDOM_DWELLING_PRESET_LEVEL_ABSOD) {
            result.setMinLevel(wrap.readUnsigned());
            result.setMaxLevel(wrap.readUnsigned());
        }
        return result;
    }

    private H3MMapEvent readEvent(ByteWrapper wrap, boolean isRoE) {
        H3MMapEvent result = new H3MMapEvent();
        result.setGuard(wrap.readBoolean() ? readCommonGuardian(wrap, isRoE) : null);
        result.setReward(readCommonReward(wrap, isRoE));
        result.setAppliesToPlayers(wrap.readBoolean());
        result.setAppliesToComputers(wrap.readBoolean());
        result.setCancelAfterVisit(wrap.readBoolean());
        wrap.readUnsigned(4); // unknown1
        return result;
    }

    private H3MGarrison readGarrison(ByteWrapper wrap, boolean isRoE) {
        H3MGarrison result = new H3MGarrison();
        result.setOwner(wrap.readInt());
        result.setArmy(readArmy(wrap, isRoE));
        result.setRemovable(!isRoE && wrap.readBoolean()); // TODO check for RoE
        wrap.readUnsigned(8); // unknown1
        return result;
    }

    private H3MMessage readMessage(ByteWrapper wrap) {
        H3MMessage result = new H3MMessage();
        result.setMessage(wrap.readString());
        wrap.readUnsigned(4);
        return result;
    }

    private H3MHero readPlaceholder(ByteWrapper wrap) {
        H3MHero placeholder = new H3MHero().setOwner(wrap.readUnsigned()).setHeroType(wrap.readUnsignedOpt());
        if (placeholder.getHeroType() == null) {
            placeholder.setPowerRating(wrap.readUnsigned());
        }
        return placeholder;
    }

    private Coordinate readCoordinate(ByteWrapper wrap) {
        return new Coordinate(wrap.readUnsigned(), wrap.readUnsigned(), wrap.readUnsigned());
    }

    private BaseLoseCondition parseLoseCondition(ByteWrapper wrap) {
        Integer loseCond = wrap.readUnsignedOpt();
        if (loseCond == null) {
            return new BaseLoseCondition();
        }
        H3MLoseType type = H3MLoseType.values()[loseCond];
        return switch (type) {
            case LOSE_TOWN, LOSE_HERO -> new LoseCoordinateCondition().setCoordinate(readCoordinate(wrap)).setType(type);
            case TIME -> new TimeElapsedCondition().setDays(wrap.readUnsignedShort()).setType(type); // days
        };
    }

    private BaseWinCondition parseWinCondition(ByteWrapper wrap, boolean isRoE) {
        Integer winCond = wrap.readUnsignedOpt();
        if (winCond == null) {
            return new BaseWinCondition();
        }
        boolean normalWin = wrap.readBoolean(); // allow normal win
        boolean appliesToComputer = wrap.readBoolean(); // applies to computer;
        H3MWinType type = H3MWinType.values()[winCond];
        BaseWinCondition result = switch (type) {
            case ACQUIRE_ARTIFACT -> new ArtifactCondition()
                    .setArtifact(readArtifact(wrap, isRoE));
            case ACCUMULATE_CREATURES -> new AccumulateCreatureCondition().setCreature(
                    new H3MCreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                            .setQuantity(wrap.readInt()));  // can't use readCreature because quantity is int, not short
            case ACCUMULATE_RESOURCES -> new AccumulateResourceCondition()
                    .setResType(wrap.readUnsigned()) // 0 - wood, 1 - mercury, 2 - ore, 3 - sulfur, 4 - crystal, 5 - gems, 6 - gold
                    .setAmount(wrap.readInt());
            case UPGRADE_TOWN -> new UpgradeTownCondition()
                    .setCoordinate(readCoordinate(wrap))
                    .setHall(wrap.readUnsigned())
                    .setFort(wrap.readUnsigned());
            case BUILD_GRAIL, DEFEAT_HERO, CAPTURE_TOWN, DEFEAT_MONSTER -> new CoordinateCondition()
                    .setCoordinate(readCoordinate(wrap));
            case FLAG_DWELLINGS, FLAG_MINES -> new BaseWinCondition();
            case TRANSPORT_ARTIFACT -> new TransportArtifactCondition()
                    .setArtType(wrap.readUnsigned())
                    .setCoordinate(readCoordinate(wrap));
        };
        result.setType(type).setNormalWin(normalWin).setAppliesToComputer(appliesToComputer);
        return result;
    }

    private static H3MReward parseReward(ByteWrapper wrap, boolean isRoE) {
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
                PrimarySkills skills = new PrimarySkills();
                switch (wrap.readUnsigned()) {
                    case 0 -> skills.setAttack(wrap.readUnsigned());
                    case 1 -> skills.setDefense(wrap.readUnsigned());
                    case 2 -> skills.setSpellPower(wrap.readUnsigned());
                    case 3 -> skills.setKnowledge(wrap.readUnsigned());
                    default -> throw new IllegalArgumentException(); // TODO assumption, remove after check
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

    private static H3MBaseQuestRequest parseQuestRequest(ByteWrapper wrap, boolean isROE, Integer questType) {
        if (questType == null) {
            return new H3MBaseQuestRequest().setDeadline(wrap.readInt());
        }
        H3MQuestType type = H3MQuestType.values()[questType];
        H3MBaseQuestRequest quest = switch (type) {
            case NONE -> new H3MBaseQuestRequest();
            case EXPERIENCE -> new H3MExperienceQuestRequest().setExperience(wrap.readInt());
            case PRIMARY_SKILLS -> new H3MPrimarySkillsQuestRequest().setPrimarySkills(readPrimarySkills(wrap));
            case DEFEAT_HERO -> new H3MHeroQuestRequest().setHero(wrap.readInt());
            case DEFEAT_MONSTER -> new H3MDefeatMonsterQuestRequest().setId(wrap.readInt());
            case ARTIFACTS -> new H3MArtifactsQuestRequest().setArtifacts(readArtifacts(wrap, isROE));
            case CREATURES -> new H3MCreaturesQuestRequest().setCreatures(readArmy(wrap, isROE, wrap.readUnsigned()));
            case RESOURCES -> new H3MResourcesQuestRequest().setResources(readResources(wrap));
            case BE_HERO -> new H3MHeroQuestRequest().setHero(wrap.readUnsigned());
            case BE_PLAYER -> new H3MPlayerQuestRequest().setPlayer(wrap.readUnsigned());
        };
        quest.setDeadline(wrap.readInt());
        quest.setProposalMessage(wrap.readString());
        quest.setProgressMessage(wrap.readString());
        quest.setCompletionMessage(wrap.readString());
        return quest;
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

    private H3MHero parseHero(ByteWrapper wrap, H3MHeader header) {
        boolean isRoE = header.isRoE();
        boolean isSoD = header.isSoD();
        H3MHero result = new H3MHero().setAbSodId(header.isRoE() ? null : wrap.readInt())
                .setOwner(wrap.readUnsigned())
                .setHeroType(wrap.readUnsigned())
                .setName(wrap.readBoolean() ? wrap.readString() : null);
        if (isSoD) {
            result.setExperience(wrap.readBoolean() ? wrap.readInt() : null);
        } else {
            result.setExperience(wrap.readInt());
        }
        result.setFace(wrap.readBoolean() ? wrap.readUnsigned() : null)
                .setSecondarySkills(wrap.readBoolean() ? readSecondarySkills(wrap, wrap.readInt()) : null)
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, header.isRoE()) : null)
                .setFormation(wrap.readUnsigned())
                .setHeroAtrifacts(wrap.readBoolean() ? parseHeroArtifacts(wrap, header) : null)
                .setPatrolRadius(wrap.readUnsigned())
                .setBiography(!isRoE && wrap.readBoolean() ? wrap.readString() : null)
                .setGender(!isRoE ? wrap.readUnsigned() : null)
                .setSodSpells(isSoD && wrap.readBoolean() ? BitSet.valueOf(wrap.readBytes(9)) : null)
                .setAbSpell(!isSoD && !isRoE ? wrap.readUnsigned() : null)
                .setPrimarySkills(isSoD && wrap.readBoolean() ? readPrimarySkills(wrap) : null)
                .setUnknown2(wrap.readUnsigned(16));
        return result;
    }

    private static int readArtifact(ByteWrapper wrap, boolean isRoE) {
        return isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort();
    }

    private H3MHeroArtifacts parseHeroArtifacts(ByteWrapper wrap, H3MHeader header) {
        boolean isRoE = header.isRoE();
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
                .setMisc4(header.isSoD() ? wrap.readUnsignedShort() : null)
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

    private H3MMapTown readTown(ByteWrapper wrap, H3MHeader header) {
        boolean isRoE = header.isRoE();
        H3MMapTown town = new H3MMapTown().setAbSodId(isRoE ? null : wrap.readInt())
                .setOwner(wrap.readUnsignedOpt())
                .setName(wrap.readBoolean() ? wrap.readString() : null)
                .setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoE) : null)
                .setFormation(wrap.readUnsigned());
        if (wrap.readBoolean()) {
            town.setBuilt(BitSet.valueOf(wrap.readBytes(6)))
                    .setDisabled(BitSet.valueOf(wrap.readBytes(6)));
        } else {
            town.setHasFort(wrap.readBoolean());
        }
        town.setMustHaveSpells(isRoE ? null : wrap.readUnsigned(9))
                .setMayHaveSpells(wrap.readUnsigned(9))
                .setTownEvents(readTownEvents(wrap, header.isSoD()))
                .setAlignment(header.isSoD() ? wrap.readUnsigned() : null)
                .setUnknown1(wrap.readUnsigned(3));
        return town;
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

    private static H3MCommonGuardian readCommonGuardian(ByteWrapper wrap, boolean isRoE) {
        H3MCommonGuardian result = new H3MCommonGuardian();
        result.setMessage(wrap.readString()); // message
        result.setCreatures(wrap.readBoolean() ? readArmy(wrap, isRoE) : null);
        wrap.readUnsigned(4); // unknown1
        return result;
    }

    private H3MSecondarySkill[] readSecondarySkills(ByteWrapper wrap, int size) {
        H3MSecondarySkill[] result = new H3MSecondarySkill[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = readSecondarySkill(wrap);
        }
        return result;
    }

    private static H3MSecondarySkill readSecondarySkill(ByteWrapper wrap) {
        return new H3MSecondarySkill().setType(wrap.readUnsigned()).setLevel(wrap.readUnsigned());
    }

    private static H3MCreatureSlot readCreature(ByteWrapper wrap, boolean isRoE) {
        return new H3MCreatureSlot().setType(isRoE ? wrap.readUnsigned() : wrap.readUnsignedShort())
                .setQuantity(wrap.readUnsignedShort());
    }

    private static H3MCreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE, int count) {
        H3MCreatureSlot[] creatures = new H3MCreatureSlot[count];
        for (int j = 0; j < creatures.length; j++) {
            creatures[j] = readCreature(wrap, isRoE);
        }
        return creatures;
    }

    private static H3MCreatureSlot[] readArmy(ByteWrapper wrap, boolean isRoE) {
        return readArmy(wrap, isRoE, 7);
    }

    private static int[] readResources(ByteWrapper wrap) {
        return wrap.readInt(7);
    }

    private static int[] readArtifacts(ByteWrapper wrap, boolean isRoE) {
        if (isRoE) {
            return wrap.readUnsigned(wrap.readUnsigned());
        } else {
            return wrap.readUnsignedShort(wrap.readUnsigned());
        }
    }

    private static PrimarySkills readPrimarySkills(ByteWrapper wrap) {
        return new PrimarySkills().setAttack(wrap.readUnsigned())
                .setDefense(wrap.readUnsigned())
                .setSpellPower(wrap.readUnsigned())
                .setKnowledge(wrap.readUnsigned());
    }
}
