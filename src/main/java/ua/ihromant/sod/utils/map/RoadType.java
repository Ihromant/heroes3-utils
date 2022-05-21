package ua.ihromant.sod.utils.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoadType { // always 17 images
    NONE(100),
    DIRT(75),
    GRAVEL(65),
    COBBLESTONE(50);
    private final int speed;
}
