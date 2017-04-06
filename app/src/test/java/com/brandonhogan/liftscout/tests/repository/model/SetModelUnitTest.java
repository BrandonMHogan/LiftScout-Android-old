package com.brandonhogan.liftscout.tests.repository.model;

import com.brandonhogan.liftscout.repository.model.Exercise;
import com.brandonhogan.liftscout.repository.model.Rep;
import com.brandonhogan.liftscout.repository.model.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.realm.RealmList;

import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_INT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class SetModelUnitTest {

    @Test
    public void SetModel_CorrectProperties_assertTrue() {
        Set set = new Set();

        java.util.Date date = new java.util.Date();
        Exercise exercise = new Exercise();
        RealmList<Rep> reps = new RealmList<>();
        reps.add(new Rep());

        set.setId(FAKE_INT);
        set.setOrderId(FAKE_INT);
        set.setDate(date);
        set.setExercise(exercise);
        set.setReps(reps);

        assertThat(set.getId(), is(FAKE_INT));
        assertThat(set.getOrderId(), is(FAKE_INT));
        assertThat(set.getDate(), is(date));
        assertThat(set.getExercise(), is(exercise));
        assertThat(set.getReps(), is(reps));
    }
}
