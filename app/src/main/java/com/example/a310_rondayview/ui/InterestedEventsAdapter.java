package com.example.a310_rondayview.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.Event;
import com.example.a310_rondayview.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InterestedEventsAdapter extends RecyclerView.Adapter<InterestedEventsAdapter.InterestedEventsViewHolder> {

    Context context;
    ArrayList<Event> eventsArrayList;

    public InterestedEventsAdapter(Context context, List<Event> eventsArrayList) {
        this.context = context;
        this.eventsArrayList = (ArrayList<Event>) eventsArrayList;
    }

    @androidx.annotation.NonNull
    @Override
    public InterestedEventsViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for individual list items
        View view = LayoutInflater.from(context).inflate(R.layout.interested_events_entry, parent, false);
        return new InterestedEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull InterestedEventsViewHolder holder, int position) {
        // Binds data to the UI elements in each list item
        Event event = eventsArrayList.get(position);

        Glide.with(holder.itemView.getContext()).load(event.getImageURL()).into(holder.eventImageView);
        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }


    // ViewHolder class to hold references to UI elements for a list item
    public static class InterestedEventsViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImageView;
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
