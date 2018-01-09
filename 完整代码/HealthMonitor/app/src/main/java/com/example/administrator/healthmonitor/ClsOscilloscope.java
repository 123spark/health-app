package com.example.administrator.healthmonitor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;


public class ClsOscilloscope {
    private ArrayList<int[]> inBuf = new ArrayList<int[]>();
    private boolean isReceving = false;//接收线程标记
    private boolean isRun = false;
    private boolean isStart = false;
    private int age;

    /**
     * X轴缩小的比例
     */
    public int rateX = 4;
    /**
     * Y轴缩小的比例
     */
    public int rateY = 4;
    /**
     * Y轴基线
     */
    public int baseLine = 0;
    /**
     * 初始化
     */
    public void initOscilloscope(int rateX, int rateY, int baseLine) {
        this.rateX = rateX;
        this.rateY = rateY;
        this.baseLine = baseLine;
    }
    /**
     * 开始
     */
    public void Start(SurfaceView sfv, Paint mPaint, String time) {
        isRun=true;
        isStart=true;
        new DrawThread(sfv, mPaint, time).start();// 开始绘制线程
        new RecevThread().start();
    }
    /**
     * 停止
     */
    public void Stop() {
        isReceving=false;
        isStart=false;
        inBuf.clear();// 清除
    }

    //接收用户年龄，用于绘制健康曲线
    public void Getage(int age){
        this.age=age;
    }

    public void Receive(int[] data){
        synchronized (inBuf){
            inBuf.add(data);
        }
    }

    /**
     * 负责接收数据到inBuf中
     *
     *
     */
    class RecevThread extends Thread{
        public void run(){
            if (inBuf.size() != 0) {
                isReceving = true;
            }
        }
    }

    /**
     * 负责绘制inBuf中的数据
     *
     *
     */
    class DrawThread extends Thread {
        private int oldX = 0;// 上次绘制的X坐标
        private int oldY = 0;// 上次绘制的Y坐标
        private SurfaceView sfv;// 画板
        private SurfaceHolder holder;
        private String time;//预设时间
        private String hour;
        private String hour1;
        private int X_index;// 当前画图所在屏幕X轴的坐标
        private Paint mPaint;// 画笔
        Paint p = new Paint();
        int HEIGHT;
        int WIDTH;
        final int X_OFFSET = 0;
        final int X_OFF = 60;
        int centerY;
        int LineX;
        private int cx;
        int TIME;
        int m;
        int m1;
        int mm;
        Calendar cal;

        public DrawThread(SurfaceView sfv, Paint mPaint,String time) {
            this.sfv = sfv;
            this.mPaint = mPaint;
            this.time=time;
        }
        public void run() {
            holder = sfv.getHolder();
            HEIGHT=sfv.getHeight();
            WIDTH=sfv.getWidth();
            centerY = HEIGHT /2;
            LineX=HEIGHT/9*8;
            mPaint.setColor(Color.GREEN);
            mPaint.setStrokeWidth(3);
            p.setColor(Color.RED);
            p.setStrokeWidth(5);
            X_index=X_OFF;
            TIME=Integer.parseInt(time);
            //初始化时间
            SimpleDateFormat M = new SimpleDateFormat("mm");
            String MM;
            MM=String.valueOf(M.format(new Date()));
            m=Integer.parseInt(MM);
            m1=m;
            mm=m;
            cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            if (cal.get(Calendar.AM_PM) == 0)
                hour = String.valueOf(cal.get(Calendar.HOUR));
            else
                hour = String.valueOf(cal.get(Calendar.HOUR)+12);
            hour1=hour;

            drawBack1(holder);
            for(;;) {
                while (isReceving) {
                    if(isStart) {
                        ArrayList<int[]> buf = new ArrayList<int[]>();
                        synchronized (inBuf) {
                            if (inBuf.size() != 0) {
                                buf = (ArrayList<int[]>) inBuf.clone();// 保存
                                inBuf.clear();// 清除
                            }
                            if (buf.size() == 0) {
                                continue;
                            }
                        }
                        drawBack(holder);
                        for (int i = 0; i < buf.size(); i++) {
                            int[] tmpBuf = buf.get(i);
                            //int m;
                            //for(int j=0;j<tmpBuf.length;j++)
                            //m=tmpBuf[j];
                            SimpleDraw(X_index, tmpBuf, rateY, baseLine/2, time);// 把缓冲区数据画出来
                            X_index = X_index + tmpBuf.length*(WIDTH-X_OFF)/(TIME*10);
                            if (X_index > sfv.getWidth()) {
                                X_index = X_OFF;
                                //timechange();
                                m=m+TIME;
                                drawBack(holder);
                            }
                        }
                        buf.clear();
                    }
                }

                //未接受数据时画正弦波形
                cx = X_OFF;
                oldX = X_OFF;
                oldY = centerY - (int) (150 * Math.sin((cx - X_OFF) * 2 * Math.PI / 300));
                while (isRun) {
                    if (isReceving) break;
                    else {
                        if(isStart) {
                            int cy = centerY - (int) (150 * Math.sin((cx - X_OFF) * 2 * Math.PI / 300));
                            Canvas canvas1 = holder.lockCanvas(new Rect(cx - 18, cy - 50, cx + 18, cy + 50));
                            canvas1.drawPoint(cx, cy, mPaint);
                            canvas1.drawLine(oldX, oldY, cx, cy, p);
                            oldX = cx;
                            oldY = cy;
                            cx = cx + 18;

                            holder.unlockCanvasAndPost(canvas1);

                            if (cx > WIDTH) {
                                drawBack1(holder);
                                cx = X_OFF;
                            }
                        }
                    }
                }
            }
        }
        /**
         * 绘制指定区域
         *
         * @param start
         *            X轴开始的位置(全屏)
         * @param buffer
         *            缓冲区
         * @param rate
         *            Y轴数据缩小的比例
         * @param baseLine
         *            Y轴基线
         */
        void SimpleDraw(int start, int[] buffer, int rate, int baseLine, String time) {
            int TIME=Integer.parseInt(time);
            if (start == X_OFF) {
                oldX = X_OFF;
                oldY = (LineX)/8*6;
            }

            int y;
            for (int i = 0; i < buffer.length; i++) {// 有多少画多少
                Canvas canvas = sfv.getHolder().lockCanvas(
                        new Rect(start-1*(WIDTH-X_OFF)/(TIME*10)+i*(WIDTH-X_OFF)/(TIME*10), 0, start + i*(WIDTH-X_OFF)/(TIME*10), sfv.getHeight()));// 关键:获取画布
                //出现问题则改为i+1
                int x = i*(WIDTH-X_OFF)/(TIME*10) + start;
                y = HEIGHT/9*7 - (buffer[i]-50)*(HEIGHT/9/10) ;// 调节缩小比例，调节基准线
                canvas.drawLine(oldX, oldY, x, y, mPaint);
                sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                oldX = x;
                oldY = y;
            }
        }


        void drawBack1(SurfaceHolder holder){
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStrokeWidth(3);

            Paint p1 = new Paint();
            p1.setColor(Color.WHITE);
            p1.setStrokeWidth(3);
            p1.setStyle ( Paint.Style.STROKE );
            p1.setAlpha(150);
            p1.setPathEffect (new DashPathEffect(new float [ ] { 15, 5 }, 0 ) ) ;

            Paint p2 = new Paint();
            p2.setColor(Color.WHITE);
            p2.setStrokeWidth(5);
            p2.setTextSize(30);//设置字体大小

            Paint p3 = new Paint();
            p3.setColor(Color.GREEN);
            p3.setStrokeWidth(6);
            p3.setStyle ( Paint.Style.STROKE );
            p3.setAlpha(100);
            p3.setPathEffect (new DashPathEffect(new float [ ] { 25, 8 }, 0 ) ) ;

            //绘制坐标轴
            canvas.drawLine(X_OFFSET, LineX, WIDTH, LineX, p);
            canvas.drawLine(X_OFF, 15, X_OFF, HEIGHT, p);
            //绘制虚线
            for(int i=1;i<=7;i++){
                canvas.drawLine(X_OFF, LineX-(LineX)/8*i, WIDTH, LineX-(LineX)/8*i, p1);
            }
            //绘制Y轴坐标
            for(int i=1;i<=7;i++){
                canvas.drawText(String.valueOf(120-10*i),X_OFF-60,(LineX)/8*i+10,p2);
            }
            //绘制健康曲线
            canvas.drawLine(X_OFF, LineX-416, WIDTH, LineX-416, p3); //65*6.4
            //canvas.drawLine(X_OFF, (220-age-50)*(HEIGHT/9/10), WIDTH, (220-age-50)*(HEIGHT/9/10), p3);
            //绘制时间轴
            for(int i=0;i<=4;i++){
                timemodify1(i);
                canvas.drawText(hour1+":"+String.valueOf(m1+Integer.parseInt(time)/4*i),X_OFF+(WIDTH-X_OFF)/4*i,LineX+40,p2);
            }
            m1=mm;
            holder.unlockCanvasAndPost(canvas);
            holder.lockCanvas(new Rect(0,0,0,0));
            holder.unlockCanvasAndPost(canvas);

        }

        void drawBack(SurfaceHolder holder){
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStrokeWidth(3);

            Paint p1 = new Paint();
            p1.setColor(Color.WHITE);
            p1.setStrokeWidth(3);
            p1.setStyle ( Paint.Style.STROKE );
            p1.setAlpha(150);
            p1.setPathEffect (new DashPathEffect(new float [ ] { 15, 5 }, 0 ) ) ;

            Paint p2 = new Paint();
            p2.setColor(Color.WHITE);
            p2.setStrokeWidth(5);
            p2.setTextSize(30);//设置字体大小

            Paint p3 = new Paint();
            p3.setColor(Color.GREEN);
            p3.setStrokeWidth(6);
            p3.setStyle ( Paint.Style.STROKE );
            p3.setAlpha(100);
            p3.setPathEffect (new DashPathEffect(new float [ ] { 25, 8 }, 0 ) ) ;

            //绘制坐标轴
            canvas.drawLine(X_OFFSET, LineX, WIDTH, LineX, p);
            canvas.drawLine(X_OFF, 15, X_OFF, HEIGHT, p);
            //绘制虚线
            for(int i=1;i<=7;i++){
                canvas.drawLine(X_OFF, LineX-(LineX)/8*i, WIDTH, LineX-(LineX)/8*i, p1);
            }
            //绘制Y轴坐标
            for(int i=1;i<=7;i++){
                canvas.drawText(String.valueOf(120-10*i),X_OFF-60,(LineX)/8*i+10,p2);
            }
            //绘制健康曲线
            canvas.drawLine(X_OFF, LineX-416, WIDTH, LineX-416, p3); //65*6.4
            //canvas.drawLine(X_OFF, (220-age-50)*(HEIGHT/9/10), WIDTH, (220-age-50)*(HEIGHT/9/10), p3);
            //绘制时间轴
            for(int i=0;i<=4;i++){
                timemodify(i);
                canvas.drawText(hour+":"+String.valueOf(m+Integer.parseInt(time)/4*i),X_OFF+(WIDTH-X_OFF)/4*i,LineX+40,p2);
            }
            holder.unlockCanvasAndPost(canvas);
            holder.lockCanvas(new Rect(0,0,0,0));
            holder.unlockCanvasAndPost(canvas);

        }

        void timemodify1(int i){
            int HOUR=Integer.parseInt(hour1);
            if(m1+TIME/4*i>=60){
                m1=m1-60;
                HOUR=HOUR+1;
                hour1=String.valueOf(HOUR);
            }
            if(HOUR>=24){
                HOUR=HOUR-24;
                hour1=String.valueOf(HOUR);
            }
        }

        void timemodify(int i){
            int HOUR=Integer.parseInt(hour);
            if(m+TIME/4*i>=60){
                m=m-60;
                HOUR=HOUR+1;
                hour=String.valueOf(HOUR);
            }
            if(HOUR>=24){
                HOUR=HOUR-24;
                hour=String.valueOf(HOUR);
            }
        }

        void timechange(){
            int HOUR=Integer.parseInt(hour);
            if(m+TIME>=60){
                m=m-60;
                HOUR=HOUR+1;
                hour=String.valueOf(HOUR);
            }
        }


    }


}
