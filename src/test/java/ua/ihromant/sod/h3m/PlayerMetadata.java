package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PlayerMetadata {
    private boolean canBeHuman;
    private boolean canBeComputer;
    private int behavior;
    private Integer allowedAlignments;
    private int townTypes;
    private Integer townConflux;
    private boolean ownsRandomTown;
    private boolean hasMainTown;

    private String startingHeroName;

    private boolean startingHeroIsRandom;
    private int startingHeroType;
    private int startingHeroFace;

    private StartingTownMetadata startingTown;

    private boolean hasAi;
    private int extTypes;
}
