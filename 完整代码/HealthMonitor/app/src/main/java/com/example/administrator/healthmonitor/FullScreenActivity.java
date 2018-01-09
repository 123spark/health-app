package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

public class FullScreenActivity extends AppCompatActivity {

    Button btnStart,btnExit;
    SurfaceView sfv;
    SurfaceHolder holder;
    EditText timetext;
    Button ReturnBtn;
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

    Paint mPaint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        //按键
        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnExit = (Button) this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        //全屏按键
        ReturnBtn=(Button)this.findViewById(R.id.ReturnBtn);
        ReturnBtn.setOnClickListener(new ClickEvent());
        //画板和画笔
        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
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
        timetext=(EditText)this.findViewById(R.id.timeText);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
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
            } else if (v == ReturnBtn){
                //sfv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                Intent i=new Intent(FullScreenActivity.this,DataActivity.class);
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
