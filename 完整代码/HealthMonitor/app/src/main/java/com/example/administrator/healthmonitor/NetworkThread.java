package com.example.administrator.healthmonitor;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/27/027.
 */

public class NetworkThread extends Thread {
    Socket client;
    private int cmd;
    private String accinfo;

    NetworkThread(int cmd){
        this.cmd=cmd;
    }
    NetworkThread(int cmd,String s){
        this.cmd=cmd;
        this.accinfo=s;
    }
    public void run(){
        try {
            client=new Socket("192.168.1.134",8884);
            Client myclient=new Client();
            switch(cmd){
                case 1:
                    SocketPacket sp=new SocketPacket();
                    sp.getbytestream(client,1,accinfo,accinfo.length());
                case 3:
                    myclient.SendFile(client);
                    client.close();
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
