package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.Coordinate;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;
import ua.ihromant.sod.utils.entities.H3MObjectType;

@Getter
@Setter
@Accessors(chain = true)
public class H3MBaseObject {
    private H3MObjectType type;
    private H3MObjectAttribute attribute;
    private Coordinate coordinate;
}
