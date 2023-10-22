package com.example.a310_rondayview.ui.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.group.GroupDatabaseService;
import com.example.a310_rondayview.data.group.GroupFirestoreManager;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.data.user.FriendCallback;
import com.example.a310_rondayview.model.Group;
import com.example.a310_rondayview.ui.adapter.FriendsAdapter;
import com.example.a310_rondayview.ui.adapter.GroupsAdaptor;
import com.example.a310_rondayview.ui.friends.FriendsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {
    private class ViewHolder {

        EditText groupNameEditTxt;
        Button joinGroupBtn;
        RecyclerView groupList;

        public ViewHolder(View view) {
            groupNameEditTxt = view.findViewById(R.id.groupNameEditText);
            joinGroupBtn = view.findViewById(R.id.join_group_btn);
            groupList = view.findViewById(R.id.group_list_recycle_view);

        }
    }
    private GroupsFragment.ViewHolder vh;
    private GroupsAdaptor groupsAdaptor;

    private List<String> currentGroupNames;
    public GroupsFragment(){
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        vh = new GroupsFragment.ViewHolder(view);
        vh.joinGroupBtn.setOnClickListener(view1 -> {
            String groupName = vh.groupNameEditTxt.getText().toString();
            GroupDatabaseService groupDatabaseService = new GroupDatabaseService();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            groupDatabaseService.getGroupByName(groupName).thenAccept(group -> {
                if(group!=null){
                    //If group exists, check wether already in group
                    if(group.getUserIdList().contains(user.getUid())){
                        Toast.makeText(getActivity(), "You're already in "+groupName, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //else update user's group list and group's member
                        Toast.makeText(getActivity(), "Joined group "+groupName, Toast.LENGTH_SHORT).show();
                        group.getUserIdList().add(user.getUid());
                        FireBaseUserDataManager.getInstance().addParticipatedGroupName(groupName);
                        currentGroupNames.add(groupName);
                        groupsAdaptor.updateGroupList(currentGroupNames);
                    }
                } else {
                    //Else create new group
                    ArrayList<String> userIdList = new ArrayList<>();
                    userIdList.add(user.getUid());
                    Group newGroup = new Group(groupName, userIdList, new ArrayList<String>());
                    //Automatically create new group if group doesn't exist
                    GroupFirestoreManager.getInstance().addGroup(newGroup, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Created new group "+groupName, Toast.LENGTH_SHORT).show();
                            FireBaseUserDataManager.getInstance().addParticipatedGroupName(groupName);
                            currentGroupNames.add(groupName);
                            groupsAdaptor.updateGroupList(currentGroupNames);
                        } else {
                            Toast.makeText(getActivity(), "Could not create new group", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FireBaseUserDataManager.getInstance().getParticipatedGroupNames(participatedGroups -> {
            currentGroupNames = participatedGroups;
            vh.groupList.setLayoutManager(new LinearLayoutManager(getContext()));
            vh.groupList.setHasFixedSize(true);
            // populate the recycler view
            Log.d("View created", "view created");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            groupsAdaptor = new GroupsAdaptor(getContext(), currentGroupNames, fragmentManager);
            vh.groupList.setAdapter(groupsAdaptor);
            groupsAdaptor.notifyDataSetChanged();
        });
    }
}
