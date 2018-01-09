package com.example.administrator.healthmonitor;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/26/026.
 */

public class Client {

    public void SendFile(Socket client){
        File datafile=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"BPM.txt" );
        int filelength=0;
        int ret=0;
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
