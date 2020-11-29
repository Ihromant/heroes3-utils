package ua.ihromant.sod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class HeroDownloader {
    @Test
    public void download() throws IOException {
        Document doc = Jsoup.parse(new URL("https://heroes.thelazy.net/index.php/Hero_portraits"), 100000);
        System.out.println(doc.toString());
        Elements table = doc.getElementsByClass("sortable").get(0).children().get(0).children();
        System.out.println(table.size());
        for (int i = 2; i < table.size(); i++) {
            Element td = table.get(i).child(0);
            if (td.children().size() > 0) {
                String url = "https://heroes.thelazy.net" + td.child(0).child(0).attr("src");
                String name = table.get(i).child(2).child(0).text().replace(' ', '_').toLowerCase() + ".png";
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());

                FileOutputStream fileOutputStream = new FileOutputStream("/home/ihromant/heroes/" + name);

                fileOutputStream.getChannel()
                        .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            }
        }
    }
}
