package ua.ihromant.sod.h3m;

import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
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
    public void generateDwellingsLegacy() {
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

    @Test
    public void generateDwellings() {
        Map<Integer, String> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.csv")))).lines()
            .skip(1)
                .map(s -> s.split(";"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> arr[2]));
        int max = values.keySet().stream().mapToInt(val -> Integer.parseInt(val.toString())).max().orElseThrow();
        System.out.println(IntStream.rangeClosed(0, max).mapToObj(i -> values.get(i) == null ? "null" : '"' + values.get(i) + '"').collect(Collectors.joining(", ", "{", "}")));
    }

    @Test
    public void generateDwellingsSQL() {
        Map<Integer, Integer> values = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/legacyIds/dwellings.csv")))).lines()
                .skip(1)
                .map(s -> s.split(";"))
                .collect(Collectors.toMap(arr -> Integer.parseInt(arr[0]), arr -> Integer.parseInt(arr[1])));
        IntStream.range(0, 12)
                .forEach(i -> {
                    AtomicInteger counter = new AtomicInteger();
                    IntStream.range(i * 14, Math.min(i * 14 + 14, 145)).forEach(un -> values.entrySet().stream().filter(e -> e.getValue() == un)
                            .forEach(e -> System.out.println("("
                            + "(SELECT MAX(id) + " + counter.incrementAndGet() + " FROM dwelling_type),"
                            + "'" + ObjectNumberConstants.DWELLINGS[e.getKey()] + "',"
                            + "(SELECT id from creature where full_name = '" + ObjectNumberConstants.CREATURES[un] + "')"
                            + "),"))
                    );
                    System.out.println();
                });
    }
}
