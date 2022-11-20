package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TimeElapsedCondition extends BaseLoseCondition {
    private int days;
}
