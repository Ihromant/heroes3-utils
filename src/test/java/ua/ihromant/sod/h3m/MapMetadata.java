package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MapMetadata {
    private PlayerMetadata[] playersMetadata = new PlayerMetadata[8];
    private BasicInformation basic;
    private MapTile[][][] tiles;
    private ObjectAttribute[] objectAttributes;
    private ObjectData[] objectData;
}
