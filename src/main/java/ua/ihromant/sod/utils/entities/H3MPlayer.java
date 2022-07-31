package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPlayer {
    private boolean canBeHuman;
    private boolean canBeComputer;
    private int behavior;
    private Integer allowedAlignments;
    private int townTypes;
    private boolean ownsRandomTown;
    private boolean hasMainTown;

    private String startingHeroName;

    private boolean startingHeroIsRandom;
    private int startingHeroType;
    private int startingHeroFace;

    private H3MStartingTown startingTown;

    private boolean hasAi;
    private int extTypes;
}
