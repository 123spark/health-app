package com.example.administrator.oscilloscope;

        import android.app.Activity;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.View.OnTouchListener;
        import android.widget.Button;
        import android.widget.ZoomControls;

public class testOscilloscope extends Activity {
    /** Called when the activity is first created. */
    Button btnStart,btnExit;
    SurfaceView sfv;
    ZoomControls zctlX,zctlY;

    ClsOscilloscope clsOscilloscope=new ClsOscilloscope();

    static final int xMax = 16;//X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
    static final int xMin = 8;//X轴缩小比例最小值
    static final int yMax = 10;//Y轴缩小比例最大值
    static final int yMin = 1;//Y轴缩小比例最小值

    Paint mPaint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_oscilloscope);
        //按键
        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnExit = (Button) this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        //画板和画笔
        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
        sfv.setOnTouchListener(new TouchEvent());
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);// 画笔为绿色
        mPaint.setStrokeWidth(1);// 设置画笔粗细
        //示波器类库
        clsOscilloscope.initOscilloscope(xMax/2, yMax/2, sfv.getHeight()/2);

        //缩放控件，X轴的数据缩小的比率高些
        zctlX = (ZoomControls)this.findViewById(R.id.zctlX);
        zctlX.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateX>xMin)
                    clsOscilloscope.rateX--;
                setTitle("X轴缩小"+String.valueOf(clsOscilloscope.rateX)+"倍"
                        +","+"Y轴缩小"+String.valueOf(clsOscilloscope.rateY)+"倍");
            }
        });
        zctlX.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateX<xMax)
                    clsOscilloscope.rateX++;
                setTitle("X轴缩小"+String.valueOf(clsOscilloscope.rateX)+"倍"
                        +","+"Y轴缩小"+String.valueOf(clsOscilloscope.rateY)+"倍");
            }
        });
        zctlY = (ZoomControls)this.findViewById(R.id.zctlY);
        zctlY.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateY>yMin)
                    clsOscilloscope.rateY--;
                setTitle("X轴缩小"+String.valueOf(clsOscilloscope.rateX)+"倍"
                        +","+"Y轴缩小"+String.valueOf(clsOscilloscope.rateY)+"倍");
            }
        });

        zctlY.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateY<yMax)
                    clsOscilloscope.rateY++;
                setTitle("X轴缩小"+String.valueOf(clsOscilloscope.rateX)+"倍"
                        +","+"Y轴缩小"+String.valueOf(clsOscilloscope.rateY)+"倍");
            }
        });

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
                clsOscilloscope.Start(sfv,mPaint);
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