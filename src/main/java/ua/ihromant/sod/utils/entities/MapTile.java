package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.map.BackgroundType;
import ua.ihromant.sod.utils.map.RiverType;
import ua.ihromant.sod.utils.map.RoadType;

@Getter
@Setter
@Accessors(chain = true)
public class MapTile {
    private BackgroundType terrainType;
    private int terrainSprite;
    private RiverType riverType;
    private int riverSprite;
    private RoadType roadType;
    private int roadSprite;
    private int mirroring;
}
