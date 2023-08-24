package com.example.a310_rondayview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.Event;
import com.example.a310_rondayview.ui.InterestedEventsAdapter;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class InterestedEventsFragment extends Fragment {

    // the fragment initialization parameters
    private ArrayList<Event> eventsArrayList;

    private int[] eventsImages;
    private String[] eventsTitles;
    private String[] eventsDescriptions;
    private RecyclerView recyclerview;


    public InterestedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interested_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialise();
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);

        Event fakeEvent1 = new Event("fakeId1", "WDCC", "Fake Event1", "Web dev club", "UoA", new Timestamp(new Date()), "https://firebasestorage.googleapis.com/v0/b/rondayview-872b4.appspot.com/o/eventImages%2FeventSpeedInterviews.png?alt=media&token=0d878d83-1308-487f-87f4-386f41a99e5f", "https://firebasestorage.googleapis.com/v0/b/rondayview-872b4.appspot.com/o/eventImages%2FSESA.png?alt=media&token=d16dda80-cc0b-4ea6-a4bb-4f9a02fc9fe4");
        eventsArrayList.add(fakeEvent1);
        Event fakeEvent2 = new Event("fakeId2", "SESA","Fake Event2", "Software engineering student association", "UoA", new Timestamp(new Date()), "https://firebasestorage.googleapis.com/v0/b/rondayview-872b4.appspot.com/o/eventImages%2FeventSpeedInterviews.png?alt=media&token=0d878d83-1308-487f-87f4-386f41a99e5f", "https://firebasestorage.googleapis.com/v0/b/rondayview-872b4.appspot.com/o/eventImages%2FSESA.png?alt=media&token=d16dda80-cc0b-4ea6-a4bb-4f9a02fc9fe4");
        eventsArrayList.add(fakeEvent2);

        InterestedEventsAdapter interestedEventsAdapter = new InterestedEventsAdapter(getContext(), eventsArrayList);
        recyclerview.setAdapter(interestedEventsAdapter);
        interestedEventsAdapter.notifyDataSetChanged();
    }

    private void dataInitialise() {
        eventsArrayList = new ArrayList<>();
        eventsImages = new int[] {
                // get list of images
        };
        eventsTitles = new String[] {
                // get list of titles
        };
        eventsDescriptions = new String[] {
                // get list of descriptions
        };

        for (int i = 0; i < eventsTitles.length; i++) {

            Event event = new Event(); // populate each event with fields from corresponding indices in the arrays
            eventsArrayList.add(event);
        }
    }
}
