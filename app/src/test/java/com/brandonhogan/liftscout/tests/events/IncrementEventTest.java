package com.brandonhogan.liftscout.tests.events;

import com.brandonhogan.liftscout.events.IncrementEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Brandon on 4/6/2017.
 * Description :
 */

@RunWith(PowerMockRunner.class)
public class IncrementEventTest {

    @Test
    public void AllExercisesListEvent_Properties() {
        IncrementEvent event = new IncrementEvent();

        assertThat(event, is(notNullValue()));
    }
}
