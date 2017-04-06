package com.brandonhogan.liftscout.tests.events;

import com.brandonhogan.liftscout.events.SearchViewEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(PowerMockRunner.class)
public class SearchViewEventTest {

    @Test
    public void AllExercisesListEvent_Properties() {

        String newTextValue = "What what";

        SearchViewEvent event = new SearchViewEvent(true, newTextValue);

        assertThat(event.isActive(), is(true));
        assertThat(event.getNewText(), is(newTextValue));
    }

}
