package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class H3MCommonGuardian {
    private String message;
    private H3MCreatureSlot[] creatures;
}
