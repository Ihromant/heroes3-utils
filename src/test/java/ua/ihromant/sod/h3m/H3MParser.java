package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.ByteWrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class H3MParser {
    private static final int H3M_FORMAT_ROE = 0x0000000E;
    private static final int H3M_FORMAT_AB = 0x00000015;
    private static final int H3M_FORMAT_SOD = 0x0000001C;
    private static final int H3M_FORMAT_CHR = 0x0000001D;
    private static final int H3M_FORMAT_WOG = 0x00000033;

    @Test
    public void parse() throws IOException {
        MapMetadata map = new MapMetadata();
        byte[] bytes = IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/FBA2018.h3m"))));
        ByteWrapper wrap = new ByteWrapper(bytes);
        int format = wrap.readInt();
        map.setBasic(new BasicInformation()
                .setAtLeast1Hero(wrap.readBoolean())
                .setMapSize(wrap.readInt())
                .setTwoLevel(wrap.readBoolean())
                .setMapName(wrap.readString(wrap.readInt()))
                .setMapDescription(wrap.readString(wrap.readInt()))
                .setMapDifficulty(wrap.readUnsigned()));
        if (format != H3M_FORMAT_ROE) {
            map.getBasic().setMasteryLevelCap(wrap.readUnsigned());
        }
        for (int i = 0; i < 8; i++) {
            PlayerMetadata player = new PlayerMetadata()
                    .setCanBeHuman(wrap.readBoolean())
                    .setCanBeComputer(wrap.readBoolean())
                    .setBehavior(wrap.readUnsigned())
                    .setAllowedAlignments(wrap.readUnsigned())
                    .setTownTypes(wrap.readUnsigned())
                    .setTownConflux(wrap.readUnsigned())
                    .setOwnsRandomTown(wrap.readBoolean())
                    .setHasMainTown(wrap.readBoolean());
            if (player.isHasMainTown()) {
                player.setStartingTown(new StartingTownMetadata()
                                .setStartingTownCreateHero(wrap.readBoolean())
                                .setStartingTownType(wrap.readUnsigned())
                                .setStartingTownXPos(wrap.readUnsigned())
                                .setStartingTownYPos(wrap.readUnsigned())
                                .setStartingTownZPos(wrap.readUnsigned()))
                        .setStartingHeroIsRandom(wrap.readBoolean())
                        .setStartingHeroType(wrap.readUnsigned())
                        .setStartingHeroFace(wrap.readUnsigned())
                        .setStartingHeroName(wrap.readString(wrap.readInt()));
            } else {
                player.setStartingHeroIsRandom(wrap.readBoolean());
                player.setStartingHeroType(wrap.readUnsigned());
                player.setStartingHeroFace(wrap.readUnsigned());
                player.setStartingHeroName(wrap.readString(wrap.readInt()));
                if (player.getStartingHeroType() == 0xFF) {
                    if (player.getTownTypes() != 0) {
                        player.setHasAi(true);
                    } else {

                    }
                } else {

                }
            }
            map.getPlayersMetadata()[i] = player;
        }
        int winCond = wrap.readUnsigned();
        if (winCond != 0xFF) {
            throw new IllegalArgumentException(); // TODO lots of cases
        }
        int loseCond = wrap.readUnsigned();
        if (loseCond != 0xFF) {
            throw new IllegalArgumentException(); // TODO lots of cases
        }
        int teamsSize = wrap.readUnsigned();
        if (teamsSize != 0) {
            throw new IllegalArgumentException(); // TODO parse teams
        }
        int[] availableHeroes = wrap.readUnsigned(format == H3M_FORMAT_ROE ? 16 : 20);
        if (format != H3M_FORMAT_ROE) {
            int empty = wrap.readInt();
        }
        if (format == H3M_FORMAT_SOD) {
            int customHeroesCount = wrap.readUnsigned();
            for (int i = 0; i < customHeroesCount; i++) {
                new CustomHero().setType(wrap.readUnsigned())
                        .setFace(wrap.readUnsigned())
                        .setName(wrap.readString(wrap.readInt()))
                        .setAllowedPlayers(wrap.readUnsigned());
            }
        }
        wrap.readUnsigned(31); // 31 empty bytes
        if (format != H3M_FORMAT_ROE) {
            int[] avaliableArtifacts = wrap.readUnsigned(format == H3M_FORMAT_AB ? 17 : 18);
            if (format == H3M_FORMAT_SOD) {
                int[] availableSpells = wrap.readUnsigned(9);
                int[] availableSkills = wrap.readUnsigned(4);
            }
        }
        int rumorsCount = wrap.readInt();
        for (int i = 0; i < rumorsCount; i++) {
            new AiRumor().setName(wrap.readString(wrap.readInt()))
                    .setDesc(wrap.readString(wrap.readInt()));
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
                heroSettings.setSecondarySkills(new CommonSecondarySkill[wrap.readInt()]);
                for (int j = 0; j < heroSettings.getSecondarySkills().length; j++) {
                    heroSettings.getSecondarySkills()[i] = new CommonSecondarySkill().setType(wrap.readUnsigned()).setLevel(wrap.readUnsigned());
                }
            }
            if (wrap.readBoolean()) {
                heroSettings.setArtConf(new HeroArtifactSettings()
                        .setHeadwear(wrap.readUnsignedShort())
                        .setShoulders(wrap.readUnsignedShort())
                        .setRightHand(wrap.readUnsignedShort())
                        .setLeftHand(wrap.readUnsignedShort())
                        .setTorso(wrap.readUnsignedShort())
                        .setRightRing(wrap.readUnsignedShort())
                        .setLeftRing(wrap.readUnsignedShort())
                        .setFeet(wrap.readUnsignedShort())
                        .setMisc1(wrap.readUnsignedShort())
                        .setMisc2(wrap.readUnsignedShort())
                        .setMisc3(wrap.readUnsignedShort())
                        .setMisc4(wrap.readUnsignedShort())
                        .setDevice1(wrap.readUnsignedShort())
                        .setDevice2(wrap.readUnsignedShort())
                        .setDevice3(wrap.readUnsignedShort())
                        .setDevice4(wrap.readUnsignedShort())
                        .setUnknown(wrap.readUnsignedShort())
                        .setSpellbook(wrap.readUnsignedShort())
                        .setMisc5(wrap.readUnsignedShort())
                        .setBackpack(new int[wrap.readUnsignedShort()]));
                for (int j = 0; j < heroSettings.getArtConf().getBackpack().length; j++) {
                    heroSettings.getArtConf().getBackpack()[j] = wrap.readUnsignedShort();
                }
            }
            if (wrap.readBoolean()) {
                heroSettings.setBiography(wrap.readString(wrap.readInt()));
            }
            heroSettings.setGender(wrap.readUnsigned());
            if (wrap.readBoolean()) {
                heroSettings.setSpells(wrap.readUnsigned(9));
            }
            if (wrap.readBoolean()) {
                heroSettings.setPrimary(new PrimarySkills().setAttack(wrap.readUnsigned())
                        .setDefense(wrap.readUnsigned())
                        .setSpellPower(wrap.readUnsigned())
                        .setKnowledge(wrap.readUnsigned()));
            }
        }
//        byte[] bytez = wrap.readBytes(100);
//        System.out.println();
        if (format != H3M_FORMAT_SOD) {
            throw new IllegalArgumentException("Not supported because not SoD"); // TODO for now
        }
    }
}
