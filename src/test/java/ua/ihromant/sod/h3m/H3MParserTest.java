package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.H3MParser;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class H3MParserTest {
    @Test
    public void testParserCorrectness() throws IOException {
        testFileName("FBA2018");
        testFileName("generated");
        testFileName("Metataxer");
        testFileName("Paragon");
        testFileName("Viking");
    }

    private void testFileName(String fileName) throws IOException {
        byte[] bytes = IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/" + fileName + ".h3m"))));
        new H3MParser().parse(bytes);
    }
}
