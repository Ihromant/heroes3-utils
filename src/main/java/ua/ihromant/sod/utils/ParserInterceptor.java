package ua.ihromant.sod.utils;

import ua.ihromant.sod.utils.entities.Coordinate;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;

public interface ParserInterceptor {
    default void interceptBasics(Coordinate size, String name, String description) {

    }

    default void interceptObjectData(Coordinate coord, H3MObjectAttribute attribute) {

    }
}
