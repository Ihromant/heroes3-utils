package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.CreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class StaticGarrison {
    private int owner;
    private CreatureSlot[] creatures;
    private int removableUnits;
    private int[] unknown1;
}
