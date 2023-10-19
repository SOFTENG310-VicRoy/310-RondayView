package com.example.a310_rondayview.ui.detailed;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.DatabaseService;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.example.a310_rondayview.model.Comment;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.adapter.SimilarEventAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class FragmentDetailed extends Fragment {
    private class ViewHolder {
        ImageView eventImage;
        ImageView backImage;
        TextView clubNameText;
        TextView eventNameText;
        ImageView profileImage;
        TextView locationText;
        TextView eventDateText;
        TextView eventDescText;
        RecyclerView similarEventRv;
        TextView addCommentText;
        EditText commentEditText;
        LinearLayout commentsLayout;

        public ViewHolder(View view) {
            eventImage = view.findViewById(R.id.event_image);
            backImage = view.findViewById(R.id.back);
            clubNameText = view.findViewById(R.id.clubNameTextView);
            profileImage = view.findViewById(R.id.profileImageView);
            eventNameText = view.findViewById(R.id.event_name);
            eventDateText = view.findViewById(R.id.event_date);
            locationText = view.findViewById(R.id.locationtext);
            eventDescText = view.findViewById(R.id.event_desc);
            similarEventRv = view.findViewById(R.id.similar_events_rv);
            addCommentText = view.findViewById(R.id.add_comment_text);
            commentEditText = view.findViewById(R.id.comment_edit_text);
            commentsLayout = view.findViewById(R.id.comments_layout);
        }
    }
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    List<Event> similarEvents;
    CurrentEventSingleton currentEvent;
    ViewHolder vh;

    public FragmentDetailed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        vh = new ViewHolder(view);
        currentEvent = CurrentEventSingleton.getInstance();
        vh.clubNameText.setText(currentEvent.getCurrentEvent().getClubName());
        vh.eventNameText.setText(currentEvent.getCurrentEvent().getTitle());
        vh.eventDateText.setText(currentEvent.getCurrentEvent().getDateTime().toString());
        vh.locationText.setText(currentEvent.getCurrentEvent().getLocation());
        vh.eventDescText.setText(currentEvent.getCurrentEvent().getDescription());
        vh.addCommentText.setOnClickListener(v -> {
            String commentText = vh.commentEditText.getText().toString();
            if (!commentText.isEmpty()) {
                DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    String currentUsername = removeAtGmail(documentSnapshot.getString("email"));
                    Comment comment = new Comment(currentUsername, commentText);
                    currentEvent.getCurrentEvent().addComment(comment);
                    EventsFirestoreManager.getInstance().updateEvent(currentEvent.getCurrentEvent());
                    addComment(comment);
                });

                // Clear the EditText after adding the comment
                vh.commentEditText.setText("");
            }
        });
        if (currentEvent.getCurrentEvent().getComments() != null) {
            for (Comment comment : currentEvent.getCurrentEvent().getComments()) {
                addComment(comment);
            }
        }



        Glide.with(getContext()).load(currentEvent.getCurrentEvent().getImageURL()).into(vh.eventImage);
        Glide.with(getContext()).load(currentEvent.getCurrentEvent().getEventClubProfilePicture()).into(vh.profileImage);
        vh.backImage.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // load in the events from the database and sort them based on their similarity to
        // the current event dispalyed. (show a max of 10 events)
        DatabaseService databaseService = new DatabaseService();
        databaseService.getAllEvents().thenAccept(events -> {
            events.remove(currentEvent.getCurrentEvent());
            similarEvents = events;
            // setup the similar event recycler view
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            vh.similarEventRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            SimilarEventAdapter similarEventAdapter = new SimilarEventAdapter(getContext(), similarEvents, fragmentManager);
            vh.similarEventRv.setAdapter(similarEventAdapter);
            // sort based on similarity
            Collections.sort(similarEvents, new SimilarEventComparator(currentEvent.getCurrentEvent()));
        });

        return view;
    }
    private void addComment(Comment comment){
        LinearLayout commentLayout = new LinearLayout(getContext());
        commentLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 20, 0, 20);

//        commentLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
        TextView commentTextView = new TextView(getContext());
        commentTextView.setTag(comment);
        commentTextView.setText(comment.getUsername() + ": " + comment.getCommentText());
        commentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        commentTextView.setTextSize(16);
        commentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));

        // Create a "delete" button (CardView) for each comment
        CardView deleteButton = new CardView(getContext());
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButton.setCardBackgroundColor(Color.RED);
        deleteButton.setCardElevation(8);

        TextView deleteTextView = new TextView(getContext());
        deleteTextView.setText("Delete");
        deleteTextView.setTextColor(Color.WHITE);
        deleteTextView.setGravity(Gravity.CENTER);
        deleteButton.addView(deleteTextView);


        deleteButton.setOnClickListener(v -> {
            currentEvent.getCurrentEvent().deleteComment(comment);
            EventsFirestoreManager.getInstance().updateEvent(currentEvent.getCurrentEvent());
            vh.commentsLayout.removeView(commentLayout);
        });
        commentLayout.addView(commentTextView);
        commentLayout.addView(deleteButton);
        deleteButton.setVisibility(View.INVISIBLE);
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String currentUsername = removeAtGmail(documentSnapshot.getString("email"));
            if (comment.getUsername().equals(currentUsername)){
                deleteButton.setVisibility(View.VISIBLE);
            }
        });

        vh.commentsLayout.addView(commentLayout,layoutParams);
        View separator = new View(getContext());
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1 // Set the height you want for the separator
        ));
        separator.setBackgroundColor(Color.argb(128, 0, 0, 0));
        vh.commentsLayout.addView(separator);
    }

    private String getUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        String userName = user.getEmail();
        userName = userName.replace("@gmail.com", "");
        userName = userName.replace("@aucklanduni.ac.nz", "");
        return userName;

    }

    private String removeAtGmail(String username){
        username = username.replace("@gmail.com", "");
        username = username.replace("@aucklanduni.ac.nz", "");
        return username;
    }


}