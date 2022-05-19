package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class StaticGarrison {
    private int owner;
    private CreatureSlot[] creatures;
    private int removableUnits;
    private int[] unknown1;
}
