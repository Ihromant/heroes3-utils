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
    private H3MStartingTown startingTown;

    private boolean startingHeroIsRandom;
    private Integer startingHeroType;
    private Integer startingHeroFace;
    private String startingHeroName;

}
