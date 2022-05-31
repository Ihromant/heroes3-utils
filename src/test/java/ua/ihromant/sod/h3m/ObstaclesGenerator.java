package ua.ihromant.sod.h3m;

import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.H3MParser;
import ua.ihromant.sod.utils.ObjectType;
import ua.ihromant.sod.utils.entities.ObjectAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ObstaclesGenerator {
    @Test
    public void generateObstacles() throws IOException {
        Map<String, ObjectAttribute> obstacles = new HashMap<>();
        for (int i = 0; i < 74; i++) {
            List<ObjectAttribute> current = new ArrayList<>();
            new H3MParser().setDataInterceptor(od -> {
                        ObjectAttribute oa = od.getOa();
                        ObjectType type = oa.getType();
                        if ((type == ObjectType.META_OBJECT_GENERIC_IMPASSABLE_TERRAIN || type == ObjectType.META_OBJECT_GENERIC_IMPASSABLE_TERRAIN_ABSOD)
                                && !obstacles.containsKey(oa.def())) {
                            current.add(oa);
                        }
                    })
                    .parse(H3MParserTest.getUnzippedBytes("/generated/Generated" + i));
            for (ObjectAttribute oa : current) {
                obstacles.put(oa.def(), oa);
            }
            System.out.println(i + ": " + current.size());
        }
        Set<List<ObjectAttribute.Shift>> shifts = obstacles.values().stream().map(od -> od.passableShifts().toList()).collect(Collectors.toSet());
        System.out.println(shifts.size() + " " + shifts.stream().mapToInt(List::size).max().orElseThrow() + " "
                + shifts.stream().mapToInt(s -> s.stream().mapToInt(ObjectAttribute.Shift::getDy).min().orElse(0)).min().orElseThrow());
        shifts.forEach(ObstaclesGenerator::generateShiftsSql);
        System.out.println(obstacles.size());
    }

    private static void generateShiftsSql(List<ObjectAttribute.Shift> passable) {
        String name = generateName(passable);
        System.out.println("insert into map_obstacle_type (");
        System.out.println("id, full_name");
        System.out.println(") values");
        System.out.println("((SELECT MAX(id) + 1 FROM map_obstacle_type),'" + name + "'" + ");");

        if (passable.isEmpty()) {
            System.out.println();
            return;
        }
        System.out.println("insert into map_obstacle_direction (");
        System.out.println("id, obstacle_type_id, dx, dy");
        System.out.println(") values");
        System.out.println(IntStream.range(0, passable.size()).mapToObj(i -> {
            return "((SELECT MAX(id) + " + (i + 1) + " FROM map_obstacle_direction),"
                    + "(SELECT id FROM map_obstacle_type WHERE full_name = '" + name + "'),"
                    + passable.get(i).getDx() + "," + passable.get(i).getDy() + ")";
        }).collect(Collectors.joining(",\n", "", ";\n")));
    }

    private static String generateName(List<ObjectAttribute.Shift> shifts) {
        if (shifts.isEmpty()) {
            return "NONE";
        }
        if (Collections.singletonList(new ObjectAttribute.Shift(0, 0)).equals(shifts)) {
            return "SINGLE";
        }
        return shifts.stream().sorted(Comparator.comparing(ObjectAttribute.Shift::getDy).reversed().thenComparing(ObjectAttribute.Shift::getDx))
                .map(sh -> String.valueOf(Math.abs(sh.getDx())) + Math.abs(sh.getDy())).collect(Collectors.joining());
    }
}
