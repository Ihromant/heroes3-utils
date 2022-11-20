package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;
import ua.ihromant.sod.utils.entities.H3MHeroArtifacts;
import ua.ihromant.sod.utils.entities.H3MSecondarySkill;
import ua.ihromant.sod.utils.entities.PrimarySkills;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MHero extends H3MBaseObject {
    private Integer abSodId;
    private Integer owner;
    private Integer heroType;
    private String name;
    private Integer experience;
    private Integer face;
    private H3MSecondarySkill[] secondarySkills;
    private H3MCreatureSlot[] creatures;
    private Integer formation;
    private H3MHeroArtifacts heroAtrifacts;
    private Integer patrolRadius;
    private String biography;
    private Integer gender;
    private Integer abSpell;
    private BitSet sodSpells;
    private PrimarySkills primarySkills;
    private Integer allowedPlayers;
    private Integer powerRating;
    private int[] unknown2;
}
