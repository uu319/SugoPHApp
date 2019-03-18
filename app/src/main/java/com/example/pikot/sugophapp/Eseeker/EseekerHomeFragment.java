package com.example.pikot.sugophapp.Eseeker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.EseekerPageAdapter;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class EseekerHomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EseekerPageAdapter eseekerPageAdapter;


    public EseekerHomeFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_eseeker_home, container, false);




        tabLayout= rootView.findViewById(R.id.tabs);


        eseekerPageAdapter = new EseekerPageAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        // Set up the ViewPager with the sections adapter.
        viewPager = rootView.findViewById(R.id.container);
        viewPager.setAdapter(eseekerPageAdapter);
        float temp=0;



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffesetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                eseekerPageAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(i);
                tabLayout.setScrollPosition(i, 0f, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



        return rootView;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SessionHandler.loginRequired(getActivity());
        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }

    }

}



