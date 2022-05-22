package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.ObjectType;
import ua.ihromant.sod.utils.map.ObjectGroup;

@Getter
@Setter
@Accessors(chain = true)
public class ObjectAttribute {
    private String def;
    private int[] passable;
    private int[] active;
    private int allowedLandsapes;
    private int landscapeGroup;
    private int objectClass;
    private int objectNumber;
    private ObjectGroup objectGroup;
    private int above;
    private int[] unknown;
    private ObjectType type;
}
