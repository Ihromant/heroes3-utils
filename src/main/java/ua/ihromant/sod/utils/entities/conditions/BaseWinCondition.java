package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MWinType;

@Getter
@Setter
@Accessors(chain = true)
public class BaseWinCondition {
    private H3MWinType type;
    private boolean normalWin;
    private boolean appliesToComputer;
}
