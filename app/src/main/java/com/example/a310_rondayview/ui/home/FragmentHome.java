package com.example.a310_rondayview.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.DatabaseService;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.adapter.SwipeAdapter;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private static class ViewHolder {

        LinearLayout buttonContainer;
        LinearLayout emptyEventsLayout;

        Button nopeButton;
        Button interestedButton;
        Button refreshButton;

        Koloda koloda;

        public ViewHolder(View rootView) {
            // setting up views
            buttonContainer = rootView.findViewById(R.id.buttonsContainer);
            emptyEventsLayout = rootView.findViewById(R.id.emptyEventsLayout);
            // Set up button click listeners
            nopeButton = rootView.findViewById(R.id.nopeButton);
            interestedButton = rootView.findViewById(R.id.interestedButton);
            refreshButton = rootView.findViewById(R.id.refreshButton);
            // setting up koloda (for the card swipes) - READ MORE HERE: https://github.com/Yalantis/Koloda-Android
            koloda = rootView.findViewById(R.id.koloda);
        }

    }

    private static final String TAG = "FragmentHome";
    private SwipeAdapter adapter;
    private List<Event> events = new ArrayList<>();
    private int currentEventIndex;
    private ViewHolder vh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        vh = new ViewHolder(rootView);
        currentEventIndex = 0;
        // fetching event data
        DatabaseService databaseService = new DatabaseService();
        databaseService.getApplicableEvents().thenAccept(events1 -> {
            events = events1;
            adapter = new SwipeAdapter(getContext(), events);
            vh.koloda.setAdapter(adapter);
        });

        buttonListeners();

        /*
          Koloda interface listener functions, don't need to use ALL
         */
        vh.koloda.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {
            }

            @Override
            public void onCardDrag(int i, @NonNull View view, float v) {
            }

            @Override
            public void onCardSwipedLeft(int i) {
                FireBaseUserDataManager.getInstance().addDisinterestedEvent(events.get(currentEventIndex));
                currentEventIndex++;
            }

            @Override
            public void onCardSwipedRight(int i) {
                FireBaseUserDataManager.getInstance().addInterestedEvent(events.get(currentEventIndex));
                currentEventIndex++;
            }

            @Override
            public void onClickRight(int i) {
            }

            @Override
            public void onClickLeft(int i) {
            }


            @Override
            public void onCardSingleTap(int i) {
                //TODO DETAILS PAGE (A2) -> show details of event from dialog
                Toast.makeText(getContext(), "Open detailed view", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCardDoubleTap(int i) {
            }

            @Override
            public void onCardLongPress(int i) {
            }

            @Override
            public void onEmptyDeck() { // events finished - need method of refreshing!
                vh.emptyEventsLayout.setVisibility(View.VISIBLE);
                vh.koloda.setVisibility(View.GONE);
                vh.buttonContainer.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    private void buttonListeners() {
        // NOT INTERESTED
        vh.nopeButton.setOnClickListener(v -> {
            vh.koloda.onButtonClick(false);
            FireBaseUserDataManager.getInstance().addDisinterestedEvent(events.get(currentEventIndex));
            currentEventIndex++;
        });

        // INTERESTED
        vh.interestedButton.setOnClickListener(view -> {
            vh.koloda.onButtonClick(true);
            FireBaseUserDataManager.getInstance().addInterestedEvent(events.get(currentEventIndex));
            currentEventIndex++;
        });

        // REFRESH PAGE
        vh.refreshButton.setOnClickListener(view -> {
            DatabaseService databaseService = new DatabaseService();
            databaseService.getApplicableEvents().thenAccept(events1 -> {
                events = events1;
                vh.koloda.reloadAdapterData();
            });
            vh.emptyEventsLayout.setVisibility(View.GONE);
            vh.koloda.setVisibility(View.VISIBLE);
            vh.buttonContainer.setVisibility(View.VISIBLE);
            currentEventIndex = 1; //set at 1 as bug where first item is skipped on refresh
        });
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getCurrentEventIndex() {
        return currentEventIndex;
    }
}
