package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPrimarySkills {
    private int attack;
    private int defense;
    private int spellPower;
    private int knowledge;
}
