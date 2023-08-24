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

import com.example.a310_rondayview.ui.InterestedEventsAdapter;

import java.util.ArrayList;

public class InterestedEventsFragment extends Fragment {

    // the fragment initialization parameters
    private ArrayList<Event> eventsArrayList;

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
        // setup the recycler view
        super.onViewCreated(view, savedInstanceState);
        dataInitialise();
        RecyclerView recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);

        // populate the recycler view
        InterestedEventsAdapter interestedEventsAdapter = new InterestedEventsAdapter(getContext(), eventsArrayList);
        recyclerview.setAdapter(interestedEventsAdapter);
        interestedEventsAdapter.notifyDataSetChanged();
    }

    //ToDo Implement populating the favourites list with database events
    private void dataInitialise() {
        // get events from the database
        eventsArrayList = new ArrayList<>();
        int[] eventsImages = new int[] {
                // get list of images
        };
        // get list of titles
        String[] eventsTitles = new String[]{
                // get list of titles
        };
        String[] eventsDescriptions = new String[] {
                // get list of descriptions
        };

        // add events to the favourites list
        for (int i = 0; i < eventsTitles.length; i++) {
            Event event = new Event(); // populate each event with fields from corresponding indices in the arrays
            eventsArrayList.add(event);
        }
    }
}
