package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.model.Event;

import java.util.List;

public class PopularEventAdaptor extends RecyclerView.Adapter<PopularEventAdaptor.PopularEventHolder>{

    private List<Event> eventList;
    private Context context;

    public PopularEventAdaptor(Context contextList, List<Event> eventList){
        this.eventList = eventList;
        this.context = contextList;
    }

    @NonNull
    @Override
    public PopularEventAdaptor.PopularEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_event_card, parent, false);
        return new PopularEventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularEventAdaptor.PopularEventHolder holder, int position) {
        Glide.with(context).load(eventList.get(position).getImageURL()).into(holder.popularEventImage);
        holder.titleTextView.setText(eventList.get(position).getTitle());
        holder.placingTextView.setText("#"+(position+1));
        holder.interestAmountTextView.setText(Integer.toString(eventList.get(position).getInterestCount()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class PopularEventHolder extends RecyclerView.ViewHolder {
        ImageView popularEventImage;
        TextView titleTextView;
        TextView placingTextView;
        TextView interestAmountTextView;
        public PopularEventHolder(@NonNull View itemView) {
            super(itemView);
            popularEventImage= itemView.findViewById(R.id.popularEventImage);
            titleTextView = itemView.findViewById(R.id.popularEventTitle);
            placingTextView = itemView.findViewById(R.id.popularEventPlacingText);
            interestAmountTextView = itemView.findViewById(R.id.interestedAmountText);
        }
    }
}
