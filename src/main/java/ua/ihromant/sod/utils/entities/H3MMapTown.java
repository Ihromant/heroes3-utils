package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MMapTown {
    private Integer abSodId;
    private int owner;
    private String name;
    private H3MCreatureSlot[] creatures;
    private int formation;
    private BitSet built;
    private BitSet disabled;
    private Boolean hasFort;
    private int[] mustHaveSpells;
    private int[] mayHaveSpells;
    private H3MTownEvent[] townEvents;
    private Integer alignment;
    private int[] unknown1;
}
