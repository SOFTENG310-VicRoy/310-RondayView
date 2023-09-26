package com.example.a310_rondayview;

import static org.junit.Assert.assertNotNull;

import com.example.a310_rondayview.data.user.FireBaseUserDataManager;

import org.junit.Test;

public class UserDataManagerTest {

    @Test
    public void testInterestedEvents(){
        FireBaseUserDataManager fireBaseUserDataManager = FireBaseUserDataManager.getInstance();
        fireBaseUserDataManager.getEvents(true);

        assertNotNull(fireBaseUserDataManager.InterestedEvents);
    }

    @Test
    public void testDisinterestedEvents(){
        FireBaseUserDataManager fireBaseUserDataManager = FireBaseUserDataManager.getInstance();
        fireBaseUserDataManager.getEvents(false);
        assertNotNull(fireBaseUserDataManager.DisinterestedEvents);
    }
}
