package ua.ihromant.sod.utils.entities.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MResourcesQuestRequest extends H3MBaseQuestRequest {
    private int[] resources;
}
