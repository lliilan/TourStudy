package com.kl.tourstudy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by KL on 2017/4/16 0016.
 */

/**
 * 主页面中Fragment的适配器
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String title[];

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> mFragments, String[] title) {
        super(fm);
        this.mFragments = mFragments;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
        return mFragments.get(position);
    }

    /**
     * 设置显示总共有3个页面显示
     */
    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * 获得页面标题
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}

