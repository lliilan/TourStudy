package com.kl.tourstudy.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by KL on 2017/3/21 0021.
 */

public class ViewPagerAdapter extends PagerAdapter {

    //界面List
    private List<View> viewList;

    public ViewPagerAdapter(List<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        if (viewList != null){
            return viewList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //销毁position位置的界面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(viewList.get(position));
    }

    //初始化position位置的界面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewList.get(position), 0);
        return viewList.get(position);
    }
}

