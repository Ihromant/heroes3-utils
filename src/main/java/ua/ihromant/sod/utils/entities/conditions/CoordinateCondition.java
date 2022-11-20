package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.Coordinate;

@Getter
@Setter
@Accessors(chain = true)
public class CoordinateCondition extends BaseWinCondition {
    private Coordinate coordinate;
}
