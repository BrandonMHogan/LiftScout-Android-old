package com.brandonhogan.liftscout.tests.events;

import com.brandonhogan.liftscout.events.CategoryListEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
public class CategoryListEventTest {

    CategoryListEvent event;

    @Before
    public void setUp() {
        event = new CategoryListEvent();
    }

    @Test
    public void AllExercisesListEvent_Properties() {

        event.setUpdate(true);
        assertThat(event.isUpdate(), is(true));

        event.setUpdate(false);
        assertThat(event.isUpdate(), is(false));

        event.setOnFabClicked(true);
        assertThat(event.isOnFabClicked(), is(true));

        event.setOnFabClicked(false);
        assertThat(event.isOnFabClicked(), is(false));
    }
}
