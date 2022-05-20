package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class StartingTownMetadata {
    private Boolean startingTownCreateHero;
    private Integer startingTownType;
    private Coordinates coordinates;
}
