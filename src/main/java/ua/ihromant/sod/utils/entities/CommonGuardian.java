package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CommonGuardian {
    private String message;
    private H3MCreatureSlot[] creatures;
    int[] unknown1;
}
