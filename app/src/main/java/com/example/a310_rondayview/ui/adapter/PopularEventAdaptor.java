package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.detailed.FragmentDetailed;

import java.util.List;

public class PopularEventAdaptor extends RecyclerView.Adapter<PopularEventAdaptor.PopularEventHolder>{

    private List<Event> eventList;
    private Context context;

    private CurrentEventSingleton currentEventSingleton;

    private FragmentManager fragmentManager;

    public PopularEventAdaptor(Context contextList, List<Event> eventList, FragmentManager fragmentManager){
        this.eventList = eventList;
        this.context = contextList;
        this.fragmentManager = fragmentManager;
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

        holder.popularEventImage.setOnClickListener(v -> {
            currentEventSingleton = CurrentEventSingleton.getInstance();
            currentEventSingleton.setCurrentEvent(eventList.get(position));
            fragmentManager.beginTransaction().addToBackStack("fragment_popular_events").replace(R.id.frame_layout, new FragmentDetailed()).commit();
        });
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
