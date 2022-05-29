package ua.ihromant.sod.h3m;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConstantsGenerator {
    @Test
    public void generateConstants() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/objectclasses.txt")))).lines()
                .filter(l -> l.startsWith("#define"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[2]), arr -> arr[1], (a, b) -> a, TreeMap::new));
        values.forEach((val, str) -> System.out.println("int " + str + " = " + val + ";"));
    }

    @Test
    public void generateArtifactsLegacy() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("/legacyIds/artifacts.properties"));
        int max = new HashMap<>(prop).values().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        Map<Integer, String> reversed = new HashMap<>(prop).entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getValue().toString()), e -> e.getKey().toString()));
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> reversed.get(i) == null ? "null" : '"' + reversed.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }

    @Test
    public void generateDwellings() {
        new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.txt")))).lines()
                .filter(l -> !l.startsWith("#"))
                .forEach(line -> {
                    int separator = line.indexOf('"');
                    String left = line.substring(0, separator);
                    String right = line.substring(separator + 1);
                    String[] split = left.split("\\s+");
                    String name = right.substring(0, right.indexOf('"')).replace(' ', '_')
                            .replace("'", "").toUpperCase();
                    System.out.println(String.valueOf(Integer.parseInt(split[1])) + ';' + split[4] + ';' + name);
                });
    }

    @Test
    public void generateCreatures() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/creatures.txt")))).lines()
                .filter(l -> !l.startsWith("#"))
                .map(l -> {
                    int separator = l.indexOf('"');
                    String left = l.substring(0, separator);
                    String right = l.substring(separator + 1);
                    return new String[] { left.split("\\s+")[0],
                            right.substring(0, right.indexOf('"')).replace(' ', '_').toUpperCase()};
                })
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> arr[1]));
        values.forEach((val, str) -> System.out.println(str + "=" + val));
    }

    @Test
    public void generateCreaturesLegacy() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("/legacyIds/creatures.properties"));
        int max = new HashMap<>(prop).values().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        Map<Integer, String> reversed = new HashMap<>(prop).entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getValue().toString()), e -> e.getKey().toString()));
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> reversed.get(i) == null ? "null" : '"' + reversed.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }
}
