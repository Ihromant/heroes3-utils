package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class StartingTownMetadata {
    private boolean startingTownCreateHero;
    private int startingTownType;
    private int startingTownXPos;
    private int startingTownYPos;
    private int startingTownZPos;
}
