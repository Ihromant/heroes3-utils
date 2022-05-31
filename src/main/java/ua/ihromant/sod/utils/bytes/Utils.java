package ua.ihromant.sod.utils.bytes;

import java.util.stream.IntStream;

public class Utils {
    public static IntStream ones(int coded) {
        return IntStream.range(0, 32).filter(i -> ((1 << i) & coded) != 0);
    }

    public static IntStream zeros(int coded, int cap) {
        return IntStream.range(0, cap).filter(i -> ((1 << i) & coded) == 0);
    }
}
