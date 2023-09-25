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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
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
    FriendsFragment.ViewHolder vh;
    FriendsAdapter friendsAdapter;
    List<String> friendList;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        vh = new FriendsFragment.ViewHolder(view);

        vh.addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FireBaseUserDataManager.getInstance().addFriend(vh.friendEmail.getText().toString());
                    FireBaseUserDataManager.getInstance().getFriends();
                    friendsAdapter.updateFriendList(FireBaseUserDataManager.getInstance().friendEmails);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FireBaseUserDataManager.getInstance().getFriends();

        // load in the list of friends' email from the database
        friendList = FireBaseUserDataManager.getInstance().friendEmails;

        // setup the recycler view
        vh.friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        vh.friendList.setHasFixedSize(true);

        // populate the recycler view
        friendsAdapter = new FriendsAdapter(getContext(), friendList);
        vh.friendList.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();
    }
}
