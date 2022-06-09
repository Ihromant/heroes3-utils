package ua.ihromant.sod.utils.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BackgroundType {
    DIRT(0x0F_3F_50, 46),
    SAND(0x8F_CF_DF, 24),
    GRASS(0x00_40_00, 79),
    SNOW(0xC0_C0_B0, 79),
    SWAMP(0x6F_80_4F, 79),
    ROUGH(0x30_70_80, 79),
    SUBTERRANEAN(0x00_30_80, 79),
    LAVA(0x4F_4F_4F, 79),
    WATER(0x90_50_0F, 33),
    ROCK(0x00_00_00, 48);
    private final int rgbMinimap;
    private final int tilesCount; // size always 32x32
}
