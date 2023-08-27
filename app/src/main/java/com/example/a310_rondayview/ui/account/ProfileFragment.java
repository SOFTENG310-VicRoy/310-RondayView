package com.example.a310_rondayview.ui.account;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a310_rondayview.ui.interestedevents.InterestedEventsFragment;
import com.example.a310_rondayview.R;

public class ProfileFragment extends Fragment {

    private class ViewHolder{

        CardView goToAccountBtn, goToLikedEventsBtn;

        public ViewHolder(View view){
            goToAccountBtn = view.findViewById(R.id.go_to_account_details);
            goToLikedEventsBtn = view.findViewById(R.id.go_to_liked_events);
        }
    }

    ViewHolder vh;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        vh = new ViewHolder(view);

        vh.goToAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of FragmentAccount
                FragmentAccount fragmentAccount = new FragmentAccount();

                replaceFragmentWith(fragmentAccount);
            }
        });

        vh.goToLikedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of FragmentAccount
                InterestedEventsFragment interestedEventsFragment = new InterestedEventsFragment();

                replaceFragmentWith(interestedEventsFragment);
            }
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