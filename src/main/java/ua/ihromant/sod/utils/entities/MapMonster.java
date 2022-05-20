package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.MessageAndTreasure;

@Getter
@Setter
@Accessors(chain = true)
public class MapMonster {
    private Integer abSodId;
    private int quantity;
    private int disposition;
    private MessageAndTreasure messTreasure;
    private int neverFlees;
    private int doesNotGrow;
    private int[] unknown1;
}
