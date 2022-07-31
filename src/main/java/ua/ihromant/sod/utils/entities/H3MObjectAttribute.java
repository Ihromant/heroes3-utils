package ua.ihromant.sod.utils.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.H3MObjectType;
import ua.ihromant.sod.utils.bytes.Utils;
import ua.ihromant.sod.utils.map.BackgroundType;
import ua.ihromant.sod.utils.map.ObjectGroup;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class H3MObjectAttribute {
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

    public H3MObjectType type() {
        return H3MObjectType.objectClassToType(objectClass);
    }

    public Stream<BackgroundType> backgrounds() {
        return Utils.ones(landscapeGroup).mapToObj(i -> BackgroundType.values()[i]);
    }

    public Stream<Shift> passableShifts() {
        return IntStream.range(0, passable.length).boxed()
                .flatMap(j -> Utils.zeros(passable[j], 8).mapToObj(i -> new Shift(i - 7, j - passable.length + 1)));
    }

    public Stream<Shift> activeShifts() {
        return IntStream.range(0, active.length).boxed()
                .flatMap(j -> Utils.ones(active[j]).mapToObj(i -> new Shift(i - 7, j - active.length + 1)));
    }

    public String def() {
        return def.substring(0, def.indexOf('.')).toLowerCase();
    }

    @EqualsAndHashCode
    @ToString
    @RequiredArgsConstructor
    @Getter
    public static class Shift {
        private final int dx;
        private final int dy;
    }
}
