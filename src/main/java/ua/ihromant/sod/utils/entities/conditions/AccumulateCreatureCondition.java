package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class AccumulateCreatureCondition extends BaseWinCondition {
    private H3MCreatureSlot creature;
}
