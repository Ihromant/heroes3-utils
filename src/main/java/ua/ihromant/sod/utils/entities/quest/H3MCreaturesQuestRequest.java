package ua.ihromant.sod.utils.entities.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MCreatureSlot;

@Getter
@Setter
@Accessors(chain = true)
public class H3MCreaturesQuestRequest extends H3MBaseQuestRequest {
    private H3MCreatureSlot[] creatures;
}
