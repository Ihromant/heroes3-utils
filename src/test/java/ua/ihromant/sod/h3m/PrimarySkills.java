package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PrimarySkills {
    private int attack;
    private int defense;
    private int spellPower;
    private int knowledge;
}
