package com.example.a310_rondayview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SwipeAdapterTest {

    @Test
    public void testGetCount() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        events.add(new Event());
        SwipeAdapter adapter = new SwipeAdapter(null, events);

        assertEquals(2, adapter.getCount());
    }

    @Test
    public void testGetItem() {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        events.add(event);
        SwipeAdapter adapter = new SwipeAdapter(null, events);

        assertEquals(event, adapter.getItem(0));
    }

    @Test
    public void testGetItemId() {
        SwipeAdapter adapter = new SwipeAdapter(null, new ArrayList<>());
        assertEquals(0, adapter.getItemId(0));
    }
}
