package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MLoseType;

@Getter
@Setter
@Accessors(chain = true)
public class BaseLoseCondition {
    private H3MLoseType type;
}
