package com.example.administrator.healthmonitor;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class HeartRateFragment extends Fragment {
    private TextView bpmView;
    private BufferArray[] mbuffer= new BufferArray[10];
    int[] bpmdata=new int[20];
    private File file;
    private Button anabtn;
    private Button btnStart,btnExit;
    private SurfaceView sfv;
    private EditText timetext;
    private EditText hourtext;
    private Paint mPaint;

    ClsOscilloscope clsOscilloscope=new ClsOscilloscope();
    static final int xMax = 16;//X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
    static final int xMin = 8;//X轴缩小比例最小值
    static final int yMax = 10;//Y轴缩小比例最大值
    static final int yMin = 1;//Y轴缩小比例最小值

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_heartrate, container,false);
        //按键
        btnStart = (Button) v.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnExit = (Button) v.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        //画板和画笔
        sfv = (SurfaceView) v.findViewById(R.id.SurfaceView01);
        sfv.setOnTouchListener(new TouchEvent());
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);// 画笔为绿色
        mPaint.setStrokeWidth(1);// 设置画笔粗细
        //示波器类库
        clsOscilloscope.initOscilloscope(xMax/2, yMax/2, sfv.getHeight()/2);
        //预设时间栏
        timetext=(EditText)v.findViewById(R.id.timeText);
        hourtext=(EditText)v.findViewById(R.id.hourText);
        file=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"data.txt" );
        anabtn=(Button)v.findViewById(R.id.analysis_button);
        bpmView=(TextView)v.findViewById(R.id.bpmview);
        for(int i=0;i<10;i++) {
            mbuffer[i] = new BufferArray();
            mbuffer[i].SetId(i);
        }
        anabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(file);
            }
        });
        return v;
    }

        private void getData(File file) {
        int p = 0; //波峰的个数
        int R_count = 0; //R峰的个数
        int id; //使用缓存的id
        int bpm;
        int start = 0; //接收数据的长度
        int count=0;
        int[] Peaks = new int[300];
        int[] R_Peaks = new int[50];
        String s;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            id=getUsefulBuffer();
            mbuffer[id].isUsed=true;
            while((s=br.readLine())!=null){
                    mbuffer[id].data[start] = Double.parseDouble(s);
                    //bpmView.setText(String.valueOf(mbuffer[id].data[start]) + " " + String.valueOf(start));
                if (start >= 2) {
                    if (mbuffer[id].data[start] == mbuffer[id].data[start - 1])
                        mbuffer[id].data[start - 1] = (mbuffer[id].data[start] +mbuffer[id].data[start - 2]) / 2;
                    if (mbuffer[id].data[start - 2] < mbuffer[id].data[start - 1] && mbuffer[id].data[start] < mbuffer[id].data[start - 1]) {
                        Peaks[p] = start - 1;
                        if (mbuffer[id].data[Peaks[p]] > 700) {
                            R_Peaks[R_count] = Peaks[p];
                            R_count++;
                        }
                        p++;
                    }
                    if (mbuffer[id].data[start - 2] > mbuffer[id].data[start - 1] && mbuffer[id].data[start] >mbuffer[id].data[start - 1]) {
                        Peaks[p] = start - 1;
                        p++;
                    }
                }
                for (int i = 1; i < p - 1; i++) {
                    if (Math.abs(mbuffer[id].data[Peaks[i]] - mbuffer[id].data[Peaks[i - 1]]) < 5 && Math.abs(mbuffer[id].data[Peaks[i]] - mbuffer[id].data[Peaks[i + 1]]) < 5) {
                        mbuffer[id].data[Peaks[i]] = (mbuffer[id].data[Peaks[i - 1]] + mbuffer[id].data[Peaks[i + 1]]) / 2;
                        for (int j = Peaks[i - 1] + 1; j < Peaks[i]; j++) {
                            mbuffer[id].data[j] = (mbuffer[id].data[j - 1] + mbuffer[id].data[Peaks[i]]) / 2;
                        }
                        for (int j = Peaks[i] + 1; j < Peaks[i + 1]; j++) {
                            mbuffer[id].data[j] = (mbuffer[id].data[j - 1] + mbuffer[id].data[Peaks[i + 1]]) / 2;
                        }
                        if (Math.abs(mbuffer[id].data[Peaks[i - 1]]) >= Math.abs(mbuffer[id].data[Peaks[i + 1]])) {
                            for (int j = i + 1; j < p - 1; j++)
                                Peaks[j - 1] = Peaks[j + 1];
                            p = p - 2;
                        } else if (Math.abs(mbuffer[id].data[Peaks[i - 1]]) < Math.abs(mbuffer[id].data[Peaks[i + 1]])) {
                            for (int j = i; j < p - 1; j++)
                                Peaks[j - 1] = Peaks[j + 1];
                            p = p - 2;
                        }
                        if (i > 1)
                            i = i - 2;
                        else
                            i = i - 1;
                    } else if (Math.abs(mbuffer[id].data[Peaks[i]] -mbuffer[id].data[Peaks[i - 1]]) < 5 && Math.abs(mbuffer[id].data[Peaks[i]] - mbuffer[id].data[Peaks[i + 1]]) > 5) {
                        mbuffer[id].data[Peaks[i]] = (mbuffer[id].data[Peaks[i - 1]] + mbuffer[id].data[Peaks[i + 1]]) / 2;
                        for (int j = Peaks[i - 1] + 1; j < Peaks[i]; j++) {
                            mbuffer[id].data[j] = (mbuffer[id].data[j - 1] + mbuffer[id].data[Peaks[i]]) / 2;
                        }
                        for (int j = Peaks[i] + 1; j < Peaks[i + 1]; j++) {
                            mbuffer[id].data[j] = (mbuffer[id].data[j - 1] + mbuffer[id].data[Peaks[i + 1]]) / 2;
                        }
                        for (int j = i; j < p - 1; j++)
                            Peaks[j - 1] = Peaks[j + 1];
                        p = p - 2;
                        if (i > 1)
                            i = i - 2;
                        else
                            i = i - 1;
                    }
                }
                start++;
               if(start==300){
                   int m=id;
                   if((bpm=analysisData(mbuffer[id].data,R_Peaks,R_count))!=0) {
                       bpmdata[count] = bpm;
                       count++;
                       if(count==20){
                           clsOscilloscope.Receive(bpmdata);
                           /*for(int j=0;j<count;j++)
                               bpmdata[j]=0;*/
                           bpmdata=new int[20];
                           count=0;
                       }
                   }
                   start=0;
                   p = 0;
                   R_count = 0;
                   Peaks = new int[300];
                   R_Peaks = new int[50];
                   id=getUsefulBuffer();
                   mbuffer[id].isUsed=true;
                   mbuffer[m].ClearData();
               }
            }
        }catch (IOException e){e.printStackTrace();}
    }

        private int analysisData ( double[] data, int[] R_Peaks, int R_count){
            int bpm = 0;            //每分钟心跳数
            int[] R_interval = new int[50];
            int frequency = 100;  //采样频率
            double ave_interval = 0;//max_interval,min_interval;       //R峰间的平均距离
            for (int i = 0; i < R_count - 1; i++) {
                R_interval[i] = R_Peaks[i + 1] - R_Peaks[i];
            }
            for (int i = 0; i < R_count - 1; i++) {
                ave_interval += R_interval[i];
            }
            ave_interval = (ave_interval / frequency) * 1000;
            bpm = (int )(60000 * (R_count - 1) / ave_interval);
            //bpmView.setText(String.valueOf(bpm));
            if(bpm!=0) {
                writeBPM(String.valueOf(bpm));
            }
            return bpm;
        }

        private int getUsefulBuffer (){
            int i;
            for (i = 0; i < mbuffer.length; i++) {
                if (!mbuffer[i].isUsed())
                    break;
            }
            return mbuffer[i].id;
        }

        private void writeBPM(String bpm){
            String sendText;
            sendText=bpm+"\r\n";
            try{
                File file=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"BPM.txt");
                FileWriter fw=new FileWriter(file,true);
                BufferedWriter bw=new BufferedWriter(fw);
                bw.write(sendText);
                bw.flush();
                bw.close();
            }catch(IOException e){e.printStackTrace();}
        }
    /**
     * 按键事件处理
     * @author GV
     *
     */
        class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnStart) {
                clsOscilloscope.baseLine=sfv.getHeight()/2;
                clsOscilloscope.Start(sfv,mPaint,String.valueOf(timetext.getText()),String.valueOf(hourtext.getText()));
            } else if (v == btnExit) {
                clsOscilloscope.Stop();
            }
        }
    }
    /**
     * 触摸屏动态设置波形图基线
     * @author GV
     *
     */
        class TouchEvent implements OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            clsOscilloscope.baseLine=(int)event.getY();
            return true;
        }

    }

}
