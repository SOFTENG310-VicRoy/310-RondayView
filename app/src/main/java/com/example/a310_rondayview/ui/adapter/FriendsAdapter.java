package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.data.user.FriendCallback;
import com.example.a310_rondayview.model.CurrentFriendSingleton;
import com.example.a310_rondayview.model.User;
import com.example.a310_rondayview.ui.friends.FriendsEventFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.Current;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    private CurrentFriendSingleton currentFriendSingleton;
    private FragmentManager fragmentManager;

    Context context;
    List<User> friendsList;

    public FriendsAdapter(Context context, List<User> friendsList, FragmentManager fragmentManager) {
        this.context = context;
        this.friendsList = friendsList;
        this.fragmentManager = fragmentManager;
    }

    public void updateFriendList(List<User> friendsList) {
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
        User friend = friendsList.get(position);
        String email = friend.getEmail();
        String id = friend.getUserId();

        // Set up the animation for the remove button being clicked
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        ToggleButton removeFriendButton = holder.itemView.findViewById(R.id.remove_friend_button);
        removeFriendButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            holder.itemView.startAnimation(fadeOut);

            // Delay the removal of the item to match the card animation duration
            // Code snippet adapted from OpenAI. (2023). ChatGPT (Aug 24 version) [Large language model]. https://chat.openai.com/chat
            holder.itemView.postDelayed(() -> {
                FireBaseUserDataManager.getInstance().removeFriend(email, new FriendCallback() {
                    @Override
                    public void onSuccessfulFriendOperation() {
                        // Dont do anything
                    }
                    @Override
                    public void onUnsuccessfulFriendOperation(Exception e) {
                        // Dont do anything
                    }
                });
                compoundButton.startAnimation(scaleAnimation);

                // These are needed in order to show the event has been removed straight away
                // Without this, the event does not disappear
                int position1 = friendsList.indexOf(friend);
                if (position1 != -1) {
                    friendsList.remove(position1);
                    notifyDataSetChanged();
                }
                holder.removeFriendButton.setChecked(true);
            }, fadeOut.getDuration());
        });
        holder.emailTextView.setText(email);
        holder.idTextView.setText("ID: " + id);
        holder.friendLinearLayout.setOnClickListener(v -> {
            currentFriendSingleton = CurrentFriendSingleton.getInstance();
            currentFriendSingleton.setCurrentFriend(friend);

            fragmentManager.beginTransaction().addToBackStack("fragment_friends_event").replace(R.id.frame_layout, new FriendsEventFragment()).commit();
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }


    // ViewHolder class to hold references to UI elements for a list item
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        ToggleButton removeFriendButton;
        TextView idTextView;
        LinearLayout friendLinearLayout;


        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.friendEmail);
            removeFriendButton = itemView.findViewById(R.id.remove_friend_button);
            idTextView = itemView.findViewById(R.id.friendId);
            friendLinearLayout = itemView.findViewById(R.id.friend_linear_layout);
        }
    }
}
