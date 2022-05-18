package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BasicInformation {
    private boolean atLeast1Hero;
    private int mapSize;
    private boolean twoLevel;
    private String mapName;
    private String mapDescription;
    private int mapDifficulty;
    private int masteryLevelCap;
}
