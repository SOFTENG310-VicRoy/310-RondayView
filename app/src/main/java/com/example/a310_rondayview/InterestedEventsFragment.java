package com.example.a310_rondayview;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.ui.Events;
import com.example.a310_rondayview.ui.InterestedEventsAdapter;

import java.util.ArrayList;

public class InterestedEventsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Events> eventsArrayList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int[] eventsImages;
    private String[] eventsTitles;
    private String[] eventsDescriptions;
    private RecyclerView recyclerview;


    public InterestedEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterestedEventsFragment newInstance(String param1, String param2) {
        InterestedEventsFragment fragment = new InterestedEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

            Events event = new Events(eventsImages[i], eventsTitles[i], eventsDescriptions[i]);
            eventsArrayList.add(event);
        }
    }
}
