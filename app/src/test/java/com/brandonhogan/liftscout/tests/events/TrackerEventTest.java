package com.brandonhogan.liftscout.tests.events;

import com.brandonhogan.liftscout.events.TrackerEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
public class TrackerEventTest {

    @Test
    public void AllExercisesListEvent_Properties() {

        TrackerEvent event = new TrackerEvent(true);
        assertThat(event.isUpdated(), is(true));
        assertThat(event.isNew(), is(false));


        event = new TrackerEvent(false, true);
        assertThat(event.isUpdated(), is(false));
        assertThat(event.isNew(), is(true));

    }

}
