package ua.ihromant.sod.utils.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResourceType {
    WOOD(2, "SAWMILL", 1500, "LUMBER_MILL"),
    MERCURY(1, "ALCHEMISTS_LAB", 3500, "MIDDLE_ENTRANCE"),
    ORE(2, "ORE_PIT", 1500, "TWO_MIDDLE_ENTRANCE"),
    SULFUR(1, "SULFUR_DUNE", 3500, "MIDDLE_ENTRANCE"),
    CRYSTAL(1, "CRYSTAL_CAVERN", 3500, "MIDDLE_ENTRANCE"),
    GEMS(1, "GEM_POND", 3500, "TWO_MIDDLE_ENTRANCE"),
    GOLD(1000, "GOLD_MINE", 7000, "MIDDLE_ENTRANCE");
    private final int dayDelta;
    private final String generatorName;
    private final int aiValue;
    private final String obstacleType;
}
