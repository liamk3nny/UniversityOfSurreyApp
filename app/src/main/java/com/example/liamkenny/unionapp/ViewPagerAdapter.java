package com.example.liamkenny.unionapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liamkenny on 20/03/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment>  homeFragmentsList = new ArrayList<>();
    private final List<String> homeFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return homeFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return homeFragmentTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return homeFragmentTitles.get(position);
    }

    public void addFragment(Fragment fragment, String tite){
        homeFragmentsList.add(fragment);
        homeFragmentTitles.add(tite);
    }
}
