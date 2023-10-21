package com.example.a310_rondayview.ui.popular;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.EventDatabaseService;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.ui.adapter.PopularEventAdaptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
]
 */
public class PopularEventsFragment extends Fragment {
private static class ViewHolder {
    ViewPager2 popularEventViewPager;
    public ViewHolder(View rootView) {
        popularEventViewPager = rootView.findViewById(R.id.popularEventViewPager);
    }
}

    private ViewHolder vh;
    private PopularEventAdaptor popularEventAdaptor;
    private final List<Event> topTenPopularEvents = new ArrayList<>();

    public PopularEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_events, container, false);
        refreshTopTenEvent();
        vh = new ViewHolder(rootView);
        vh.popularEventViewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.3f+r*0.7f);
        });
        vh.popularEventViewPager.setPageTransformer(transformer);
        //Fetch top 10 interested events


        return rootView;
    }

    /**
     * Fetches the top ten event ranked by the amount of interests and refreshes the display
     */

    private void refreshTopTenEvent(){
        Comparator<Event> descendingComparator = Comparator
                .comparingInt(Event::getInterestCount)
                .reversed();
        EventDatabaseService eventDatabaseService = new EventDatabaseService();
        eventDatabaseService.getAllEvents().thenAccept(events1 -> {
            events1.sort(descendingComparator);
            topTenPopularEvents.clear();
            for(Event event : events1){
                topTenPopularEvents.add(event);
                if(topTenPopularEvents.size() == 10) {
                    break;
                }
            }
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            popularEventAdaptor = new PopularEventAdaptor(getContext(), topTenPopularEvents,fragmentManager);
            vh.popularEventViewPager.setAdapter(popularEventAdaptor);
        });
    }
}