package com.example.a310_rondayview.ui.detailed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.ui.interestedevents.InterestedEventsFragment;


public class FragmentDetailed extends Fragment {
    public FragmentDetailed() {
        // Required empty public constructor
    }
    private class ViewHolder {

        public ViewHolder(View view) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);



        return view;
    }



}