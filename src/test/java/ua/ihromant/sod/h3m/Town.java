package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Town {
    private Integer abSodId;
    private int owner;
    private String name;
    private CreatureSlot[] creatures;
    private int formation;
    private int[] built;
    private int[] disabled;
    private Boolean hasFort;
    private int[] mustHaveSpells;
    private int[] mayHaveSpells;
    private TownEvent[] townEvents;
    private Integer alignment;
    private int[] unknown1;
}
