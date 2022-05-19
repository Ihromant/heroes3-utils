package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CommonGuardian {
    private String message;
    private CreatureSlot[] creatures;
    int[] unknown1;
}
