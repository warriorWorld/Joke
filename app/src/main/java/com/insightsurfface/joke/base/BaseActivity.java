package com.insightsurfface.joke.base;/**
 * Created by Administrator on 2016/10/17.
 */

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.insightsurfface.joke.R;
import com.insightsurfface.joke.utils.ActivityPoor;
import com.insightsurfface.joke.widget.bar.TopBar;
import com.insightsurfface.joke.widget.toast.EasyToast;

import androidx.appcompat.app.AppCompatActivity;


/**
 * 作者：苏航 on 2016/10/17 11:56
 * 邮箱：772192594@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected TopBar baseTopBar;
    protected EasyToast baseToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        initUI();
        baseToast = new EasyToast(this);
        // 在oncreate里订阅
        ActivityPoor.addActivity(this);

//        PushAgent.getInstance(this).onAppStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            setFitSystemWindow(true);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
        }
    }

    protected void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        baseTopBar = (TopBar) findViewById(R.id.base_topbar);
        ViewGroup containerView = (ViewGroup) findViewById(R.id.base_container);
        LayoutInflater.from(this).inflate(getLayoutId(), containerView);

        baseTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onLeftClick() {
                topBarOnLeftClick();
            }

            @Override
            public void onRightClick() {
                topBarOnRightClick();
            }

            @Override
            public void onTitleClick() {
                topBarOnTitleClick();
            }
        });
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    protected abstract int getLayoutId();

    protected void hideBaseTopBar() {
        baseTopBar.setVisibility(View.GONE);
    }

    protected void hideBack() {
        baseTopBar.hideLeftButton();
    }

    protected void topBarOnLeftClick() {
        this.finish();
    }

    protected void topBarOnRightClick() {

    }

    protected void topBarOnTitleClick() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityPoor.finishSingleActivity(this);
    }
}
