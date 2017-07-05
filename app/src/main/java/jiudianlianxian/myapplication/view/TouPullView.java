package jiudianlianxian.myapplication.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2017/6/22.
 */

public class TouPullView extends View {
    //画笔
    private Paint circlePaint = null;
    //圆的半径
    private float circleRadius = 50;
    //圆心的想x，y坐标
    private float circleRadiusX, circleRadiusY;
    //高度该变得进度值
    private float proGress;
    //可拖动的高度
    private int dragHegith = 400;
    //目标宽度
    private int targetWidth = 400;
    //贝塞尔曲线的画笔及路径
    private Paint bezierPaint;
    private Path path = new Path();
    //重心点最终高度，决定控制点的y坐标
    private int targetGravityHegith = 10;
    //角度变化0-135
    private int targeAangle = 105;
    private Interpolator interpolator = new DecelerateInterpolator();
    private Interpolator targeAangleInterpolator;


    public TouPullView(Context context) {
        this(context, null);
    }

    public TouPullView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /**
     * 初始化
     */
    private void init() {
        //初始化圆画笔
        circlePaint = new Paint();
        //设置抗锯齿
        circlePaint.setAntiAlias(true);
        //设置防抖动
        circlePaint.setDither(true);
        //设置填充方式
        circlePaint.setStyle(Paint.Style.FILL);
        //设置颜色
        circlePaint.setColor(Color.parseColor("#FF4081"));

        //初始化贝塞尔曲线画笔
        bezierPaint = new Paint();
        //设置抗锯齿
        bezierPaint.setAntiAlias(true);
        //设置防抖动
        bezierPaint.setDither(true);
        //设置填充方式
        bezierPaint.setStyle(Paint.Style.FILL);
        //设置颜色
        bezierPaint.setColor(Color.parseColor("#FF4081"));
        //切角路径插值器
        targeAangleInterpolator = PathInterpolatorCompat.create((2.0f * circleRadius) / dragHegith, 90.0f / targeAangle);

    }

    /**
     * 绘制圆形
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = canvas.save();
        float tranX = (getWidth() - getValueByLine(getWidth(), targetWidth, proGress)) / 2;
        canvas.translate(tranX, 0);
        //绘制圆
        canvas.drawCircle(circleRadiusX, circleRadiusY, circleRadius, circlePaint);
        //绘制贝塞尔曲线
        canvas.drawPath(path, bezierPaint);
        canvas.restoreToCount(count);
    }

    /**
     * 测量时触发
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度的意图或类型
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //高度的意图或类型
        int hegithMode = MeasureSpec.getMode(heightMeasureSpec);
        int hegith = MeasureSpec.getSize(heightMeasureSpec);
        //最小的要求
        int minWidth = (int) (2 * circleRadius + getPaddingLeft() + getPaddingRight());
        int minHeight = ((int) (dragHegith * proGress + 0.5f)) + getPaddingTop() + getPaddingBottom();
        int measureWidth, measureHeight;
        //宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            //确切的
            measureWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //最多的
            measureWidth = Math.min(minWidth, width);
        } else {
            measureWidth = minWidth;
        }
        //高度
        if (hegithMode == MeasureSpec.EXACTLY) {
            //确切的
            measureHeight = hegith;
        } else if (hegithMode == MeasureSpec.AT_MOST) {
            //最多的
            measureHeight = Math.min(minHeight, hegith);
        } else {
            measureHeight = minHeight;
        }
        //设置測量宽度和高度
        setMeasuredDimension(measureWidth, measureHeight);

    }

    /**
     * 当大小改变是触发
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //当高度变化是进行路径更新
        updatePathLayout();


    }

    //设置进度
    public void setProgress(float progress) {
        //给进度值赋值
        proGress = progress;
        //请求重新布局测量
        requestLayout();

    }

    /**
     * 更新我们路径的相关操作
     */
    private void updatePathLayout() {

        //获取进度值
        final float progress = interpolator.getInterpolation(proGress);
        //获取可绘制去与的宽度和高度
        final float w = getValueByLine(getWidth(), targetWidth, proGress);
        final float h = getValueByLine(0, dragHegith, proGress);
        //x轴对称参数 圆心的x坐标
        final float cPointX = w / 2;
        //圆的半径
        final float cRadius = circleRadius;
        //圆心的y坐标
        final float cPointY = h - cRadius;
        //控制点结束y的值
        final float endControly = targetGravityHegith;
        //更新圆的坐标
        circleRadiusX = cPointX;
        circleRadiusY = cPointY;
        //路径
        final Path path1 = path;
        path1.reset();
        path1.moveTo(0, 0);
        //左边部分结束点和控制点
        float lEndPointX, lEndPointY;
        float lControlPointX, lControlPointY;
        //获取当前切线的弧度
        float angle = targeAangle * targeAangleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle);
        float x = (float) (Math.sin(radian) * cRadius);
        float y = (float) (Math.cos(radian) * cRadius);
        //结束点
        lEndPointX = cPointX - x;
        lEndPointY = cPointY + y;
        //控制点的y轴变化
        lControlPointY = getValueByLine(0, endControly, progress);
        //控制点与结束点之间y的高度
        float tHeight = lEndPointY - lControlPointY;
        //控制点与x坐标距离
        float tWidth = (float) (tHeight / Math.tan(radian));
        lControlPointX = lEndPointX - tWidth;
        //贝塞尔曲线
        path1.quadTo(lControlPointX, lControlPointY, lEndPointX, lEndPointY);
        //链接到右边
        path1.lineTo(cPointX + (cPointX - lEndPointX), lEndPointY);
        //画右贝塞尔曲线
        path1.quadTo(cPointX + cPointX - lControlPointX, lControlPointY, w, 0);

    }

    //释放动画
    private ValueAnimator valueAnimator;

    public void release() {
        if (valueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(proGress, 0f);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object val = animation.getAnimatedValue();
                    if (val instanceof Float) {
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(proGress, 0f);

        }
        valueAnimator.start();
    }

    /**
     * 获取当前值
     *
     * @param start    启始值
     * @param end      结束值
     * @param progress 进度
     * @return 当前进度的值
     */
    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }
}
