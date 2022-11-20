package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccumulateResourceCondition extends BaseWinCondition {
    private int resType;
    private int amount;
}
