package com.example.administrator.healthmonitor;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
/**
 * Created by Administrator on 2017/12/26/026.
 */

public class SocketPacket {

    public void getbytestream(Socket client,int command,String filename,int length) throws UnsupportedEncodingException{
        int BUFSIZE=8192;
        byte[] buf=new byte[BUFSIZE];
        try {
            DataOutputStream out=new DataOutputStream(client.getOutputStream());
            DataInputStream input = new DataInputStream(client.getInputStream());
            byte[] cmdbyte=intToByteArray(command);
            byte[] databyte=new byte[100];
            System.arraycopy(getBytes(filename.toCharArray()),0,databyte,0,getBytes(filename.toCharArray()).length);
            for(int i=getBytes(filename.toCharArray()).length;i<100;i++)
            databyte[i]=0;
            byte[] lengthbyte=intToByteArray(length);
            byte[] header=new byte[cmdbyte.length+lengthbyte.length+databyte.length];
            System.arraycopy(cmdbyte, 0, header, 0, cmdbyte.length);
            System.arraycopy(lengthbyte, 0, header, cmdbyte.length,lengthbyte.length);
            System.arraycopy(databyte, 0, header, cmdbyte.length+lengthbyte.length,databyte.length);
            out.write(header);
            out.flush();
            switch(command) {
                case 1:
                    //只发送报文不执行操作
                    break;
                case 3:
                    String filepath = Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"BPM.txt";
                    DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath)));
                    fis.read(buf);
                    out.write(buf);
                    out.flush();
                    out.close();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName ("UTF-8");
        CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);

        return bb.array();

    }
}
