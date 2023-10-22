package com.example.a310_rondayview.ui.account;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a310_rondayview.ui.friends.FriendsFragment;
import com.example.a310_rondayview.ui.groups.GroupsFragment;
import com.example.a310_rondayview.ui.interestedevents.InterestedEventsFragment;
import com.example.a310_rondayview.R;

public class ProfileFragment extends Fragment {

    private class ViewHolder{

        CardView goToAccountBtn;
        CardView goToLikedEventsBtn;
        CardView goToFriends;
        CardView goToGroupsBtn;

        public ViewHolder(View view){
            goToAccountBtn = view.findViewById(R.id.go_to_account_details);
            goToLikedEventsBtn = view.findViewById(R.id.go_to_liked_events);
            goToFriends = view.findViewById(R.id.go_to_friends);
            goToGroupsBtn = view.findViewById(R.id.go_to_groups);
        }
    }

    ViewHolder vh;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        vh = new ViewHolder(view);

        vh.goToAccountBtn.setOnClickListener(v -> {
            // Create an instance of FragmentAccount
            FragmentAccount fragmentAccount = new FragmentAccount();

            replaceFragmentWith(fragmentAccount);
        });

        vh.goToLikedEventsBtn.setOnClickListener(v -> {
            // Create an instance of FragmentAccount
            InterestedEventsFragment interestedEventsFragment = new InterestedEventsFragment();

            replaceFragmentWith(interestedEventsFragment);
        });

        vh.goToFriends.setOnClickListener(v -> {
            // Create an instance of FragmentAccount
            FriendsFragment friendsFragment = new FriendsFragment();

            replaceFragmentWith(friendsFragment);
        });

        vh.goToGroupsBtn.setOnClickListener(v->{
            //Create an instance of fragment groups
            GroupsFragment groupsFragment = new GroupsFragment();
            replaceFragmentWith(groupsFragment);
        });

        return view;
    }

    // method created to reduce duplicate code in onclicks
    public void replaceFragmentWith(Fragment replacementFragment){

        // Get the Fragment Manager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Begin a transaction to replace the current fragment with the interestedEventsFragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, replacementFragment)
                .addToBackStack(null)
                .commit();

    }

}