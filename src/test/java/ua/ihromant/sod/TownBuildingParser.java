package ua.ihromant.sod;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TownBuildingParser {
    private static void header() {
        System.out.println("insert into town_building (");
        System.out.println("id, full_name, castle_id, parent_id, picture_id, wood, mercury, ore, sulfur, crystal, gems, gold");
        System.out.println(") values");
    }

    @Test
    public void generateTownBuildings() throws IOException {
        List<String> lines = IOUtils.readLines(Objects.requireNonNull(getClass().getResourceAsStream("/buildings.txt")), Charset.defaultCharset());
        Map<String, List<String>> specific = readSpecific(lines);
        List<String> standard = readStandard(lines);
        Map<String, List<String>> dwellings = readDwellings(lines);
        Arrays.stream(ObjectNumberConstants.CASTLES).forEach(castle -> generateTownBuildings(castle, standard, specific.get(castle), dwellings.get(castle)));
    }

    private void generateTownBuildings(String castle, List<String> standard, List<String> specific, List<String> dwellings) {
        header();
        System.out.println("((SELECT MAX(id) + 1 FROM town_building),'" + castle + "_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,10,0,0,0,0,0,0,0),");
        System.out.println("((SELECT MAX(id) + 2 FROM town_building),'GRAIL',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,26,0,0,0,0,0,0,0),");
        System.out.println("((SELECT MAX(id) + 3 FROM town_building),'" + castle + "_FORT',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,7,20,0,20,0,0,0,5000),");
        System.out.println("((SELECT MAX(id) + 4 FROM town_building),'" + castle + "_TAVERN',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,5,5,0,0,0,0,0,500),");
        System.out.println("((SELECT MAX(id) + 5 FROM town_building),'" + castle + "_BLACKSMITH',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,16,5,0,0,0,0,0,500),");
        System.out.println("((SELECT MAX(id) + 6 FROM town_building),'" + castle + "_MARKETPLACE',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,14,5,0,0,0,0,0,500),");
        System.out.println("((SELECT MAX(id) + 7 FROM town_building),'" + castle + "_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,0,5,0,5,0,0,0,2000),");
        System.out.println("((SELECT MAX(id) + 8 FROM town_building),'" + castle + "_SHIPYARD',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,6,20,0,0,0,0,0,2000);");
        header();
        System.out.println("((SELECT MAX(id) + 1 FROM town_building),'" + castle + "_TOWN_HALL',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_VILLAGE_HALL'),11,0,0,0,0,0,0,2500),");
        System.out.println("((SELECT MAX(id) + 2 FROM town_building),'" + castle + "_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_MARKETPLACE'),15,0,0,5,0,0,0,5000),");
        System.out.println("((SELECT MAX(id) + 3 FROM town_building),'" + castle + "_CITADEL',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_FORT'),8,0,0,5,0,0,0,2500),");
        System.out.println("((SELECT MAX(id) + 4 FROM town_building),'" + castle + "_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_MAGE_GUILD_1'),1,5,4,5,4,4,4,5000);");
        header();
        System.out.println("((SELECT MAX(id) + 1 FROM town_building),'" + castle + "_CITY_HALL',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_TOWN_HALL'),12,0,0,0,0,0,0,5000),");
        System.out.println("((SELECT MAX(id) + 2 FROM town_building),'" + castle + "_CASTLE',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_CITADEL'),9,10,0,10,0,0,0,5000),");
        System.out.println("((SELECT MAX(id) + 3 FROM town_building),'" + castle + "_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_MAGE_GUILD_2'),2,5,6,5,6,6,6,1000);");
        header();
        System.out.println("((SELECT MAX(id) + 1 FROM town_building),'" + castle + "_CAPITOL',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_CITY_HALL'),13,0,0,0,0,0,0,10000),");
        System.out.println("((SELECT MAX(id) + 2 FROM town_building),'" + castle + "_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_MAGE_GUILD_3'),3,5,8,5,8,8,8,1000);");
        header();
        System.out.println("((SELECT MAX(id) + 1 FROM town_building),'" + castle + "_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = '" + castle + "'),(SELECT id FROM town_building WHERE full_name='" + castle + "_MAGE_GUILD_4'),4,5,10,5,10,10,10,1000);");
        System.out.println();
        header();
        System.out.println(IntStream.range(0, 7).mapToObj(i -> {
            String[] items = dwellings.get(i).split("\\s+");
            return "((SELECT MAX(id) + " + (i + 1) + " FROM town_building),'" + items[7] + "',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,"
                    + (30 + i) + "," + Arrays.stream(items, 0, 7).collect(Collectors.joining(",")) + ")";
        }).collect(Collectors.joining(",\n")) + ";\n");
        header();
        System.out.println(IntStream.range(0, 7).mapToObj(i -> {
            String[] items = dwellings.get(i + 7).split("\\s+");
            return "((SELECT MAX(id) + " + (i + 1) + " FROM town_building),'UPGR_" + items[7] + "',(SELECT id FROM castle WHERE full_name='" + castle + "'),"
                    + "(SELECT id from town_building WHERE full_name='" + "'),"
                    + (37 + i) + "," + Arrays.stream(items, 0, 7).collect(Collectors.joining(",")) + ")";
        }).collect(Collectors.joining(",\n")) + ";\n");
        header();
        AtomicInteger counter = new AtomicInteger();
        System.out.println(specific.stream().map(s -> s.split("\\s+"))
                .filter(items -> items.length > 7).map(items -> "((SELECT MAX(id) + " + counter.incrementAndGet() +
                        " FROM town_building),'" + Arrays.stream(items, 7, items.length).collect(Collectors.joining("_"))
                        + "',(SELECT id FROM castle WHERE full_name='" + castle + "'),null,"
                        + -1 + "," + Arrays.stream(items, 0, 7).collect(Collectors.joining(",")) + ")")
                .collect(Collectors.joining(",\n")) + ";\n");
    }

    private int findLine(List<String> lines, String word) {
        return IntStream.range(0, lines.size()).filter(i -> word.equalsIgnoreCase(lines.get(i).trim())).findFirst().orElseThrow();
    }

    private List<String> readNonEmpty(List<String> lines, int idx) {
        int emptyIdx = IntStream.range(idx, lines.size()).filter(i -> lines.get(i).trim().isEmpty()).findFirst().orElseThrow(() -> new IllegalArgumentException("" + idx));
        return lines.subList(idx + 1, emptyIdx);
    }

    private Map<String, List<String>> readDwellings(List<String> lines) {
        int idx = findLine(lines, "Dwellings");
        int[] idxs = Arrays.stream(ObjectNumberConstants.CASTLES).mapToInt(castle -> findLine(lines.subList(idx, lines.size()), castle) + idx).toArray();
        return IntStream.range(0, idxs.length).boxed().collect(Collectors.toMap(i -> ObjectNumberConstants.CASTLES[i], i -> readNonEmpty(lines, idxs[i])));
    }

    private List<String> readStandard(List<String> lines) {
        int idx = findLine(lines, "Neutral Buildings");
        return readNonEmpty(lines, idx);
    }

    private Map<String, List<String>> readSpecific(List<String> lines) {
        int[] idxs = Arrays.stream(ObjectNumberConstants.CASTLES).mapToInt(castle -> findLine(lines, castle)).toArray();
        return IntStream.range(0, idxs.length).boxed().collect(Collectors.toMap(i -> ObjectNumberConstants.CASTLES[i], i -> readNonEmpty(lines, idxs[i])));
    }
}
