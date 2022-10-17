package ua.ihromant.sod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExperienceParser {
    @Test
    public void download() throws IOException {
        Document doc = Jsoup.parse(new URL("https://heroes.thelazy.net/index.php/Experience"), 100000);
        Element table = doc.getElementsByTag("table").get(0);
        System.out.println(table);
        boolean head = true;
        List<Integer> experiences = new ArrayList<>();
        for (Element row : table.child(0).children()) {
            if (head) {
                head = false;
                continue;
            }
            experiences.add(Integer.parseInt(row.child(1).text().replace(",", "")));
        }
        System.out.println(experiences);
    }
}
