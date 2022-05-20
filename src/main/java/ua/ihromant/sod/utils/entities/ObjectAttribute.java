package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    private int objectGroup;
    private int above;
    private int[] unknown;
    private ObjectType type;
}
