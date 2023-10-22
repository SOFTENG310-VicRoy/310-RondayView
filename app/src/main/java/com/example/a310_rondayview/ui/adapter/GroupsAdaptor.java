package com.example.a310_rondayview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.group.GroupDatabaseService;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.data.user.FriendCallback;
import com.example.a310_rondayview.model.CurrentFriendSingleton;
import com.example.a310_rondayview.model.User;
import com.example.a310_rondayview.ui.friends.FriendsEventFragment;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupsAdaptor extends RecyclerView.Adapter<GroupsAdaptor.GroupsViewHolder> {
    private CurrentFriendSingleton currentFriendSingleton;
    private FragmentManager fragmentManager;

    Context context;
    List<String> groupNames;
    GroupDatabaseService groupDatabaseService = new GroupDatabaseService();

    public GroupsAdaptor(Context context, List<String> groupNames, FragmentManager fragmentManager) {
        this.context = context;
        this.groupNames = groupNames;
        this.fragmentManager = fragmentManager;
    }

    public void updateGroupList(List<String> groupNames) {
        this.groupNames = groupNames;
        notifyDataSetChanged();
    }

    @androidx.annotation.NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for individual list items
        View view = LayoutInflater.from(context).inflate(R.layout.group_card, parent, false);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdaptor.GroupsViewHolder holder, int position) {
        String groupName = groupNames.get(position);
        holder.groupName.setText(groupName);
        StringBuilder memberCount = new StringBuilder("Members: ");
        StringBuilder eventCount = new StringBuilder("Events : ");
        groupDatabaseService.getGroupByName(groupName).thenAccept(group -> {
            memberCount.append(group.getUserIdList().size());
            eventCount.append(group.getEventIdList().size());
        }).thenAccept(task->{
            holder.groupEventCount.setText(eventCount.toString());
            holder.groupMemberCount.setText(memberCount.toString());
        });

        //TODO: onclick
        //fragmentManager.beginTransaction().addToBackStack("fragment_friends_event").replace(R.id.frame_layout, new FriendsEventFragment()).commit();
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    // ViewHolder class to hold references to UI elements for a list item
    public static class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
//        ToggleButton leaveGroupBtn;
        TextView groupMemberCount;
        TextView groupEventCount;


        public GroupsViewHolder(@org.checkerframework.checker.nullness.qual.NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            groupMemberCount = itemView.findViewById(R.id.group_member_count);
            groupEventCount= itemView.findViewById(R.id.group_event_count);
        }
    }
}
