package com.brandonhogan.liftscout.tests.events;

import com.brandonhogan.liftscout.events.HistoryTrackerEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
public class HistoryTrackerEventTest {

    @Test
    public void AllExercisesListEvent_Properties() {

        Date date = new Date();
        HistoryTrackerEvent event = new HistoryTrackerEvent(0, date, 1);

        assertThat(event.getEventID(), is(0));
        assertThat(event.getDate(), is(date));
        assertThat(event.getExerciseId(), is(1));

        assertThat(HistoryTrackerEvent.EVENT_EDIT_SET, is(1));
        assertThat(HistoryTrackerEvent.EVENT_VIEW_WORKOUT, is(2));
    }
}
