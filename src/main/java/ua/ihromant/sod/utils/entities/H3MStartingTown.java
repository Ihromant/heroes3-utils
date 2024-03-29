package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.Coordinate;

@Getter
@Setter
@Accessors(chain = true)
public class H3MStartingTown {
    private boolean startingTownCreateHero;
    private Integer startingTownType;
    private Coordinate coordinate;
}
