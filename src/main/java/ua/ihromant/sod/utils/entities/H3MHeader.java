package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.conditions.BaseLoseCondition;
import ua.ihromant.sod.utils.entities.conditions.BaseWinCondition;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MHeader {
    private static final int H3M_FORMAT_ROE = 0x0000000E;
    private static final int H3M_FORMAT_AB = 0x00000015;
    private static final int H3M_FORMAT_SOD = 0x0000001C;
    private static final int H3M_FORMAT_CHR = 0x0000001D;
    private static final int H3M_FORMAT_WOG = 0x00000033;
    private int format;
    private boolean atLeastOneHero;
    private int size;
    private boolean underground;
    private String name;
    private String description;
    private int mapDifficulty;
    private Integer masteryCapLevel;
    private H3MPlayer[] h3mPlayers = new H3MPlayer[8];
    private BaseWinCondition winCondition;
    private BaseLoseCondition loseCondition;
    private int teamsCount;
    private byte[] teams;
    private BitSet availableHeroes;

    public boolean isRoE() {
        return format == H3M_FORMAT_ROE;
    }

    public boolean isSoD() {
        return format == H3M_FORMAT_SOD;
    }
}
