package ua.ihromant.sod;

import java.util.function.BiFunction;

public class ARGBColor {
    public static final int YELLOW = from(255, 255, 0);
    public static final int RED = from(255, 0, 0);
    public static final int WHITE = from(255, 255, 255);
    public static int parse(String from) {
        return from.length() == 7
                ? from(Integer.parseInt(from.substring(1, 3), 16), Integer.parseInt(from.substring(3, 5), 16), Integer.parseInt(from.substring(5, 7), 16))
                : from(Integer.parseInt(from.substring(1, 3), 16), Integer.parseInt(from.substring(3, 5), 16),
                Integer.parseInt(from.substring(5, 7), 16), Integer.parseInt(from.substring(7, 9), 16));
    }

    public static int from(int r, int g, int b, int a) {
        return (r << 16) | (g << 8) | b | (a << 24);
    }

    public static int from(int r, int g, int b) {
        return (r << 16) | (g << 8) | b | (0xff << 24);
    }

    public static int interpolate(int from, int to, double a) {
        return from(linear(getR(from), getR(to), a), linear(getG(from), getG(to), a), linear(getB(from), getB(to), a), linear(getA(from), getA(to), a));
    }

    public static int transparent(int from, double a) {
        return from(getR(from), getG(from), getB(from), round(a * getA(from)));
    }

    public static BiFunction<Integer, Double, Integer> interpolator(int to) {
        return (base, a) -> interpolate(base, to, a);
    }

    private static int linear(int from, int to, double a) {
        return round(from * (1 - a) + to * a);
    }

    private static int getR(int value) {
        return value >>> 16 & 0xff;
    }

    private static int getG(int value) {
        return value >>> 8 & 0xff;
    }

    private static int getB(int value) {
        return value & 0xff;
    }

    private static int getA(int value) {
        return value >>> 24 & 0xff;
    }

    private static String toHex(int from) {
        return from < 9 ? "0" + from
                : from < 15 ? "0" + (char) ('a' + from - 10) : Integer.toHexString(from);
    }

    public static String toString(int value) {
        int a = getA(value);
        if (a == 0xff) {
            return "#" + toHex(getR(value)) + toHex(getG(value)) + toHex(getB(value));
        } else {
            return "#" + toHex(getR(value)) + toHex(getG(value)) + toHex(getB(value)) + toHex(a);
        }
    }

    public static int round(double a) {
        return (int) (a + Math.signum(a) * 0.5);
    }
}
