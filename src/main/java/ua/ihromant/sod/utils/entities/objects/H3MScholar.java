package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.ScholarRewardType;

@Getter
@Setter
@Accessors(chain = true)
public class H3MScholar extends H3MBaseObject {
    private ScholarRewardType rewardType;
    private int rewardValue;
}
