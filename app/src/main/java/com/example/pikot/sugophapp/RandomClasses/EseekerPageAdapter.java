package com.example.pikot.sugophapp.RandomClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pikot.sugophapp.Eseeker.EseekerPostErrandCategoryFragment;
import com.example.pikot.sugophapp.Eseeker.EseekerMyErrandFragment;

public class EseekerPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public EseekerPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs= numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:

                return new EseekerMyErrandFragment();

            case 1:

                return new  EseekerPostErrandCategoryFragment();

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
