package jiudianlianxian.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import jiudianlianxian.myapplication.view.TouPullView;

public class MainActivity extends AppCompatActivity {
    //按下的点
    private float touchEventY = 0;
    //Y方向上最大移动600
    private static final float TOUCH_MOVE_MAX_Y = 600;
    //自定义view
    private TouPullView touPullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touPullView = (TouPullView) findViewById(R.id.activity_main_toupullView);
        findViewById(R.id.activity_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取意图
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchEventY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //当前点的坐标
                        float y = event.getY();
                        if (y >= touchEventY) {//往向下拉
                            //向下拉动的距离
                            float moveSize = y - touchEventY;
                            //进度运算
                            float progress = moveSize >= TOUCH_MOVE_MAX_Y ? 1 : moveSize / TOUCH_MOVE_MAX_Y;
                            touPullView.setProgress(progress);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        touPullView.release();
                        break;
                }
                return true;
            }
        });
    }
}
