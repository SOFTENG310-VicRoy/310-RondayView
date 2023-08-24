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

import java.util.List;

public class InterestedEventsFragment extends Fragment {

    // the fragment initialization parameters
    private List<Event> eventsList;

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

        // load in the list of favourited events from the database here

        // setup the recycler view
        RecyclerView recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);

        // populate the recycler view
        InterestedEventsAdapter interestedEventsAdapter = new InterestedEventsAdapter(getContext(), eventsList);
        recyclerview.setAdapter(interestedEventsAdapter);
        interestedEventsAdapter.notifyDataSetChanged();
    }
}
