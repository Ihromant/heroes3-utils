package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AiHeroSettings {
    private int gender;
    private int experience;
    private CommonSecondarySkill[] secondarySkills;
    private HeroArtifactSettings artConf;
    private String biography;
    private int[] spells;
    private PrimarySkills primary;
}
