package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MMapMonster {
    private Integer abSodId;
    private int quantity;
    private int disposition;
    private MessageAndTreasure messTreasure;
    private int neverFlees;
    private int doesNotGrow;
    private int[] unknown1;
}
