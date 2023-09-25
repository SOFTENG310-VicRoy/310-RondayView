package com.example.a310_rondayview.ui.detailed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.model.CurrentEvent;


public class FragmentDetailed extends Fragment {
    CurrentEvent currentEvent;
    ViewHolder vh;

    public FragmentDetailed() {
        // Required empty public constructor
    }
    private class ViewHolder {
        ImageView eventImage;
        public ViewHolder(View view) {
            eventImage = view.findViewById(R.id.event_image);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        vh = new ViewHolder(view);
        currentEvent = CurrentEvent.getInstance();

        Glide.with(getContext()).load(currentEvent.getCurrentEvent().getImageURL()).into(vh.eventImage);


        return view;
    }



}