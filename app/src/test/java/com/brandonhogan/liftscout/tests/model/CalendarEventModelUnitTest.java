package com.brandonhogan.liftscout.tests.model;

import com.brandonhogan.liftscout.repository.model.CalendarEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_INT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class CalendarEventModelUnitTest {

    @Test
    public void CalendarEventModel_CorrectProperties_assertTrue() {

        java.util.Date date = new java.util.Date();
        CalendarEvent calendarEvent = new CalendarEvent(FAKE_INT, date);

        assertThat(calendarEvent.getColor(), is(FAKE_INT));
        assertThat(calendarEvent.getDate(), is(date));

        calendarEvent.setColor(FAKE_INT + 10);
        calendarEvent.setDate(date);

        assertThat(calendarEvent.getColor(), is(FAKE_INT + 10));
    }
}
