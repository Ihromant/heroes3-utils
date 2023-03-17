package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.H3MReader;
import ua.ihromant.sod.utils.bytes.ByteWrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class H3MReaderTest {
    @Test
    public void testParserCorrectness() throws IOException {
        testFileName("FBA2018");
        testFileName("Generated6lm");
        testFileName("GeneratedJC");
        testFileName("Metataxer");
        testFileName("Paragon");
        testFileName("JaredHaret");
        testFileName("Viking");
        testFileName("Attestation");
        testFileName("TheWordOfGod");
        testFileName("TheRock");
        testFileName("ArmageddonsBlade");
        testFileName("Barbarian");
        testFileName("BarbaricCrusade");
        testFileName("BackToTheRoots");
        testFileName("MandateOfHeaven");
        testFileName("ArenaKonwentowa24");
    }

    private void testFileName(String fileName) throws IOException {
        new H3MReader().parse(new ByteWrapper(getUnzippedBytes(fileName)));
    }

    public static byte[] getUnzippedBytes(String fileName) throws IOException {
        return IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(H3MReaderTest.class.getResourceAsStream("/h3m/" + fileName + ".h3m"))));
    }

    @Test
    public void debug() throws IOException {
        //testFileName("ArmageddonsBlade");
        //testFileName("Generated6lm");
        testFileName("TestMap");
    }
}
