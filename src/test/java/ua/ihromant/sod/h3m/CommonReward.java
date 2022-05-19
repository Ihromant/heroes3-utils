package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CommonReward {
    private int experience;
    private int spellPoints;
    private int morale;
    private int luck;
    private int[] resources;
    private PrimarySkills skills;
    private CommonSecondarySkill[] secondarySkills;
    private int[] artifacts;
    private int[] spells;
    private CreatureSlot[] creatures;
    private int[] unknown;
}
