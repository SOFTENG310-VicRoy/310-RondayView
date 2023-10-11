package com.example.a310_rondayview.ui.detailed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.DatabaseService;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.adapter.InterestedEventsAdapter;
import com.example.a310_rondayview.ui.adapter.SimilarEventAdapter;

import java.util.List;

public class FragmentDetailed extends Fragment {
    private class ViewHolder {
        ImageView eventImage;
        ImageView backImage;
        TextView clubNameText;
        TextView eventNameText;
        ImageView profileImage;
        TextView locationText;
        TextView eventDateText;
        TextView eventDescText;
        RecyclerView similarEventRv;

        public ViewHolder(View view) {
            eventImage = view.findViewById(R.id.event_image);
            backImage = view.findViewById(R.id.back);
            clubNameText = view.findViewById(R.id.clubNameTextView);
            profileImage = view.findViewById(R.id.profileImageView);
            eventNameText = view.findViewById(R.id.event_name);
            eventDateText = view.findViewById(R.id.event_date);
            locationText = view.findViewById(R.id.locationtext);
            eventDescText = view.findViewById(R.id.event_desc);
            similarEventRv = view.findViewById(R.id.similar_events_rv);
        }
    }
    List<Event> similarEvents;
    CurrentEventSingleton currentEvent;
    ViewHolder vh;

    public FragmentDetailed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        vh = new ViewHolder(view);
        currentEvent = CurrentEventSingleton.getInstance();
        vh.clubNameText.setText(currentEvent.getCurrentEvent().getClubName());
        vh.eventNameText.setText(currentEvent.getCurrentEvent().getTitle());
        vh.eventDateText.setText(currentEvent.getCurrentEvent().getDateTime().toString());
        vh.locationText.setText(currentEvent.getCurrentEvent().getLocation());
        vh.eventDescText.setText(currentEvent.getCurrentEvent().getDescription());
        Glide.with(getContext()).load(currentEvent.getCurrentEvent().getImageURL()).into(vh.eventImage);
        Glide.with(getContext()).load(currentEvent.getCurrentEvent().getEventClubProfilePicture()).into(vh.profileImage);
        vh.backImage.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // setup the recycler view
        vh.similarEventRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        vh.similarEventRv.setHasFixedSize(true);
        // load in the all the  events from the database
        DatabaseService databaseService = new DatabaseService();
        databaseService.getAllEvents().thenAccept(events -> {
            similarEvents = events;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            SimilarEventAdapter similarEventAdapter = new SimilarEventAdapter(getContext(), similarEvents, fragmentManager);
            vh.similarEventRv.setAdapter(similarEventAdapter);
        });

        return view;
    }
}