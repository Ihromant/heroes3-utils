package ua.ihromant.sod;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLGenerator {
    @Test
    public void generate() {
        AtomicInteger counter = new AtomicInteger();
        new BufferedReader(new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream("/castle.txt")))).lines()
                .filter(StringUtils::isNotBlank)
                .map(l -> l.split("\\s+"))
                .forEach(chunks -> {
                    System.out.print("(");
                    System.out.print(counter.getAndIncrement() + ",");
                    System.out.print("'" + chunks[0] + "',");
                    System.out.print(Castle.valueOf(chunks[4].toUpperCase()).ordinal() + ",");
                    System.out.print(chunks[6].charAt(0) + ",");
                    System.out.print((chunks[6].length() == 2 ? "true" : "false") + ",");
                    System.out.print(chunks[7] + ",");
                    System.out.print(chunks[8] + ",");
                    System.out.print(chunks[9] + ",");
                    System.out.print(chunks[10] + ",");
                    System.out.print(chunks[11] + ",");
                    System.out.print(chunks[12] + ",");
                    System.out.print(chunks[13] + ",");
                    System.out.print(chunks[14] + ",");
                    System.out.print(chunks[15]);
                    System.out.println("),");
                });
    }
}
