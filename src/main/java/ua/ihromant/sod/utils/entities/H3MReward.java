package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MReward {
    private int experience;
    private int spellPoints;
    private int morale;
    private int luck;
    private int[] resources;
    private PrimarySkills skills;
    private H3MSecondarySkill[] secondarySkills;
    private int[] artifacts;
    private int[] spells;
    private H3MCreatureSlot[] creatures;
    private int[] unknown;
}
