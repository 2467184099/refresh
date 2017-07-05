package jiudianlianxian.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/6/22.
 */

public class TestBezier extends View {
    //画笔
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ;
    //路径
    private final Path path = new Path();

    public TestBezier(Context context) {
        this(context, null);
    }

    public TestBezier(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestBezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        //一阶贝塞尔曲线
        Path path1 = path;
        path1.moveTo(10, 10);
        path1.lineTo(30, 30);

        //二阶贝塞尔曲线
        // path1.quadTo(500,0,700,300);
        //相对上一次结束的点的实现
        path1.rQuadTo(20, -30, 40, 0);
        path1.moveTo(40, 80);
        //三阶
        //path1.cubicTo(50, 60, 70, 120, 80, 80);
        path1.rCubicTo(10,-20,30,40,40,0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawPoint(60, 10, paint);
        canvas.drawPoint(50, 60, paint);
        canvas.drawPoint(70, 120, paint);

    }
}
