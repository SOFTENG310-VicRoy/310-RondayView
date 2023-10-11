package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.model.User;
import com.example.a310_rondayview.ui.detailed.FragmentDetailed;
import com.google.api.Distribution;

import java.util.List;

public class SimilarEventAdapter extends RecyclerView.Adapter<SimilarEventAdapter.SimilarEventViewHolder> {
    public class SimilarEventViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        TextView eventTitle;
        ImageView clubImage;
        ImageView eventImage;
        LinearLayout container;
        public SimilarEventViewHolder(@NonNull View itemView) {
            super(itemView);
            this.clubImage = itemView.findViewById(R.id.se_profileImageView);
            this.eventTitle = itemView.findViewById(R.id.se_eventTitleTextView);
            this.clubName = itemView.findViewById(R.id.se_clubNameTextView);
            this.eventImage = itemView.findViewById(R.id.se_eventImageView);
            this.container = itemView.findViewById(R.id.se_container);
        }
    }

    private Context context;
    private List<Event> similarEvents;
    private FragmentManager fragmentManager;

    public SimilarEventAdapter(Context context, List<Event> similarEvents, FragmentManager fragmentManager) {
        this.context = context;
        this.similarEvents = similarEvents;
        this.fragmentManager = fragmentManager;
    }
    public void setSimilarEvents(List<Event> similarEvents) {
        this.similarEvents = similarEvents;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SimilarEventAdapter.SimilarEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for similar event  items
        View view = LayoutInflater.from(context).inflate(R.layout.small_event_container, parent, false);
        return new SimilarEventAdapter.SimilarEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarEventAdapter.SimilarEventViewHolder holder, int position) {
        Event event = similarEvents.get(position);
        holder.clubName.setText(event.getClubName());
        holder.eventTitle.setText(event.getTitle());
        Glide.with(holder.itemView.getContext()).load(event.getImageURL()).into(holder.eventImage);
        Glide.with(holder.itemView.getContext()).load(event.getEventClubProfilePicture()).into(holder.clubImage);
        // navigate to detail view for the similar event item
        holder.container.setOnClickListener(e -> {
            CurrentEventSingleton.getInstance().setCurrentEvent(event);
            fragmentManager.beginTransaction().addToBackStack("fragment_interested_events").replace(R.id.frame_layout, new FragmentDetailed()).commit();
        });

    }

    @Override
    public int getItemCount() {
        return similarEvents.size();
    }
}
