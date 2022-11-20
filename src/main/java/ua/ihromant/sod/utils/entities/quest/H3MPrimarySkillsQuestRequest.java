package ua.ihromant.sod.utils.entities.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.PrimarySkills;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPrimarySkillsQuestRequest extends H3MBaseQuestRequest {
    private PrimarySkills primarySkills;
}
