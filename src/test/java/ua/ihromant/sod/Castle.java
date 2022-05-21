package ua.ihromant.sod;

import ua.ihromant.sod.utils.map.BackgroundType;

public enum Castle {
    CASTLE(BackgroundType.GRASS), RAMPART(BackgroundType.GRASS), TOWER(BackgroundType.SNOW),
    INFERNO(BackgroundType.LAVA), NECROPOLIS(BackgroundType.DIRT), DUNGEON(BackgroundType.SUBTERRANEAN),
    STRONGHOLD(BackgroundType.ROUGH), FORTRESS(BackgroundType.SWAMP), CONFLUX(BackgroundType.GRASS);

    private final BackgroundType background;

    Castle(BackgroundType background) {
        this.background = background;
    }

    public BackgroundType getBackground() {
        return this.background;
    }
}
