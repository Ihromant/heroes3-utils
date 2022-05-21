package ua.ihromant.sod.h3m;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.H3MParser;
import ua.ihromant.sod.utils.entities.MapMetadata;
import ua.ihromant.sod.utils.entities.MapTile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Objects;
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
    }

    private MapMetadata testFileName(String fileName) throws IOException {
        byte[] bytes = IOUtils.toByteArray(new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/h3m/" + fileName + ".h3m"))));
        return new H3MParser().parse(bytes);
    }

    @Test
    public void experiments() throws IOException {
        MapMetadata meta = testFileName("GeneratedJC");
        int size = meta.getTiles()[0].length;
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("div");
        root.addAttribute("class", "adv-map-content vertical");
        Element top = root.addElement("div").addAttribute("class", "horizontal");
        top.addElement("img").addAttribute("src", "img/border/top_left.png");
        for (int i = 0; i < size; i++) {
            top.addElement("img").addAttribute("src", "img/border/top_" + (i % 4) + ".png");
        }
        top.addElement("img").addAttribute("src", "img/border/top_right.png");
        for (int i = 0; i < size; i++) {
            Element row = root.addElement("div").addAttribute("class", "horizontal");
            row.addElement("img").addAttribute("src", "img/border/left_" + (i % 4) + ".png");
            for (int j = 0; j < size; j++) {
                MapTile tile = meta.getTiles()[0][i][j];
                row.addElement("img")
                        .addAttribute("src", "img/tiles/" + tile.getTerrainType().toString().toLowerCase() + ".png")
                        .addAttribute("style", "width:32px;height:32px;object-position:0px "
                                + (-32 * tile.getTerrainSprite()) + "px;object-fit:cover;" + evaluateTransform(tile.getMirroring(), 0));
            }
            row.addElement("img").addAttribute("src", "img/border/right_" + (i % 4) + ".png");
        }
        Element bottom = root.addElement("div").addAttribute("class", "horizontal");
        bottom.addElement("img").addAttribute("src", "img/border/bottom_left.png");
        for (int i = 0; i < size; i++) {
            bottom.addElement("img").addAttribute("src", "img/border/bottom_" + (i % 4) + ".png");
        }
        bottom.addElement("img").addAttribute("src", "img/border/bottom_right.png");
        OutputFormat format = OutputFormat.createPrettyPrint();
        HTMLWriter writer = new HTMLWriter(System.out, format);
        writer.write(doc);
        writer.flush();
    }

    private String evaluateTransform(int mirroring, int shift) {
        int bits = mirroring >>> shift;
        if ((bits & 3) == 0) {
            return "";
        }
        return "transform: scale(" + ((bits & 1) == 1 ? "-1" : "1") + "," + ((bits & 2) == 2 ? "-1" : "1") + ");";
    }
}
