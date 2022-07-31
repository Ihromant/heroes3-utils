package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MHero {
    private Integer abSodId;
    private int owner;
    private int type;
    private String name;
    private Integer experience;
    private Integer face;
    private H3MSecondarySkill[] secondarySkills;
    private H3MCreatureSlot[] creatures;
    private int formation;
    private H3MHeroArtifacts heroAtrifacts;
    private int patrolRadius;
    private String biography;
    private Integer gender;
    private Integer abSpell;
    private int[] sodSpells;
    private H3MPrimarySkills primarySkills;
    private int[] unknown2;
}
