package Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import Game.Debug;

public class Container {
    public Hashtable<Byte, Object> table;
    ByteArrayOutputStream          bao;
    DataOutputStream               dos;

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

    public static Container getFromBytes(byte[] rawData, int len) {
        Container c = new Container();
        ByteArrayInputStream bis = new ByteArrayInputStream(rawData, 0,len);
        DataInputStream dis = new DataInputStream(bis);
        
        try {
            Debug.Log("total available data is " + dis.available());
            while (dis.available() > 0) {
                byte param = dis.readByte();
                byte type = dis.readByte();
                byte length = dis.readByte();
                
                switch (type) {
                    case TYPE_CODE.BYTE:
                        c.table.put(param, dis.readByte());
                        break;
                    case TYPE_CODE.INT:
                        c.table.put(param, dis.readInt());
                        break;
                    case TYPE_CODE.FLOAT:
                        c.table.put(param, dis.readFloat());
                        break;
                    case TYPE_CODE.STRING: {
                        byte[] value = new byte[length];
                        dis.read(value, 0, length);
                        c.table.put(param, new String(value));
                    }
                        break;
                    case TYPE_CODE.BYTE_ARRAY: {
                        byte[] value = new byte[length];
                        dis.read(value, 0, length);
                        c.table.put(param, value);
                    }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return c;
    }

    public byte[] getBytes() {
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
