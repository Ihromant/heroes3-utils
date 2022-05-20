package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CommonHero {
    private Integer abSodId;
    private int owner;
    private int type;
    private String name;
    private Integer experience;
    private Integer face;
    private CommonSecondarySkill[] secondarySkills;
    private CreatureSlot[] creatures;
    private int formation;
    private HeroArtifacts heroAtrifacts;
    private int patrolRadius;
    private String biography;
    private Integer gender;
    private Integer abSpell;
    private int[] sodSpells;
    private PrimarySkills primarySkills;
    private int[] unknown2;
}
