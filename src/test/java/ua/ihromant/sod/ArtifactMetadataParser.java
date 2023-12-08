package ua.ihromant.sod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArtifactMetadataParser {
    @Test
    public void parse() throws IOException {
        Document doc = Jsoup.parse(new URL("https://heroes.thelazy.net/index.php/List_of_artifacts"), 100000);
        System.out.println(doc.toString());
        Elements table = doc.getElementsByClass("sortable").get(0).child(1).children();
        System.out.println(table.size());
        for (int i = 1; i < table.size(); i++) {
            Elements tds = table.get(i).children();
            String name = tds.get(0).text().replace("'", "").replace(' ', '_').toUpperCase();
            ImageIO.write(ImageIO.read(new URL("https://heroes.thelazy.net" + tds.get(0).child(0).child(0).attr("src"))), "png", new File("/home/ihromant/artifacts/" + name.toLowerCase() + ".png"));
            String stVal = tds.get(1).text();
            boolean combo = "Combo".equalsIgnoreCase(stVal);
            SlotType sType = combo ? null : SlotType.valueOf(stVal.toUpperCase());
            String atVal = tds.get(2).text();
            ArtifactType aType = combo ? ArtifactType.COMBO : ArtifactType.valueOf(atVal.toUpperCase());
            int cost = Integer.parseInt(tds.get(3).text());
            String effects = tds.get(4).text();
            String pr = tds.get(5).text();
            String parent = pr.startsWith("Part of ") ? pr.substring(8).replace("'", "").replace(' ', '_').toUpperCase() : null;
            System.out.println("('" + name + "'," + (sType == null ? null : sType.ordinal()) + "," + aType.ordinal() + "," + cost + ","
                    + (parent == null ? null : "(SELECT id FROM artifact WHERE name = '" + parent + "')") + "),");
            System.out.println("((SELECT id FROM artifact WHERE name = '" + name + "')," + effects + "),");
        }
    }

    @Test
    public void split() {
        List<String> lines = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "/not_prepared_artifacts_sql.txt"))).lines().collect(Collectors.toList());
        IntStream.range(0, lines.size() / 2).forEach(i -> System.out.println(lines.get(2 * i)));
        System.out.println();
        IntStream.range(0, lines.size() / 2).forEach(i -> System.out.println(lines.get(2 * i + 1)));
    }

    @Test
    public void parseCreatures() throws IOException {
        Document doc = Jsoup.parse(new URL("https://heroes.thelazy.net/index.php/List_of_creatures"), 100000);
        //System.out.println(doc.toString());
        Elements table = doc.getElementsByTag("tr");
        //System.out.println(table.size());
        for (int i = 16; i < table.size(); i++) {
            Elements tds = table.get(i).children();
            //System.out.println(tds);
            String name = tds.get(0).text().replace("'", "").replace(' ', '_').toLowerCase();
            String cost = tds.get(11).text();
            System.out.println("((SELECT MAX(id) + " + (i - 15) + " FROM creature_price),(SELECT id from creature where full_name = '" + name + "'),(SELECT id from res_count_meta where full_name = '" + cost + "g')),");
//            ImageIO.write(ImageIO.read(new URL("https://heroes.thelazy.net" + tds.get(0).child(0).child(0).attr("src"))), "png", new File("/home/ihromant/artifacts/" + name.toLowerCase() + ".png"));
//            String stVal = tds.get(1).text();
//            boolean combo = "Combo".equalsIgnoreCase(stVal);
//            SlotType sType = combo ? null : SlotType.valueOf(stVal.toUpperCase());
//            String atVal = tds.get(2).text();
//            ArtifactType aType = combo ? ArtifactType.COMBO : ArtifactType.valueOf(atVal.toUpperCase());
//            int cost = Integer.parseInt(tds.get(3).text());
//            String effects = tds.get(4).text();
//            String pr = tds.get(5).text();
//            String parent = pr.startsWith("Part of ") ? pr.substring(8).replace("'", "").replace(' ', '_').toUpperCase() : null;
//            System.out.println("('" + name + "'," + (sType == null ? null : sType.ordinal()) + "," + aType.ordinal() + "," + cost + ","
//                    + (parent == null ? null : "(SELECT id FROM artifact WHERE name = '" + parent + "')") + "),");
//            System.out.println("((SELECT id FROM artifact WHERE name = '" + name + "')," + effects + "),");
        }
    }
}
