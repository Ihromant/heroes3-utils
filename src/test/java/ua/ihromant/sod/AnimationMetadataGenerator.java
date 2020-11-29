package ua.ihromant.sod;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

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
}
