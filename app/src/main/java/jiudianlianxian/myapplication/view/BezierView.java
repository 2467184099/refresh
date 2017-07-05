package jiudianlianxian.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/6/22.
 */

public class BezierView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();
    Path path2 = new Path();

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        Paint paint1 = paint;
        //抗锯齿
        paint1.setAntiAlias(true);
        //抗抖动
        paint1.setDither(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(10);
        //初始化源贝塞尔曲线
        path2.cubicTo(20, 70, 50, 120, 70, 20);
        //初始化贝塞尔曲线
        new Thread(){
            @Override
            public void run() {
                try {
                    initBezier();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 初始化贝塞尔曲线
     */
    private void initBezier() throws InterruptedException {
        float[] pointX = new float[]{0, 20, 50, 70};
        float[] pointY = new float[]{0, 70, 120, 20};
        Path path1 = path;
        int fps = 10000;
        for (int i = 0; i <= fps; i++) {
            float progress = i / (float) fps;
            float x = calculateBezier(progress, pointX);
            float y = calculateBezier(progress, pointY);
            path1.lineTo(x, y);
            //刷新界面
            postInvalidate();
            Thread.sleep(5);
        }

    }

    /**
     * 计算某时刻贝塞尔的值
     *
     * @param t      时间 0-1
     * @param values 贝塞尔曲线点的集合
     * @return 贝塞尔所处的点
     */
    private float calculateBezier(float t, float... values) {
        final int len = values.length;
        //采用双重for循环
        for (int i = len - 1; i > 0; i--) {
            //外层
            for (int j = 0; j < i; j++) {
                //内层计算
                values[j] = values[j] + (values[j + 1] - values[j]) * t;
            }

        }

        //运算时结果保存在第一位，所以返回第一位
        return values[0];
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        canvas.drawPath(path2, paint);
        paint.setColor(Color.RED);
        canvas.drawPath(path, paint);
    }
}
