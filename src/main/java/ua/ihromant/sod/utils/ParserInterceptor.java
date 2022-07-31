package ua.ihromant.sod.utils;

import ua.ihromant.sod.utils.entities.Coordinate;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;
import ua.ihromant.sod.utils.entities.MapTile;
import ua.ihromant.sod.utils.entities.H3MPlayer;

public interface ParserInterceptor {
    default void interceptBasics(Coordinate size, String name, String description) {

    }

    default void interceptObjectData(Coordinate coord, H3MObjectAttribute attribute) {

    }

    default void interceptKingdomInfo(int i, H3MPlayer player) {

    }

    default void interceptTile(Coordinate coordinate, MapTile tile) {

    }

    default void interceptImpassable(Coordinate coords, H3MObjectAttribute attribute) {

    }
}
