package ua.ihromant.sod;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class LowerCaser {
    @Test
    public void lowerCaseSqlStrings() throws IOException {
        try (InputStream is = new FileInputStream("");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader br = new BufferedReader(isr)) {
            br.lines().forEach(l -> {
                String line = l;
                int i = l.indexOf('\'');
                while (i > 0) {
                    int j = l.indexOf('\'', i + 1);
                    line = line.substring(0, i + 1) + line.substring(i + 1, j).toLowerCase() + line.substring(j);
                    i = l.indexOf('\'', j + 1);
                }
                System.out.println(line);
            });
        }
    }

    @Test
    public void lowerCaseJavaStrings() throws IOException {
        try (InputStream is = new FileInputStream("");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader br = new BufferedReader(isr)) {
            br.lines().forEach(l -> System.out.println(l.toLowerCase()));
        }
    }
}
