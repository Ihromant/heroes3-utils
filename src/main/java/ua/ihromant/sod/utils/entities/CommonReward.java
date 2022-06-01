package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.CommonSecondarySkill;

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
    private String[] spells;
    private CreatureSlot[] creatures;
    private int[] unknown;
}
