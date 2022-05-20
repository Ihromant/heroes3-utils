package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.CreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class CommonGuardian {
    private String message;
    private CreatureSlot[] creatures;
    int[] unknown1;
}
