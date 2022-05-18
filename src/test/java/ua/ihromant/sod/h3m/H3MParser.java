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
        byte[] bytes = IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/Viking.h3m"))));
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
        if (format != H3M_FORMAT_SOD) {
            throw new IllegalArgumentException("Not supported because not SoD"); // TODO for now
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
                boolean pos8 = wrap.readBoolean();
                int pos9 = wrap.readUnsigned();
                if (pos9 == 0xFF) {
                    if (player.getTownTypes() != 0) {
                        player.setHasAi(true);
                    } else {
                        player.setStartingHeroIsRandom(pos8);
                        player.setStartingHeroType(pos9);
                        player.setStartingHeroFace(wrap.readUnsigned());
                        player.setStartingHeroName(wrap.readString(wrap.readInt()));
                    }
                } else {
                    player.setStartingHeroIsRandom(pos8);
                    player.setStartingHeroType(pos9);
                    player.setStartingHeroFace(wrap.readUnsigned());
                    player.setStartingHeroName(wrap.readString(wrap.readInt()));
                }
            }
            map.getPlayersMetadata()[i] = player;
        }
        //byte[] bytez = wrap.readBytes(100);
        //System.out.println();
    }
}
