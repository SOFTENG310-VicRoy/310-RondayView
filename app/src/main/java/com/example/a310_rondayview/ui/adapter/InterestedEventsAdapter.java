package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.DatabaseService;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.detailed.FragmentDetailed;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class InterestedEventsAdapter extends RecyclerView.Adapter<InterestedEventsAdapter.InterestedEventsViewHolder> {
    // ViewHolder class to hold references to UI elements for a list item
    public static class InterestedEventsViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImageView;
        TextView titleTextView;
        TextView descriptionTextView;
        ToggleButton heartButton;

        Button notificationButton;


        public InterestedEventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.coverImage);
            titleTextView = itemView.findViewById(R.id.titleText);
            descriptionTextView = itemView.findViewById(R.id.descriptionText);
            heartButton = itemView.findViewById(R.id.heart_button);
            notificationButton = itemView.findViewById(R.id.notification_button);
        }
    }
    static String description = "description";
    static String eventLocation = "eventLocation";
    static String title= "title";
    Context context;
    List<Event> eventsList;
    boolean hideHeart;

    private CurrentEventSingleton currentEventSingleton;
    private FragmentManager fragmentManager;

    public InterestedEventsAdapter(Context context, List<Event> eventsList) {
        this.context = context;
        this.eventsList = eventsList;

        this.hideHeart = false;
    }
    public InterestedEventsAdapter(Context context, List<Event> eventsList, boolean hideHeart, FragmentManager fragmentManager) {
        this.context = context;
        this.eventsList = eventsList;
        this.hideHeart = hideHeart;
        this.fragmentManager = fragmentManager;

    }

    @androidx.annotation.NonNull
    @Override
    public InterestedEventsViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for individual list items
        View view = LayoutInflater.from(context).inflate(R.layout.interested_events_card, parent, false);
        return new InterestedEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull InterestedEventsViewHolder holder, int position) {
        // Binds data to the UI elements in each list item
        Event event = eventsList.get(position);

        // Set up the animation for the heart being clicked
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        if (hideHeart) {
            holder.heartButton.setVisibility(View.GONE);
        }
        holder.heartButton.setChecked(true);
        holder.heartButton.setEnabled(true);

        holder.heartButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (!isChecked) {
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                holder.itemView.startAnimation(fadeOut);

                // disable the button so that user cannot spam it and run the event updater multiple times
                holder.heartButton.setEnabled(false);

                // Delay the removal of the item to match the card animation duration
                // Code snippet adapted from OpenAI. (2023). ChatGPT (Aug 24 version) [Large language model]. https://chat.openai.com/chat
                holder.itemView.postDelayed(() -> {
                    Log.d("test", "Event namme:" + event.getTitle());
                    Log.d("test", "Event count:" + event.getInterestCount());

                    String eventId = event.getEventId();
                    DatabaseService databaseService = new DatabaseService();
                    databaseService.getEventById(eventId).thenAccept(event1 -> {
                        event1.decrementInterestCount();
                        EventsFirestoreManager.getInstance().updateEvent(event1);
                        FireBaseUserDataManager.getInstance().removeInterestedEvent(event1);
                        FireBaseUserDataManager.getInstance().getEvents(true);
                        compoundButton.startAnimation(scaleAnimation);
                        // These are needed in order to show the event has been removed straight away
                        // Without this, the event does not disappear
                        int position1 = eventsList.indexOf(event);
                        if (position1 != -1) {
                            eventsList.remove(position1);
                            notifyDataSetChanged();
                        }
                    });

                }, fadeOut.getDuration());
            }


        });

        Glide.with(holder.itemView.getContext()).load(event.getImageURL()).into(holder.eventImageView);
        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());
        holder.notificationButton.setOnClickListener(v -> {
            currentEventSingleton = CurrentEventSingleton.getInstance();

            currentEventSingleton.setCurrentEvent(event);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(title, event.getTitle())
                    .putExtra(eventLocation, event.getLocation())
                    .putExtra(description, event.getDescription())
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getDateTime());
            context.startActivity(intent);
        });
        holder.eventImageView.setOnClickListener(v -> {
            fragmentManager.beginTransaction().addToBackStack("fragment_interested_events").replace(R.id.frame_layout, new FragmentDetailed()).commit();
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


}
