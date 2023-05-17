package ua.ihromant.sod;

import ua.ihromant.sod.utils.map.BackgroundType;

public enum Castle {
    CASTLE(BackgroundType.grass), RAMPART(BackgroundType.grass), TOWER(BackgroundType.snow),
    INFERNO(BackgroundType.lava), NECROPOLIS(BackgroundType.dirt), DUNGEON(BackgroundType.subterranean),
    STRONGHOLD(BackgroundType.rough), FORTRESS(BackgroundType.swamp), CONFLUX(BackgroundType.grass);

    private final BackgroundType background;

    Castle(BackgroundType background) {
        this.background = background;
    }

    public BackgroundType getBackground() {
        return this.background;
    }
}
