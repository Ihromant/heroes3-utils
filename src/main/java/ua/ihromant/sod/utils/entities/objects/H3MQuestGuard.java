package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.quest.H3MBaseQuestRequest;

@Getter
@Setter
@Accessors(chain = true)
public class H3MQuestGuard extends H3MBaseObject {
    private H3MBaseQuestRequest request;
}
