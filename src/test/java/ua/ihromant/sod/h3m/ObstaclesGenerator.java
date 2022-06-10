package ua.ihromant.sod.h3m;

import org.junit.jupiter.api.Test;
import ua.ihromant.sod.ImageMerger;
import ua.ihromant.sod.ImageMetadata;
import ua.ihromant.sod.utils.H3MParser;
import ua.ihromant.sod.utils.ObjectNumberConstants;
import ua.ihromant.sod.utils.ObjectType;
import ua.ihromant.sod.utils.bytes.Utils;
import ua.ihromant.sod.utils.entities.ObjectAttribute;
import ua.ihromant.sod.utils.map.BackgroundType;
import ua.ihromant.sod.utils.map.ResourceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
//        Set<List<ObjectAttribute.Shift>> shifts = obstacles.values().stream().map(od -> od.passableShifts().toList()).collect(Collectors.toSet());
//        System.out.println(shifts.size() + " " + shifts.stream().mapToInt(List::size).max().orElseThrow() + " "
//                + shifts.stream().mapToInt(s -> s.stream().mapToInt(ObjectAttribute.Shift::getDy).min().orElse(0)).min().orElseThrow());
//        shifts.forEach(ObstaclesGenerator::generateShiftsSql);
        System.out.println(obstacles.size());
        for (ObjectAttribute oa : obstacles.values()) {
            generateObstacle(oa);
        }
    }

    private static void generateObstacle(ObjectAttribute oa) throws IOException {
        System.out.println("insert into map_impassable (");
        System.out.println("id, full_name, obstacle_type_id, pict_width, pict_height, pict_count");
        System.out.println(") values");
        String passableName = generateName(oa.passableShifts().toList());
        String def = oa.def();
        ImageMetadata meta = ImageMerger.mergeImage("/home/ihromant/Games/units/images-shadow/", "/home/ihromant/workspace/ihromant.github.io/img/mapimpassable", def);
        System.out.println("((SELECT MAX(id) + 1 FROM map_impassable),'" + def
                + "',(select id from map_obstacle_type where full_name = '" + passableName + "'),"
                + (meta.getImageWidth() / 32) + "," + (meta.getImageHeight() / 32) + "," + meta.getImagesCount() + ");");
        List<BackgroundType> backgroundTypes = oa.backgrounds().toList();
        System.out.println("insert into impassable_to_terrain (");
        System.out.println("id, impassable_id, terrain_id");
        System.out.println(") values");
        System.out.println(IntStream.range(0, backgroundTypes.size()).mapToObj(i -> {
            return "((SELECT MAX(id) + " + (i + 1) + " FROM impassable_to_terrain),"
                    + "(SELECT id FROM map_impassable WHERE full_name = '" + def + "'),"
                    + "(SELECT id FROM terrain WHERE full_name = '" + backgroundTypes.get(i) + "'))";
        }).collect(Collectors.joining(",\n", "", ";\n")));
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

    @Test
    public void generateMines() throws IOException {
        Map<String, ObjectAttribute> defs = new TreeMap<>();
        for (int i = 0; i < 74; i++) {
            new H3MParser().setDataInterceptor(od -> {
                        ObjectType type = od.getOa().getType();
                        if (type == ObjectType.META_OBJECT_RESOURCE_GENERATOR && !defs.containsKey(od.getOa().def())) {
                            defs.put(od.getOa().def(), od.getOa());
                        }
                    })
                    .parse(H3MParserTest.getUnzippedBytes("/generated/Generated" + i));
        }
        defs.forEach((k, v) -> System.out.println(k + "="
                + Utils.ones(v.getLandscapeGroup()).mapToObj(i -> BackgroundType.values()[i]).toList()));
        defs.get("avmsulf0").setLandscapeGroup(255);
        defs.get("avmalch0").setLandscapeGroup(255 ^ (1 << BackgroundType.SNOW.ordinal()));
        defs.get("avmgems0").setLandscapeGroup(1 << BackgroundType.GRASS.ordinal());
        defs.get("avmgerf0").setLandscapeGroup(1 << BackgroundType.ROUGH.ordinal());
        defs.put("avmgesd0", new ObjectAttribute().setDef("avmgesd0.def").setLandscapeGroup(1 << BackgroundType.SAND.ordinal()));
        defs.put("avmgesu0", new ObjectAttribute().setDef("avmgesu0.def").setLandscapeGroup(1 << BackgroundType.SUBTERRANEAN.ordinal()));
        defs.put("avmgesw0", new ObjectAttribute().setDef("avmgesw0.def").setLandscapeGroup(1 << BackgroundType.SWAMP.ordinal()));
        defs.put("avmords0", new ObjectAttribute().setDef("avmords0.def").setLandscapeGroup(1 << BackgroundType.SAND.ordinal()));
        defs.put("avmorsb0", new ObjectAttribute().setDef("avmorsb0.def").setLandscapeGroup(1 << BackgroundType.SUBTERRANEAN.ordinal()));
        defs.put("avmswds0", new ObjectAttribute().setDef("avmswds0.def").setLandscapeGroup(1 << BackgroundType.SAND.ordinal()));
        ResourceType resource = null;
        for (ObjectAttribute oa : defs.values()) {
            boolean resourceChanged = resource != (resource = byDef(oa.def()));
            String genName = resource.getGeneratorName();
            if (resourceChanged) {
                System.out.println();
                System.out.println("insert into resource_generator (");
                System.out.println("id, full_name, resource_id, daily_growth, obstacle_type_id, ai_value");
                System.out.println(") values");
                System.out.println("((SELECT MAX(id) + 1 FROM resource_generator),'" + genName
                        + "',(select id from resource where full_name = '" + resource.name() + "'),"
                        + resource.getDayDelta() + ",(select id from map_obstacle_type where full_name = '" + resource.getObstacleType()
                        + "')," + resource.getAiValue() + ");");
                System.out.println("insert into generator_to_terrain (");
                System.out.println("id, graph_name, generator_id, terrain_id, pict_width, pict_height, pict_count");
                System.out.println(") values");
            }
            long count = Utils.ones(oa.getLandscapeGroup()).count();
            String name = genName + "_" + (count > 2 ? "COMMON" :
            count == 1 ? BackgroundType.values()[Utils.ones(oa.getLandscapeGroup()).findFirst().orElseThrow()]
                    : Utils.ones(oa.getLandscapeGroup()).mapToObj(i -> BackgroundType.values()[i].toString()).collect(Collectors.joining("_")));
            ImageMetadata meta = ImageMerger.mergeImage("/home/ihromant/Games/units/images-shadow/", oa.def(), name.toLowerCase());
            System.out.println(Utils.ones(oa.getLandscapeGroup()).mapToObj(i ->
                            "((SELECT MAX(id) + 1 FROM generator_to_terrain),'" + name
                                    + "',(select id from resource_generator where full_name = '" + genName
                                    + "'),(select id from terrain where full_name = '" + BackgroundType.values()[i] + "'),"
                                    + (meta.getImageWidth() / 32) + "," + (meta.getImageHeight() / 32)
                                    + "," + meta.getImagesCount() + ")")
                    .collect(Collectors.joining(",\n", "", ";")));
        }
    }

    private static ResourceType byDef(String def) {
        return switch (def.substring(3, 5)) {
            case "su" -> ResourceType.SULFUR;
            case "or" -> ResourceType.ORE;
            case "sw", "sa" -> ResourceType.WOOD;
            case "go" -> ResourceType.GOLD;
            case "al" -> ResourceType.MERCURY;
            case "ge" -> ResourceType.GEMS;
            case "cr" -> ResourceType.CRYSTAL;
            default -> throw new IllegalArgumentException();
        };
    }

    @Test
    public void generateResources() {
        Arrays.stream(ResourceType.values()).forEach(res -> System.out.println("(COALESCE((SELECT MAX(id) FROM resource), 0) + "
                + (res.ordinal() + 1) + ",'" + res.name() + "'," + res.getDayDelta() + "),"));
    }

    @Test
    public void generateMonsters() throws IOException {
        Map<Integer, ObjectAttribute> defs = new TreeMap<>();
        for (int i = 0; i < 74; i++) {
            new H3MParser().setDataInterceptor(od -> {
                        if (od.getOa().getType() == ObjectType.META_OBJECT_MONSTER && !defs.containsKey(od.getOa().getObjectNumber())) {
                            defs.put(od.getOa().getObjectNumber(), od.getOa());
                        }
                    })
                    .parse(H3MParserTest.getUnzippedBytes("/generated/Generated" + i));
        }
        defs.put(139, new ObjectAttribute().setDef("avwpeas.def").setObjectNumber(139));
        for (ObjectAttribute oa : defs.values()) {
            ImageMetadata meta = ImageMerger.mergeImage("/home/ihromant/Games/units/images-shadow/", oa.def(), ObjectNumberConstants.CREATURES[oa.getObjectNumber()].toLowerCase());
            System.out.println((meta.getImageWidth() != 64 || meta.getImageHeight() != 64 ? "!!!" : "")
                    + ObjectNumberConstants.CREATURES[oa.getObjectNumber()] + "=" + meta.getImagesCount());
        }
    }
}