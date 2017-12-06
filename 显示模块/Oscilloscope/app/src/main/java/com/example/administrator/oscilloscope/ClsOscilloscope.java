package com.example.administrator.oscilloscope;
        import java.util.ArrayList;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;


public class ClsOscilloscope {
    private ArrayList<short[]> inBuf = new ArrayList<short[]>();
    private boolean isReceving = false;//接收线程标记
    private boolean isRun = false;

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
    public void Start(SurfaceView sfv, Paint mPaint) {
        isRun=true;
        new DrawThread(sfv, mPaint).start();// 开始绘制线程
    }
    /**
     * 停止
     */
    public void Stop() {
        isRun=false;
        isReceving=false;
        inBuf.clear();// 清除
    }

    /**
     * 负责绘制inBuf中的数据
     *
     * @author GV
     *
     */
    class DrawThread extends Thread {
        private int oldX = 0;// 上次绘制的X坐标
        private int oldY = 0;// 上次绘制的Y坐标
        private SurfaceView sfv;// 画板
        private SurfaceHolder holder;
        private int X_index = 0;// 当前画图所在屏幕X轴的坐标
        private Paint mPaint;// 画笔
        Paint p = new Paint();
        int HEIGHT;
        int WIDTH;
        final int X_OFFSET = 0;
        int centerY;
        private int cx;

        public DrawThread(SurfaceView sfv, Paint mPaint) {
            this.sfv = sfv;
            this.mPaint = mPaint;
        }
        public void run() {
            holder = sfv.getHolder();
            HEIGHT=sfv.getHeight();
            WIDTH=sfv.getWidth();
            centerY = HEIGHT /2;
            mPaint.setColor(Color.GREEN);
            mPaint.setStrokeWidth(3);
            p.setColor(Color.RED);
            p.setStrokeWidth(5);

           for(;;) {
               Canvas canvasclear = holder.lockCanvas(new Rect(X_OFFSET, 15, WIDTH, HEIGHT));
               canvasclear.drawColor(Color.BLACK);// 清除背景
               holder.unlockCanvasAndPost(canvasclear);
               drawBack(holder);
              while (isReceving) {
                  ArrayList<short[]> buf = new ArrayList<short[]>();
                  synchronized (inBuf) {
                      if (inBuf.size() == 0) {
                          isReceving=false;
                          break;
                      }
                      buf = (ArrayList<short[]>) inBuf.clone();// 保存
                      inBuf.clear();// 清除
                  }
                  for (int i = 0; i < buf.size(); i++) {
                      short[] tmpBuf = buf.get(i);
                      SimpleDraw(X_index, tmpBuf, rateY, baseLine);// 把缓冲区数据画出来
                      drawBack(holder);
                      X_index = X_index + tmpBuf.length;
                      if (X_index > sfv.getWidth()) {
                          X_index = 0;
                      }
                  }
              }

              //未接受数据时画正弦波形
              canvasclear = holder.lockCanvas(new Rect(X_OFFSET, 15, WIDTH, HEIGHT));
              canvasclear.drawColor(Color.BLACK);// 清除背景
              holder.unlockCanvasAndPost(canvasclear);
              drawBack(holder);
              cx = 0;
              oldX = 0;
              oldY = centerY - (int) (150 * Math.sin((cx - 1) * 2 * Math.PI / 300));
              while (isRun) {
                  if (isReceving) break;
                  else {
                      int cy = centerY - (int) (150 * Math.sin((cx - 1) * 2 * Math.PI / 300));
                      Canvas canvas = holder.lockCanvas(new Rect(cx - 18, cy - 50, cx + 18, cy + 50));
                      canvas.drawPoint(cx, cy, mPaint);
                      canvas.drawLine(oldX, oldY, cx, cy, p);
                      oldX = cx;
                      oldY = cy;
                      cx = cx + 18;

                      holder.unlockCanvasAndPost(canvas);

                      if (cx > WIDTH) {
                          canvasclear = holder.lockCanvas(new Rect(X_OFFSET, 15, WIDTH, HEIGHT));
                          canvasclear.drawColor(Color.BLACK);// 清除背景
                          holder.unlockCanvasAndPost(canvasclear);
                          drawBack(holder);
                          cx = 0;
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
        void SimpleDraw(int start, short[] buffer, int rate, int baseLine) {
            if (start == 0) {
                oldX = 0;
                oldY = 0;
            }
            Canvas canvas = sfv.getHolder().lockCanvas(
                    new Rect(start, 0, start + buffer.length, sfv.getHeight()));// 关键:获取画布
            canvas.drawColor(Color.BLACK);// 清除背景
            int y;
            for (int i = 0; i < buffer.length; i++) {// 有多少画多少
                int x = i + start;
                y = buffer[i] / rate + baseLine;// 调节缩小比例，调节基准线
                canvas.drawLine(oldX, oldY, x, y, mPaint);
                oldX = x;
                oldY = y;
            }
            sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        }


        void drawBack(SurfaceHolder holder){
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStrokeWidth(3);

            //绘制坐标轴
            canvas.drawLine(X_OFFSET, centerY, WIDTH, centerY, p);
            canvas.drawLine(X_OFFSET, 15, X_OFFSET, HEIGHT, p);
            holder.unlockCanvasAndPost(canvas);
            holder.lockCanvas(new Rect(0,0,0,0));
            holder.unlockCanvasAndPost(canvas);

        }


    }


}
