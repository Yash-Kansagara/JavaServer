package Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class Container {
    Hashtable<Byte, Object> table;
    ByteArrayOutputStream   bao;
    DataOutputStream        dos;

    public class TYPE_CODE {
        public static final byte STRING     = 0;
        public static final byte INT        = 1;
        public static final byte BYTE       = 2;
        public static final byte FLOAT      = 3;
        public static final byte BYTE_ARRAY = 4;
    }

    public Container() {
        table = new Hashtable<>();
        bao = new ByteArrayOutputStream();
        dos = new DataOutputStream(bao);
    }
    
    public static Container getFromBytes(byte[] data){
        return new Container();
    }
    
    public byte[] getBytes(){
        return bao.toByteArray();
    }

    public void put(Byte key, String value) {
        byte[] data = value.getBytes();
        try {
            dos.write(new byte[]{key, TYPE_CODE.STRING, (byte)data.length});
            dos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(Byte key, int value) {

        try {
            dos.write(new byte[]{key, TYPE_CODE.INT, 4});
            dos.writeInt(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(Byte key, byte value) {
        try {
            dos.write(new byte[]{key, TYPE_CODE.BYTE, 1, value});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(Byte key, byte[] value) {
        try {
            dos.write(new byte[]{key, TYPE_CODE.BYTE_ARRAY, (byte)value.length});
            dos.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(Byte key, Float value) {
        try {
            dos.write(new byte[]{key, TYPE_CODE.BYTE_ARRAY, Float.SIZE});
            dos.writeFloat(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
