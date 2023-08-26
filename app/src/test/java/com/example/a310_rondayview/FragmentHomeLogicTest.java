package com.example.a310_rondayview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

// because of private fields had to limit test cases, but basic ones to ensure its running properly
public class FragmentHomeLogicTest {
    @Test
    public void testGetEvents_initialState() {
        FragmentHome fragmentHome = new FragmentHome();
        assertTrue(fragmentHome.getEvents().isEmpty());
    }

    @Test
    public void testGetCurrentEventIndex_initialState() {
        FragmentHome fragmentHome = new FragmentHome();
        assertEquals(0, fragmentHome.getCurrentEventIndex());
    }
}



