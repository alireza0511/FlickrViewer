package com.jazzb.alireza.malauzai_test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public SimpleFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return this.fragments.get(position);
//        if (position == 0) {
//            return new FirstFragment();
//        } else if (position == 1) {
//
//            return new SecondFragment();
//        } else if (position == 2) {
//
//            return new ThirdFragment();
//        } else if (position == 3) {
//
//            return new ForthFragment();
//        } else {
//
//            return new FifthFragment();
//        }
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
