package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MShrine extends H3MBaseObject {
    private int spell;
}
