package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.Disposition;
import ua.ihromant.sod.utils.entities.H3MMessageAndTreasure;

@Getter
@Setter
@Accessors(chain = true)
public class H3MMapMonster extends H3MBaseObject {
    private Integer abSodId;
    private int quantity;
    private Disposition disposition;
    private H3MMessageAndTreasure messTreasure;
    private int neverFlees;
    private int doesNotGrow;
    private int[] unknown1;
}
