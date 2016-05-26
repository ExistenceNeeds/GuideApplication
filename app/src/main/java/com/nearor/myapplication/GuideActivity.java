package com.nearor.myapplication;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private static final int[] mImages = new int[]{
            R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private ViewPager vpGuide;
    private ArrayList<ImageView>  mImageViewList;
    private LinearLayout llPointGroup;//引导圆点的父控件
    private int mPointWidth;//圆点的距离
    private View viewRedPoint;//小红点
    private Button mNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        mNext = (Button) findViewById(R.id.btn_next);

        initViews();

        vpGuide.setAdapter(new GuideAapter());

        vpGuide.addOnPageChangeListener(new GuidePagerListen());

    }

    /**
     * 初始化界面
     */
    private void initViews(){
        mImageViewList = new ArrayList<>();

        //初始化三个背景页面
        for (int i = 0; i < mImages.length; i++) {
            ImageView ima = new ImageView(this);
            ima.setBackgroundResource(mImages[i]);//设置引导背景页面
            mImageViewList.add(ima);
        }

        // 初始化引导页的小圆点
        for (int i = 0; i < mImages.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);//设置引导页默认圆点

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this,10),DensityUtils.dp2px(this,10));

            if(i >0){
                params.leftMargin = DensityUtils.dp2px(this,10);//设置圆点间距
            }

            point.setLayoutParams(params);//设置圆点的大小

            llPointGroup.addView(point);//将圆点添加到线性布局

        }

        // 获取视图树, 对layout结束事件进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 当layout执行结束后回调此方法
                System.out.print("layout结束");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mPointWidth = llPointGroup.getChildAt(1).getLeft()
                            - llPointGroup.getChildAt(0).getLeft();
                    System.out.println("圆点距离:" + mPointWidth);
                }
            }
        });

    }

    /**
     * viewpager数据适配器
     */

    class GuideAapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * viewpager的滑动监听
     */

    class GuidePagerListen implements ViewPager.OnPageChangeListener {

        //滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // System.out.println("当前位置:" + position + ";百分比:" + positionOffset
            // + ";移动距离:" + positionOffsetPixels);
            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    viewRedPoint.getLayoutParams();//获取当前小圆点的布局参数

            params.leftMargin = len;//设置左边距
            viewRedPoint.setLayoutParams(params);//重新布局小圆点
        }

        //页面被选中
        @Override
        public void onPageSelected(int position) {

        }

        //滑动状态的变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
