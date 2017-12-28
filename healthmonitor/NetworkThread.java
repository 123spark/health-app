package com.example.administrator.healthmonitor;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/27/027.
 */

public class NetworkThread extends Thread {
    Socket client;
    public void run(){
        try {
            client=new Socket("192.168.1.119",8884);
            Client myclient=new Client();
            myclient.SendFile(client);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
