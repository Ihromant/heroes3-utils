package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.H3MParser;
import ua.ihromant.sod.utils.ObjectClassConstants;
import ua.ihromant.sod.utils.H3MObjectType;
import ua.ihromant.sod.utils.ParserInterceptor;
import ua.ihromant.sod.utils.entities.Coordinate;
import ua.ihromant.sod.utils.entities.H3MObjectAttribute;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

public class H3MParserTest {
    @Test
    public void testParserCorrectness() throws IOException {
        testFileName("FBA2018");
        testFileName("Generated6lm");
        testFileName("GeneratedJC");
        testFileName("Metataxer");
        testFileName("Paragon");
        testFileName("Viking");
        testFileName("Attestation");
        testFileName("TheWordOfGod");
        testFileName("TheRock");
        testFileName("ArmageddonsBlade");
        testFileName("BarbaricCrusade");
        testFileName("BackToTheRoots");
        testFileName("ArenaKonwentowa24");
    }

    private void testFileName(String fileName) throws IOException {
        byte[] bytes = getUnzippedBytes(fileName);
        new H3MParser().parse(bytes, new ParserInterceptor(){});
    }

    public static byte[] getUnzippedBytes(String fileName) throws IOException {
        return IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(H3MParserTest.class.getResourceAsStream("/h3m/" + fileName + ".h3m"))));
    }

    @Test
    public void checkDataPresent() throws IllegalAccessException, IOException {
        Map<Integer, String> constants = constantsMap();
        Set<Integer> types = new HashSet<>();
        Set<String> defs = new HashSet<>();
        new H3MParser().parse(getUnzippedBytes("Generated6lm"), new ParserInterceptor() {
            @Override
            public void interceptObjectData(Coordinate coord, H3MObjectAttribute attr) {
                H3MObjectType type = attr.type();
                types.add(attr.getObjectClass());
                if (type == H3MObjectType.META_OBJECT_MONSTER && defs.add(attr.def())) {
                    System.out.println(attr);
                }
            }
        });
        System.out.println(types.size());
        types.stream()
                .sorted(Comparator.comparing(i -> H3MObjectType.objectClassToType(i).ordinal()))
                .forEach(i -> System.out.println(H3MObjectType.objectClassToType(i) + " -> " + constants.get(i)));
    }

    private Map<Integer, String> constantsMap() throws IllegalAccessException {
        TreeMap<Integer, String> result = new TreeMap<>();
        Field[] fields = ObjectClassConstants.class.getDeclaredFields();
        for (Field field : fields) {
            result.put((Integer) field.get(null), field.getName());
        }
        return result;
    }
}
