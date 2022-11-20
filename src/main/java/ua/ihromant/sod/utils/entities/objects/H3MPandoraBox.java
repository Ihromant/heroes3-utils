package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.H3MReward;

@Getter
@Setter
@Accessors(chain = true)
public class H3MPandoraBox extends H3MBaseObject {
    private H3MCommonGuardian guard;
    private H3MReward reward;
}
