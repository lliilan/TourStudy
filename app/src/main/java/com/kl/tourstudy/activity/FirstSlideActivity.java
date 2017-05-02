package com.kl.tourstudy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.ViewPagerAdapter;
import com.kl.tourstudy.util.PreferenceUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 第一次引导页
 * Created by KL on 2017/3/21 0021.
 */

    public class FirstSlideActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

        private ViewPager viewPager;
        private List<View> viewList;                //储存ViewPager中要使用到的View的集合
        private GestureDetector gestureDetector;    //手势监听类，监听滑动动作

        //引导图片资源
        private final int[] pics = {R.drawable.first1,R.drawable.first2,R.drawable.first3,R.drawable.first4};
        //底部点资源
        private ImageView[] dots;
        //记录当前选中位置
        private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置隐藏标题栏，设置全屏（即隐藏了状态栏）
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_first_slide);


        //获得引导页面的判断值，然后进行判断是否要进入引导界面
        boolean isShow = PreferenceUtil.getBoolean(FirstSlideActivity.this, PreferenceUtil.SHOW_FIRST_SLIDE);
        if (isShow){
            IntentMain();
        }else{
            viewList = new ArrayList<>();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            //给引导页面加载图片
            for (int i = 0;i<pics.length;i++){
                ImageView iv = new ImageView(FirstSlideActivity.this);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                InputStream is = getResources().openRawResource(pics[i]);
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
                iv.setBackgroundDrawable(bitmapDrawable);
                iv.setLayoutParams(params);
//                iv.setBackgroundResource(pics[i]);
                viewList.add(iv);
            }
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setOnPageChangeListener(this);

            initDots();
            slipToMain();
        }

    }

    /**
     * 初始化底部dot(点)
     */
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];
        for (int i = 0;i<pics.length;i++) {
            //得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setImageResource(R.drawable.dot1);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }
        currentIndex = 0;
        dots[currentIndex].setImageResource(R.drawable.dot2);//设置为白色，即选中状态
    }

    private void IntentMain(){
        startActivity(new Intent(FirstSlideActivity.this,NavigationActivity.class));
        finish();
    }

    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    private void setCurDot(int position) {
        if (position < 0 || position > pics.length - 1 || currentIndex == position) {
            return;
        }
        dots[position].setImageResource(R.drawable.dot2);
        dots[currentIndex].setImageResource(R.drawable.dot1);

        currentIndex = position;
    }

    /**
     * 判断是否跳转到MainActivity
     */
    private void slipToMain(){
        gestureDetector = new GestureDetector(FirstSlideActivity.this,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        if (currentIndex == (pics.length-1)){
                            if ((e1.getRawX() - e2.getRawX()) >= 0){
                                PreferenceUtil.setBoolean(FirstSlideActivity.this, PreferenceUtil.SHOW_FIRST_SLIDE, true);
                                IntentMain();
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
