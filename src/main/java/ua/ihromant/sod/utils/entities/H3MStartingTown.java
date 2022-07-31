package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MStartingTown {
    private Boolean startingTownCreateHero;
    private Integer startingTownType;
    private Coordinate coordinates;
}
