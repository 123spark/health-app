package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class DataActivity extends AppCompatActivity {
    private BufferArray[] mbuffer= new BufferArray[10];
    private int age;
    private int oxyline1,oxyline2;
    int[] bpmdata=new int[10];
    private File file;
    private Button btnStart,btnExit,ScreenBtn;
    private SurfaceView sfv;
    private SurfaceHolder holder;
    private EditText timetext;
    private TextView quiethr,sportime,errhr;
    private Paint mPaint;
    private Paint p1;

    ClsOscilloscope clsOscilloscope=new ClsOscilloscope();
    static final int xMax = 16;//X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
    static final int xMin = 8;//X轴缩小比例最小值
    static final int yMax = 10;//Y轴缩小比例最大值
    static final int yMin = 1;//Y轴缩小比例最小值

    private Canvas canvas;
    //记录两个触屏点的坐标
    private int x1, x2, y1, y2;
    //倍率
    private float rate = 1;
    //记录上次的 ? ?
    private float oldRate = 1;
    //记录第一次触屏时线段的长 ?
    private float oldLineDistance;
    //判定是否头次多指触点屏幕
    private boolean isFirst = true;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        oxyline1=(int)((220-age)*0.6);
        oxyline2=(int)((220-age)*0.8); //有氧运动的心率上下限
        //按键

        btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnExit = (Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        ScreenBtn = (Button)findViewById(R.id.ScreenBtn);
        ScreenBtn.setOnClickListener(new ClickEvent());
        quiethr=(TextView)findViewById(R.id.quiethr2);
        sportime=(TextView)findViewById(R.id.sportime2);
        errhr=(TextView)findViewById(R.id.errhr2);
        //画板和画笔
        sfv = (SurfaceView)findViewById(R.id.SurfaceView01);
        sfv.setOnTouchListener(new TouchEvent());
        holder=sfv.getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);// 画笔为绿色
        mPaint.setStrokeWidth(1);// 设置画笔粗细
        p1 = new Paint();
        p1.setColor(Color.RED);
        p1.setStrokeWidth(2);
        p1.setStyle ( Paint.Style.STROKE );
        p1.setAlpha(110);
        p1.setPathEffect (new DashPathEffect(new float [ ] { 4, 3 }, 0 ) ) ;
        //示波器类库
        clsOscilloscope.initOscilloscope(xMax/2, yMax/2, sfv.getHeight()/2);
        //预设时间栏
        timetext=(EditText)findViewById(R.id.timeText);
        //anabtn=(Button)findViewById(R.id.analysis_button);
        for(int i=0;i<10;i++) {
            mbuffer[i] = new BufferArray();
            mbuffer[i].SetId(i);
        }

    }

    protected void onStart(){
        super.onStart();
        try{
            File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");
            FileReader fr=new FileReader(account);
            BufferedReader br=new BufferedReader(fr);
            age=Integer.valueOf(br.readLine().split(" ")[3]);
        }catch(IOException e){e.printStackTrace();}
        file=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"data.txt" );
        getData(file);
    }

    private void getData(File file) {
        int oxytime=0;        //有氧运动时间
        int p = 0; //波峰的个数
        int time=0;
        int R_count = 0; //R峰的个数
        int id; //使用缓存的id
        int bpm;
        int start = 0; //接收数据的长度
        int count=0;
        int[] Peaks = new int[600];
        int[] R_Peaks = new int[100];
        File sportfile=new File(Environment.getExternalStorageDirectory() + File.separator + "Heartrate" + File.separator+"sport.txt" );
        try{
            String content;
            FileReader sFR=new FileReader(sportfile);
            BufferedReader sBR=new BufferedReader(sFR);
            content=sBR.readLine();
            if(content==null){
                FileWriter sFW=new FileWriter(sportfile,false);
                BufferedWriter sBW=new BufferedWriter(sFW);
                sBW.write(String.valueOf(0));
                sBW.flush();
                sBW.close();
                oxytime=0;
            }else {
                oxytime = Integer.valueOf(content);
            }
        }catch(IOException e){e.printStackTrace();}
        String s;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            id = getUsefulBuffer();
            mbuffer[id].isUsed = true;
            for (; ; ) {
                if (time == 20)
                    break;
                else {
                    fr = new FileReader(file);
                    br = new BufferedReader(fr);
                    while ((s = br.readLine()) != null) {
                        mbuffer[id].data[start] = Double.parseDouble(s);
                        if(mbuffer[id].data[start]==0)
                            start--;
                        //bpmView.setText(String.valueOf(mbuffer[id].data[start]) + " " + String.valueOf(start));
                        if (start >= 2) {
                            if (mbuffer[id].data[start] == mbuffer[id].data[start - 1])
                                mbuffer[id].data[start - 1] = (mbuffer[id].data[start] + mbuffer[id].data[start - 2]) / 2;
                            if (mbuffer[id].data[start - 2] < mbuffer[id].data[start - 1] && mbuffer[id].data[start] < mbuffer[id].data[start - 1]) {
                                Peaks[p] = start - 1;
                                if (mbuffer[id].data[Peaks[p]] > 700) {
                                    R_Peaks[R_count] = Peaks[p];
                                    R_count++;
                                }
                                p++;
                            }
                            if (mbuffer[id].data[start - 2] > mbuffer[id].data[start - 1] && mbuffer[id].data[start] > mbuffer[id].data[start - 1]) {
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
                            } else if (Math.abs(mbuffer[id].data[Peaks[i]] - mbuffer[id].data[Peaks[i - 1]]) < 5 && Math.abs(mbuffer[id].data[Peaks[i]] - mbuffer[id].data[Peaks[i + 1]]) > 5) {
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
                        if (start == 600) {
                            int m = id;
                            if ((bpm = analysisData(mbuffer[id].data, R_Peaks, R_count)) != 0) {
                                if(bpm>=oxyline1&&bpm<=oxyline2){
                                    oxytime++;
                                }
                                bpmdata[count] = bpm;
                                count++;
                            }
                            start = 0;
                            p = 0;
                            R_count = 0;
                            Peaks = new int[600];
                            R_Peaks = new int[100];
                            id = getUsefulBuffer();
                            mbuffer[id].isUsed = true;
                            mbuffer[m].ClearData();
                        }
                    }
                    FileWriter sFW=new FileWriter(sportfile,false);
                    BufferedWriter sBW=new BufferedWriter(sFW);
                    sBW.write(String.valueOf(oxytime));
                    sBW.flush();
                    sBW.close();
                    clsOscilloscope.Receive(bpmdata);
                    int average=0;
                    for(int i=0;i<count;i++){
                        average+=bpmdata[i];
                    }
                    average/=count;
                    quiethr.setText(String.valueOf(average));
                    new NetworkThread(3).start();
                    count=0;
                    //clear(file);    //清空文件
                    bpmdata = new int[10];
                    time++;
                }
                sportime.setText(String.valueOf(oxytime));
            }
        } catch(IOException e){
                e.printStackTrace();
            }

    }

    private int analysisData ( double[] data, int[] R_Peaks, int R_count){
        int bpm = 0;            //每分钟心跳数
        int[] R_interval = new int[100];
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
                clsOscilloscope.Start(sfv,mPaint,String.valueOf(timetext.getText()));
            } else if (v == btnExit) {
                clsOscilloscope.Stop();
            } else if (v == ScreenBtn) {
                Intent i=new Intent(DataActivity.this,FullScreenActivity.class);
                startActivity(i);
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
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isFirst = true;
                oldRate = rate;
            } else {
                if (event.getPointerCount() > 1) {
                    x1 = (int) event.getX(0);
                    y1 = (int) event.getY(0);
                    x2 = (int) event.getX(1);
                    y2 = (int) event.getY(1);
                    if (event.getPointerCount() == 2) {
                        if (isFirst) {
                            //得到第一次触屏时线段的长 ?
                            oldLineDistance = (float) Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2));
                            isFirst = false;
                        } else {
                            //得到非第 ?  触屏时线段的长度
                            float newLineDistance = (float) Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2));
                            //获取本次的缩放比 ?
                            rate = oldRate * newLineDistance / oldLineDistance;
                        }
                    }
                }
            }

            canvas = holder.lockCanvas(new Rect(x1,y1,x2,y2));
            if (canvas != null) {
                //canvas.save();
                //canvas.scale(3f, 3f, Math.abs((x2-x1)/2), Math.abs((y2-y1)/2));
                //canvas.restore();
                //便于观察，这里绘制两个触点时形成的线 ?
                canvas.drawLine(x1, y1, x2, y2, p1);
                holder.unlockCanvasAndPost(canvas);
            }
            return true;
        }

    }



}
