package ua.ihromant.sod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.Objects;

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

    @Test
    public void rearrangePortraits() throws IOException {
        Map<String, Integer> shifts = Map.of("el", 136, "kn", 0, "pl", 128, "cl", 0);
        File file = new File("/home/ihromant/Games/units/images/zheroes");
        for (File port : Objects.requireNonNull(file.listFiles())) {
            String name = port.getName();
            String prefix = name.substring(0, 3);
            String numb = name.substring(3, 6);
            Integer idx = Character.isDigit(numb.charAt(0)) ? Integer.parseInt(name.substring(3, 6), 10) : null;
            String suffix = name.substring(6, 8);
            if (idx != null && idx < 128) {
                if (idx < 10 && !shifts.containsKey(suffix)) {
                    continue;
                }
                String heroName = ObjectNumberConstants.HEROES[idx + shifts.getOrDefault(suffix, 0)];
                if (prefix.equals("hps")) {
                    BufferedImage img = ImageIO.read(port);
                    BufferedImage res = new BufferedImage(img.getWidth() - 2, img.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
                    for (int i = 0; i < res.getWidth(); i++) {
                        for (int j = 0; j < res.getHeight(); j++) {
                            res.setRGB(i, j, img.getRGB(i + 1, j + 1));
                        }
                    }
                    ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/icons/46x30/hero", heroName.toLowerCase() + ".png"));
                }
            }
        }
        File mullich = new File("/home/ihromant/Games/units/images/zheroes/hps130kn.png");
        BufferedImage img = ImageIO.read(mullich);
        BufferedImage res = new BufferedImage(img.getWidth() - 2, img.getHeight() - 2, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < res.getWidth(); i++) {
            for (int j = 0; j < res.getHeight(); j++) {
                res.setRGB(i, j, img.getRGB(i + 1, j + 1));
            }
        }
        ImageIO.write(res, "png", new File("/home/ihromant/workspace/ihromant.github.io/img/icons/46x30/hero", "sir_mullich.png"));
    }
}
