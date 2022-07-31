package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class StaticGarrison {
    private int owner;
    private H3MCreatureSlot[] creatures;
    private int removableUnits;
    private int[] unknown1;
}
