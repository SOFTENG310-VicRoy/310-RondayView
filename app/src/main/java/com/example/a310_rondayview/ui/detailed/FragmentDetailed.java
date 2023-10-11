package com.example.a310_rondayview.ui.detailed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.model.CurrentEventSingleton;

public class FragmentDetailed extends Fragment {
    CurrentEventSingleton currentEvent;
    ViewHolder vh;

    public FragmentDetailed() {
        // Required empty public constructor
    }
    private class ViewHolder {
        ImageView eventImage;
        ImageView backImage;
        TextView clubNameText;
        TextView eventNameText;
        ImageView profileImage;
        TextView locationText;
        TextView eventDateText;
        TextView eventDescText;

        public ViewHolder(View view) {
            eventImage = view.findViewById(R.id.event_image);
            backImage = view.findViewById(R.id.back);
            clubNameText = view.findViewById(R.id.clubNameTextView);
            profileImage = view.findViewById(R.id.profileImageView);
            eventNameText = view.findViewById(R.id.event_name);
            eventDateText = view.findViewById(R.id.event_date);
            locationText = view.findViewById(R.id.locationtext);
            eventDescText = view.findViewById(R.id.event_desc);
        }
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
        return view;
    }
}