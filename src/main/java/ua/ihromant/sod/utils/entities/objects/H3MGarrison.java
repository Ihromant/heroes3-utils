package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class H3MGarrison extends H3MBaseObject {
    private int owner;
    private H3MCreatureSlot[] army;
    private boolean removable;
}
