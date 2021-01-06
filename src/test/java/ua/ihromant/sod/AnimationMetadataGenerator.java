package ua.ihromant.sod;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimationMetadataGenerator {
    private final List<String> castles = Stream.concat(Arrays.stream(Castle.values()).map(v -> v.name().toLowerCase()), Stream.of("neutrals")).collect(Collectors.toList());
    private final ObjectMapper map = new ObjectMapper();
    @Test
    public void generateAnimationMetadata() throws IOException {
        File root = new File("/home/ihromant/Games/units/units");
        for (String castle : castles) {
            File dir = new File(root, castle + "/units");
            for (File json : Objects.requireNonNull(dir.listFiles(f -> f.getName().endsWith(".json")))) {
                String name = json.getName().substring(0, json.getName().length() - 5);
                Creature.valueOf(name.toUpperCase());
                //new File(dir, name + ".dir").renameTo(new File(dir, name));
                Metadata meta = map.readValue(new InputStreamReader(new FileInputStream(json)), Metadata.class);
                String[] values = new String[22];
                meta.getSequences().forEach(s -> values[s.group] = String.valueOf(s.frames.size()));
                System.out.println("((SELECT id FROM creature WHERE full_name = '" + name.toUpperCase() + "')," + String.join(",", values) + "),");
            }
        }
    }

    private static class Metadata {
        private int format;
        private int type;
        private List<Data> sequences;

        public int getFormat() {
            return format;
        }

        public void setFormat(int format) {
            this.format = format;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<Data> getSequences() {
            return sequences;
        }

        public void setSequences(List<Data> sequences) {
            this.sequences = sequences;
        }
    }

    private static class Data {
        private List<String> frames;
        private int group;

        public List<String> getFrames() {
            return frames;
        }

        public void setFrames(List<String> frames) {
            this.frames = frames;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
    }

    @Test
    public void calculateBoundaries() {
        int[] dims = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/unitsizes.txt"))).lines()
                .map(line -> line.substring(line.indexOf('(') + 1, line.length() - 1).split(","))
                .map(lines -> Arrays.stream(lines).mapToInt(Integer::parseInt).toArray())
                .reduce((a, b) -> new int[] {Math.min(a[0], b[0]), Math.max(a[1], b[1]), Math.min(a[2], b[2]), Math.max(a[3], b[3])})
                .orElseThrow();
        System.out.println(dims[0] + "," + dims[1] + "," + dims[2] + "," + dims[3]);
    }
}
