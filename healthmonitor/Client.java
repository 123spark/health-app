package com.example.administrator.healthmonitor;

import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/26/026.
 */

public class Client {

    public int SendFile(Socket client){
        File datafile=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"BPM.txt" );
        int filelength=0;
        String filename="BPM.txt";
        try {
            try{
            filelength=(int)getFileSize(datafile);
            }catch (Exception e){
                e.printStackTrace();
            }
            SocketPacket sp=new SocketPacket();
            sp.getbytestream(client,3,filename,filelength);
        }catch(IOException e){e.printStackTrace();}
        return filelength;
    }

    public long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }
}
