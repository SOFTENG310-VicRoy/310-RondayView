package com.example.a310_rondayview.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.data.user.FriendCallback;
import com.example.a310_rondayview.model.CurrentFriendSingleton;
import com.example.a310_rondayview.ui.adapter.InterestedEventsAdapter;

public class FriendsEventFragment extends Fragment {
    private class ViewHolder {
        RecyclerView friendsEventRecyclerView;
        public ViewHolder(View view) {
            friendsEventRecyclerView = view.findViewById(R.id.friends_events_recycler_view);
        }
    }
    CurrentFriendSingleton currentFriendSingleton;
    FriendsEventFragment.ViewHolder vh;

    public FriendsEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_event, container, false);
        vh = new FriendsEventFragment.ViewHolder(view);
        currentFriendSingleton = CurrentFriendSingleton.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // load in the list of interested events from the database
        FireBaseUserDataManager.getInstance().getFriendsEvents(currentFriendSingleton.getCurrentFriend(), new FriendCallback() {
            @Override
            public void onSuccessfulFriendOperation() {
                // setup the recycler view
                vh.friendsEventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                vh.friendsEventRecyclerView.setHasFixedSize(true);

                // populate the recycler view
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                InterestedEventsAdapter interestedEventsAdapter = new InterestedEventsAdapter(getContext(), currentFriendSingleton.getCurrentFriend().getInterestedEvents(), true, fragmentManager);
                vh.friendsEventRecyclerView.setAdapter(interestedEventsAdapter);
                interestedEventsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onUnsuccessfulFriendOperation(Exception e) {
                // Do nothing
            }
        });


    }
}
