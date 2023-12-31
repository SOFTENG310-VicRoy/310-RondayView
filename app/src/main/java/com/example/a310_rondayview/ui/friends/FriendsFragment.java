package com.example.a310_rondayview.ui.friends;

import android.os.Bundle;
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
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.data.user.FriendCallback;
import com.example.a310_rondayview.model.User;
import com.example.a310_rondayview.ui.adapter.FriendsAdapter;

import java.util.List;

public class FriendsFragment extends Fragment {
    private class ViewHolder {

        EditText friendEmail;
        Button addFriendBtn;
        RecyclerView friendList;

        public ViewHolder(View view) {
            friendEmail = view.findViewById(R.id.friend_email);
            addFriendBtn = view.findViewById(R.id.add_friend_btn);
            friendList = view.findViewById(R.id.friend_list_recycler_view);

        }
    }
    private FriendsFragment.ViewHolder vh;
    private FriendsAdapter friendsAdapter;
    List<User> friendList;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        vh = new FriendsFragment.ViewHolder(view);
        vh.addFriendBtn.setOnClickListener(view1 -> {
            String friendEmail = vh.friendEmail.getText().toString();
            FireBaseUserDataManager.getInstance().addFriend(friendEmail, new FriendCallback() {
                    @Override
                    public void onSuccessfulFriendOperation() {
                        friendsAdapter.updateFriendList(FireBaseUserDataManager.getInstance().getFriendsList());
                    }
                    @Override
                    public void onUnsuccessfulFriendOperation(Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendList = FireBaseUserDataManager.getInstance().getFriendsList();
        // setup the recycler view
        vh.friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        vh.friendList.setHasFixedSize(true);

        // populate the recycler view
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        friendsAdapter = new FriendsAdapter(getContext(), friendList, fragmentManager);
        vh.friendList.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();

        // if the local friend list stored was empty, we fetch friend list from db and update recycler view if there is content
        if (friendList.isEmpty()) {
            FireBaseUserDataManager.getInstance().getFriends(new FriendCallback() {
                @Override
                public void onSuccessfulFriendOperation() {
                    friendsAdapter.updateFriendList(FireBaseUserDataManager.getInstance().getFriendsList());
                }
                @Override
                public void onUnsuccessfulFriendOperation(Exception e) {
                    // Don't do anything if it fails
                }
            });
        }
    }
}
