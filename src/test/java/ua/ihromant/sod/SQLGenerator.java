package ua.ihromant.sod;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SQLGenerator {
    @Test
    public void generate() {
        AtomicInteger counter = new AtomicInteger();
        new BufferedReader(new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream("/castle.txt")))).lines()
                .filter(StringUtils::isNotBlank)
                .map(l -> l.split("\\s+"))
                .forEach(chunks -> {
                    System.out.print("(");
                    System.out.print(counter.getAndIncrement() + ",");
                    System.out.print("'" + chunks[0] + "',");
                    System.out.print(Castle.valueOf(chunks[4].toUpperCase()).ordinal() + ",");
                    System.out.print(chunks[6].charAt(0) + ",");
                    System.out.print((chunks[6].length() == 2 ? "true" : "false") + ",");
                    System.out.print(chunks[7] + ",");
                    System.out.print(chunks[8] + ",");
                    System.out.print(chunks[9] + ",");
                    System.out.print(chunks[10] + ",");
                    System.out.print(chunks[11] + ",");
                    System.out.print(chunks[12] + ",");
                    System.out.print(chunks[13] + ",");
                    System.out.print(chunks[14] + ",");
                    System.out.print(chunks[15]);
                    System.out.println("),");
                });
    }

    @Test
    public void reconvertResources() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/buildings.sql")));
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            if ("id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold".equals(line)) {
                Map<String, List<Integer>> resources = new LinkedHashMap<>();
                System.out.println("id, full_name, castle_id, parent_id, animation_id");
                System.out.println(reader.readLine().trim());
                while (!line.startsWith("insert into")) {
                    line = reader.readLine().trim();
                    if (line.startsWith("(")) {
                        int idx1 = line.indexOf('\'');
                        String building = line.substring(idx1 + 1, line.indexOf('\'', idx1 + 1));
                        int idxC2 = line.indexOf(',', idx1);
                        int idxC3 = line.indexOf(',', idxC2 + 1);
                        int idxC4 = line.indexOf(',', idxC3 + 1);
                        int idxC5 = line.indexOf(',', idxC4 + 1);
                        String resStr = line.substring(idxC5 + 1, line.lastIndexOf(')'));
                        resources.put(building, Arrays.stream(resStr.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
                        boolean end = line.endsWith(";");
                        System.out.print(line.substring(0, idxC5));
                        System.out.println(end ? ");" : "),");
                    }
                }
                System.out.println("insert into building_price (");
                System.out.println("id, building_id, resource_id, res_count");
                System.out.println(") values");
                int counter = 0;
                List<String> strings = new ArrayList<>();
                for (Map.Entry<String, List<Integer>> e : resources.entrySet()) {
                    for (int i = 0; i < ObjectNumberConstants.RESOURCES.length; i++) {
                        int resCount = e.getValue().get(i);
                        if (resCount != 0) {
                            strings.add("((SELECT MAX(id) + " + ++counter + " FROM building_price),"
                            + "(SELECT id FROM town_building WHERE full_name = '" + e.getKey() + "'),"
                            + "(SELECT id FROM resource WHERE full_name = '" + ObjectNumberConstants.RESOURCES[i] + "'),"
                            + resCount + ")");
                        }
                    }
                }
                for (int i = 0; i < strings.size(); i++) {
                    System.out.print(strings.get(i));
                    System.out.println(i == strings.size() - 1 ? ";" : ",");
                }
            } else {
                System.out.println(line);
                line = reader.readLine();
            }
        }
    }
}
