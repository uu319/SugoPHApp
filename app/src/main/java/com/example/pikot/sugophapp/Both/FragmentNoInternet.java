package com.example.pikot.sugophapp.Both;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pikot.sugophapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoInternet extends Fragment {


    public FragmentNoInternet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_no_internet, container, false);
    }

}
