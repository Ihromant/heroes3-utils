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
import ua.ihromant.sod.utils.map.RiverType;
import ua.ihromant.sod.utils.map.RoadType;

import java.io.IOException;
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
        Element wrapper = doc.addElement("div").addAttribute("class", "adv-map-content")
                .addAttribute("style", "width:" + (size * 32 + 320) + "px;height:" + (size * 32 + 320) + "px;");
        Element background = wrapper.addElement("div").addAttribute("class", "vertical").addAttribute("style", "position:absolute;");
        Element roads = wrapper.addElement("div").addAttribute("style", "position:absolute;");
        Element rivers = wrapper.addElement("div").addAttribute("style", "position:absolute;");
        Element top = background.addElement("div").addAttribute("class", "horizontal");
        top.addElement("img").addAttribute("src", "img/border/top_left.png");
        for (int i = 0; i < size; i++) {
            top.addElement("img").addAttribute("src", "img/border/top_" + (i % 4) + ".png");
        }
        top.addElement("img").addAttribute("src", "img/border/top_right.png");
        for (int i = 0; i < size; i++) {
            Element row = background.addElement("div").addAttribute("class", "horizontal");
            row.addElement("img").addAttribute("src", "img/border/left_" + (i % 4) + ".png");
            for (int j = 0; j < size; j++) {
                MapTile tile = meta.getTiles()[0][i][j];
                row.addElement("img")
                        .addAttribute("src", "img/tiles/" + tile.getTerrainType().toString().toLowerCase() + ".png")
                        .addAttribute("style", "width:32px;height:32px;object-position:0px "
                                + (-32 * tile.getTerrainSprite()) + "px;object-fit:cover;"
                                + evaluateTransform(tile.getMirroring(), 0));
                if (tile.getRiverType() != RiverType.NONE) {
                    rivers.addElement("img").addAttribute("src", "img/rivers/" + tile.getRiverType().toString().toLowerCase() + ".png")
                            .addAttribute("style", "position:absolute;"
                                    + "top:" + (32 * i + 32) + "px;left:" + (32 * j + 32) + "px;"
                                    + "width:32px;height:32px;object-position:0px "
                                    + (-32 * tile.getRiverSprite()) + "px;object-fit:cover;"
                                    + evaluateTransform(tile.getMirroring(), 2));
                }
                if (tile.getRoadType() != RoadType.NONE) {
                    roads.addElement("img").addAttribute("src", "img/roads/" + tile.getRoadType().toString().toLowerCase() + ".png")
                            .addAttribute("style", "position:absolute;"
                                    + "top:" + (32 * i + 48) + "px;left:" + (32 * j + 32) + "px;"
                                    + "width:32px;height:32px;object-position:0px "
                                    + (-32 * tile.getRoadSprite()) + "px;object-fit:cover;"
                                    + evaluateTransform(tile.getMirroring(), 4));
                }
            }
            row.addElement("img").addAttribute("src", "img/border/right_" + (i % 4) + ".png");
        }
        Element bottom = background.addElement("div").addAttribute("class", "horizontal");
        bottom.addElement("img").addAttribute("src", "img/border/bottom_left.png");
        for (int i = 0; i < size; i++) {
            bottom.addElement("img").addAttribute("src", "img/border/bottom_" + (i % 4) + ".png");
        }
        bottom.addElement("img").addAttribute("src", "img/border/bottom_right.png");

        wrapper.addElement("div").addAttribute("class", "grid")
                .addAttribute("style", "position:relative;width:" + (32 * size + 64) + "px;height:" + (32 * size + 64) + "px;");

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
