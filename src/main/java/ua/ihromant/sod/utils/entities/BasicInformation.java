package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BasicInformation {
    private boolean atLeast1Hero;
    private String mapName;
    private String mapDescription;
    private int mapDifficulty;
    private int masteryLevelCap;
}
