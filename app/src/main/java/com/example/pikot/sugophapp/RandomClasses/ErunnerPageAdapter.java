package com.example.pikot.sugophapp.RandomClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.pikot.sugophapp.Erunner.ErunnerHistoryFragment;
import com.example.pikot.sugophapp.Erunner.ErunnerSettings;


public class ErunnerPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public ErunnerPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs= numOfTabs;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new ErunnerHistoryFragment();
            case 1:
                return new ErunnerSettings();

            default:
                return null;


        }
    }



    @Override
    public int getCount() {
        return numOfTabs;
    }
}
