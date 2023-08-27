package ua.ihromant.sod;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.ObjectNumberConstants;

import java.io.File;
import java.io.IOException;

public class ImageGenerator {
    private static final String[] levels = {"basic", "advanced", "expert"};
    @Test
    public void generateSecSkills() throws IOException {
        for (int i = 0; i < ObjectNumberConstants.SECONDARY.length; i++) {
            for (int j = 0; j < levels.length; j++) {
                int cff = 3 * (i + 1) + j;
                FileUtils.copyFile(new File("/home/ihromant/Games/units/images-shadow/secsk32",
                                "00_" + (cff < 10 ? "0" + cff : String.valueOf(cff)) + ".png"),
                        new File("/home/ihromant/workspace/ihromant.github.io/img/icons/32x32/sec_skill",
                                ObjectNumberConstants.SECONDARY[i].name().toLowerCase() + "_" + levels[j] + ".png"));
            }
        }
    }
}
