package ua.ihromant.sod;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteWrapper {
    private int position;
    private final LittleEndianDataInputStream str;

    public ByteWrapper(byte[] bytes) {
        this.str = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes));
    }

    public void seek(int position) throws IOException {
        str.reset();
        str.skipBytes(position);
        this.position = position;
    }

    public int readInt() throws IOException {
        position = position + 4;
        return str.readInt();
    }

    public String readString(int characters) throws IOException {
        position = position + characters;
        char[] nm = new char[characters];
        for (int k = 0; k < characters; k++) {
            nm[k] = (char) str.readByte();
        }
        return new String(nm);
    }

    public int readUnsigned() throws IOException {
        position = position + 1;
        return str.readUnsignedByte();
    }

    public int readRGB() throws IOException {
        position = position + 3;
        return (str.readUnsignedByte() << 16) | (str.readUnsignedByte() << 8) | str.readUnsignedByte();
    }

    public int readBGR() throws IOException {
        position = position + 3;
        return str.readUnsignedByte() | (str.readUnsignedByte() << 8) | (str.readUnsignedByte() << 16);
    }

    public byte[] readBytes(int size) throws IOException {
        position = position + size;
        byte[] bytes = new byte[size];
        str.read(bytes);
        return bytes;
    }

    public int[] readUnsigned(int size) throws IOException {
        position = position + size;
        int[] bytes = new int[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = str.readUnsignedByte();
        }
        return bytes;
    }

    public byte readByte() throws IOException {
        position = position + 1;
        return str.readByte();
    }

    public boolean readBoolean() throws IOException {
        return readUnsigned() > 0;
    }

    public int readUnsignedShort() throws IOException {
        position = position + 2;
        return str.readUnsignedShort();
    }

    public int getPosition() {
        return position;
    }
}
