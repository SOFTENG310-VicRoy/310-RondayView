package com.example.a310_rondayview.ui.detailed;

import com.example.a310_rondayview.model.Event;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;

public class SimilarEventComparator implements Comparator<Event> {
    private Event baseEvent;
    private LevenshteinDistance levenshteinDistance;

    public SimilarEventComparator(Event baseEvent) {
        this.baseEvent = baseEvent;
        this.levenshteinDistance = new LevenshteinDistance();
    }

    @Override
    public int compare(Event event1, Event event2) {
        int distance1 = calculateSimilarity(event1);
        int distance2 = calculateSimilarity(event2);

        //sorts in ascending order of Levenshtein distance (more similar first)
        return Integer.compare(distance1, distance2);
    }

    /**
     * This method calculates the levenshtein distance from an event to the base event.
     * This calculates in a higher weighting for similar club names then to title then lastly description
     * @param event the event we are checking similarity to
     * @return
     */
    private int calculateSimilarity(Event event) {
        String baseClub = baseEvent.getClubName();
        String eventClub = event.getClubName();

        String baseTitle = baseEvent.getTitle();
        String eventTitle = event.getTitle();

        String baseDescription = baseEvent.getDescription();
        String eventDescription = event.getDescription();

        //calculate Levenshtein distance for club name, title, and description
        int clubDistance = levenshteinDistance.apply(baseClub, eventClub);
        int titleDistance = levenshteinDistance.apply(baseTitle, eventTitle);
        int descriptionDistance = levenshteinDistance.apply(baseDescription, eventDescription);

        //give more weighting to similar club names then to title then lastly description
        return (50 * clubDistance) + (3 * titleDistance) + descriptionDistance;
    }
}
