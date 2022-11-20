package ua.ihromant.sod.utils.entities.conditions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.Coordinate;

@Getter
@Setter
@Accessors(chain = true)
public class UpgradeTownCondition extends BaseWinCondition {
    private Coordinate coordinate;
    private int hall; // 0 - town, 1 - city, 2 - capitol
    private int fort; // 0 - fort, 1 - citadel, 2 - castle
}
