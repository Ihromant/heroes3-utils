package ua.ihromant.sod.h3m;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ConstantsGenerator {
    @Test
    public void generateConstants() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/objectclasses.txt")))).lines()
                .filter(l -> l.startsWith("#define"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[2]), arr -> arr[1], (a, b) -> a, TreeMap::new));
        values.forEach((val, str) -> System.out.println("int " + str + " = " + val + ";"));
    }
}
