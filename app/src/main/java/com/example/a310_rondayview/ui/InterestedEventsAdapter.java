package com.example.a310_rondayview.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.google.android.material.imageview.ShapeableImageView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class InterestedEventsAdapter extends RecyclerView.Adapter<InterestedEventsAdapter.InterestedEventsViewHolder> {

    Context context;
    ArrayList<Events> eventsArrayList;

    public InterestedEventsAdapter(Context context, ArrayList<Events> eventsArrayList) {
        this.context = context;
        this.eventsArrayList = eventsArrayList;
    }

    @androidx.annotation.NonNull
    @Override
    public InterestedEventsViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interested_events_entry, parent, false);
        return new InterestedEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull InterestedEventsViewHolder holder, int position) {
        Events events = eventsArrayList.get(position);
        holder.eventImageView.setImageResource(events.eventImage);
        holder.titleTextView.setText(events.eventTitle);
        holder.descriptionTextView.setText(events.eventDescription);
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public static class InterestedEventsViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView eventImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public InterestedEventsViewHolder(@NonNull View itemView){
            super(itemView);
            eventImageView = itemView.findViewById(R.id.coverImage);
            titleTextView = itemView.findViewById(R.id.titleText);
            descriptionTextView = itemView.findViewById(R.id.descriptionText);

        }
    }
}
