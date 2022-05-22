package ua.ihromant.sod.utils.map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.BasicInformation;
import ua.ihromant.sod.utils.entities.MapTile;
import ua.ihromant.sod.utils.entities.ObjectAttribute;
import ua.ihromant.sod.utils.entities.ObjectData;
import ua.ihromant.sod.utils.entities.PlayerMetadata;

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
