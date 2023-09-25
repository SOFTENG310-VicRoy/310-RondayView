package com.example.a310_rondayview.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.ui.interestedevents.InterestedEventsFragment;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private class ViewHolder {

        EditText friendEmail;
        Button addFriendBtn;
        ListView friendList;

        public ViewHolder(View view) {
            friendEmail = view.findViewById(R.id.friend_email);
            addFriendBtn = view.findViewById(R.id.add_friend_btn);
            friendList = view.findViewById(R.id.friend_list);

        }
    }
    FriendsFragment.ViewHolder vh;
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

        // Assuming you have an ArrayList of strings
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("Item 1");
        stringList.add("Item 2");
        stringList.add("Item 3");
        // Add more items as needed

        // Create an ArrayAdapter to bind the ArrayList to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringList);
        vh.friendList.setAdapter(adapter);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
