package ua.ihromant.sod.utils.entities.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MBaseQuestRequest {
    private H3MQuestType type;
    private int deadline;
    private String proposalMessage;
    private String progressMessage;
    private String completionMessage;
}
