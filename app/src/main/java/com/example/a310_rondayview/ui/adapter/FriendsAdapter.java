package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.Event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    Context context;
    List<String> friendsList;

    public FriendsAdapter(Context context, List<String> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    public void updateFriendList(List<String> friendsList) {
        this.friendsList = friendsList;
        notifyDataSetChanged();
    }

    @androidx.annotation.NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for individual list items
        View view = LayoutInflater.from(context).inflate(R.layout.friend_card, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull FriendsViewHolder holder, int position) {
        // Binds data to the UI elements in each list item
        String email = friendsList.get(position);

        // Set up the animation for the remove button being clicked
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        ToggleButton removeFriendButton = holder.itemView.findViewById(R.id.remove_friend_button);
        removeFriendButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                holder.itemView.startAnimation(fadeOut);

                // Delay the removal of the item to match the card animation duration
                // Code snippet adapted from OpenAI. (2023). ChatGPT (Aug 24 version) [Large language model]. https://chat.openai.com/chat
                holder.itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FireBaseUserDataManager.getInstance().removeFriend(email);
                        FireBaseUserDataManager.getInstance().getFriends();
                        compoundButton.startAnimation(scaleAnimation);

                        // These are needed in order to show the event has been removed straight away
                        // Without this, the event does not disappear
                        int position = friendsList.indexOf(email);
                        if (position != -1) {
                            friendsList.remove(position);
                            notifyDataSetChanged();
                        }
                        holder.removeFriendButton.setChecked(true);
                    }
                }, fadeOut.getDuration());
            }
        });
        holder.emailTextView.setText(email);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }


    // ViewHolder class to hold references to UI elements for a list item
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        ToggleButton removeFriendButton;


        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.friendEmail);
            removeFriendButton = itemView.findViewById(R.id.remove_friend_button);
        }
    }
}
