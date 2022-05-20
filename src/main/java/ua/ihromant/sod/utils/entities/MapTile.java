package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MapTile {
    private BackgroundType terrainType;
    private int terrainSprite;
    private int riverType;
    private int riverSprite;
    private int roadType;
    private int roadSprite;
    private int mirroring;
}
