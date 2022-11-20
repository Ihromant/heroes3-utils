package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MRandomDwelling extends H3MBaseObject {
    private Integer owner;
    private Integer abSodId;
    private Integer alignment;
    private Integer minLevel;
    private Integer maxLevel;
}
