package ua.ihromant.sod.utils.bytes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteWrapper {
    private final ByteBuffer str;

    public ByteWrapper(byte[] bytes) {
        this.str = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    public void seek(int position) {
        str.position(position);
    }

    public int readInt() {
        return str.getInt();
    }

    public String readString(int characters) {
        char[] nm = new char[characters];
        for (int k = 0; k < characters; k++) {
            nm[k] = (char) str.get();
        }
        return new String(nm);
    }

    public String readString() {
        return readString(readInt());
    }

    public int readUnsigned() {
        return str.get() & 0xFF;
    }

    public int readRGB() {
        return (readUnsigned() << 16) | (readUnsigned() << 8) | readUnsigned();
    }

    public int readBGR() {
        return readUnsigned() | (readUnsigned() << 8) | (readUnsigned() << 16);
    }

    public int[] readUnsigned(int size) {
        int[] bytes = new int[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = readUnsigned();
        }
        return bytes;
    }

    public byte readByte() {
        return str.get();
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        str.get(bytes);
        return bytes;
    }

    public boolean readBoolean() {
        return readUnsigned() > 0;
    }

    public int readUnsignedShort() {
        return str.getShort() & 0xFFFF;
    }

    public int[] readUnsignedShort(int size) {
        int[] shorts = new int[size];
        for (int i = 0; i < size; i++) {
            shorts[i] = readUnsignedShort();
        }
        return shorts;
    }

    public int[] readInt(int size) {
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            ints[i] = str.getInt();
        }
        return ints;
    }

    public int getPosition() {
        return str.position();
    }

    public int[] debug(int count) {
        int[] result = readUnsigned(count);
        str.position(str.position() - count);
        return result;
    }

    public int size() {
        return str.capacity();
    }
}
