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

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.DatabaseService;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.CurrentEventSingleton;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.adapter.PopularEventAdaptor;
import com.example.a310_rondayview.ui.adapter.SwipeAdapter;
import com.example.a310_rondayview.ui.detailed.FragmentDetailed;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentHome extends Fragment {

    private static class ViewHolder {
        LinearLayout buttonContainer;
        LinearLayout emptyEventsLayout;
        ViewPager2 popularEventViewPager;
        Button nopeButton;
        Button interestedButton;
        Button refreshButton;
        Koloda koloda;

        public ViewHolder(View rootView) {
            // setting up views
            buttonContainer = rootView.findViewById(R.id.buttonsContainer);
            emptyEventsLayout = rootView.findViewById(R.id.emptyEventsLayout);
            //Set up UI transition for the popular events list
            popularEventViewPager = rootView.findViewById(R.id.popularEventViewPager);

            popularEventViewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
            CompositePageTransformer transformer = new CompositePageTransformer();
            transformer.addTransformer(new MarginPageTransformer(40));
            transformer.addTransformer((page, position) -> {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.3f+r*0.7f);
            });

            popularEventViewPager.setPageTransformer(transformer);
            // Set up button click listeners
            nopeButton = rootView.findViewById(R.id.nopeButton);
            interestedButton = rootView.findViewById(R.id.interestedButton);
            refreshButton = rootView.findViewById(R.id.refreshButton);
            // setting up koloda (for the card swipes) - READ MORE HERE: https://github.com/Yalantis/Koloda-Android
            koloda = rootView.findViewById(R.id.koloda);
        }

    }

    private SwipeAdapter adapter;
    private PopularEventAdaptor popularEventAdaptor;
    private List<Event> events = new ArrayList<>();
    private List<Event>topTenPopularEvents = new ArrayList<>();
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
            //Fetch top 10 interested events
            refreshTopTenEvent();
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
                Toast.makeText(getContext(), "Nope!", Toast.LENGTH_SHORT).show();
                FireBaseUserDataManager.getInstance().addDisinterestedEvent(events.get(currentEventIndex));
                currentEventIndex++;
            }
            @Override
            public void onCardSwipedRight(int i) {
                Toast.makeText(getContext(), "Interested!", Toast.LENGTH_SHORT).show();
                String eventId = events.get(currentEventIndex).getEventId();
                DatabaseService databaseService = new DatabaseService();
                databaseService.getEventById(eventId).thenAccept(event1 -> {
                    event1.incrementInterestCount();
                    EventsFirestoreManager.getInstance().updateEvent(event1);
                    refreshTopTenEvent();
                    FireBaseUserDataManager.getInstance().addInterestedEvent(event1);
                    currentEventIndex++;
                });
            }
            @Override
            public void onClickRight(int i) {
               onCardSwipedRight(i);
            }
            @Override
            public void onClickLeft(int i) {
                onCardSwipedLeft(i);

            }
            @Override
            public void onCardSingleTap(int i) {
                CurrentEventSingleton.getInstance().setCurrentEvent(events.get(currentEventIndex));
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("fragment_home").replace(R.id.frame_layout, new FragmentDetailed()).commit();
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
        vh.nopeButton.setOnClickListener(v -> vh.koloda.onClickLeft());
        // INTERESTED
        vh.interestedButton.setOnClickListener(v -> vh.koloda.onClickRight());

        // REFRESH PAGE
        vh.refreshButton.setOnClickListener(view -> {
            DatabaseService databaseService = new DatabaseService();
            databaseService.getApplicableEvents().thenAccept(events1 -> {
                events = events1;
                vh.koloda.reloadAdapterData();
                vh.emptyEventsLayout.setVisibility(View.GONE);
                vh.koloda.setVisibility(View.VISIBLE);
                vh.buttonContainer.setVisibility(View.VISIBLE);
                currentEventIndex = 0;//Bug where first card messes up count
                refreshTopTenEvent();
            });

        });
    }

    /**
     * Fetches the top ten event ranked by the amount of interests and refreshes the display
     */

    private void refreshTopTenEvent(){
        Comparator<Event> descendingComparator = Comparator
                .comparingInt(Event::getInterestCount)
                .reversed();
        DatabaseService databaseService = new DatabaseService();
        databaseService.getAllEvents().thenAccept(events1 -> {
            Collections.sort(events1, descendingComparator);
            topTenPopularEvents.clear();
            for(Event event : events1){
                topTenPopularEvents.add(event);
                if(topTenPopularEvents.size() == 10) {
                    break;
                }
            }
            popularEventAdaptor = new PopularEventAdaptor(getContext(), topTenPopularEvents);
            vh.popularEventViewPager.setAdapter(popularEventAdaptor);
        });
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getCurrentEventIndex() {
        return currentEventIndex;
    }
}
