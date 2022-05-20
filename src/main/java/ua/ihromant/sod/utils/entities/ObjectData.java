package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ObjectData {
    private int x;
    private int y;
    private int z;
    private int oaIndex;
    private int[] unknown1;
}
