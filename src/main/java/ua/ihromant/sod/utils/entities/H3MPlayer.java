package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPlayer {
    private Control control;
    private Behavior behavior;
    private int allowedAlignments;
    private BitSet townTypes;
    private boolean ownsRandomTown;
    private boolean hasMainTown;

    private boolean startingHeroIsRandom;
    private Integer startingHeroType;
    private Integer startingHeroFace;
    private String startingHeroName;

    private H3MStartingTown startingTown;

    private boolean hasAi;
    private int extTypes;

    public enum Control {
        NONE, COMPUTER, HUMAN;

        public static Control of(boolean canBeHuman, boolean canBeComputer) {
            return canBeComputer ? canBeHuman ? HUMAN : COMPUTER : NONE;
        }
    }

    public enum Behavior {
        RANDOM, WARRIOR, BUILDER, EXPLORER
    }
}
