package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPlayer {
    private boolean canBeHuman;
    private boolean canBeComputer;
    private int behavior;
    private Integer allowedAlignments;
    private BitSet townTypes;
    private boolean ownsRandomTown;
    private boolean hasMainTown;

    private String startingHeroName;

    private boolean startingHeroIsRandom;
    private Integer startingHeroType;
    private Integer startingHeroFace;

    private H3MStartingTown startingTown;

    private boolean hasAi;
    private int extTypes;
}
