package com.brandonhogan.liftscout.model;

import android.content.Context;

import com.brandonhogan.liftscout.repository.model.Exercise;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.Utils.constants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class ExerciseModelUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void ExerciseModel_CorrectProperties_assertTrue() {
        Exercise exercise = new Exercise();
        java.util.Date date = new java.util.Date();

        exercise.setId(FAKE_INT);
        exercise.setName(FAKE_STRING);
        exercise.setCategoryId(FAKE_INT);
        exercise.setIncrement(FAKE_DOUBLE);
        exercise.setRestAutoStart(FAKE_BOOLEAN_TRUE);
        exercise.setRestSound(FAKE_BOOLEAN_TRUE);
        exercise.setRestVibrate(FAKE_BOOLEAN_TRUE);
        exercise.setRestTimer(FAKE_INT);
        exercise.setType(FAKE_INT);
        exercise.setDeleteDate(date);
        exercise.setDeleted(FAKE_BOOLEAN_TRUE);


        // All asserts should be true
        assertThat(exercise.getId(), is(FAKE_INT));
        assertThat(exercise.getName(), is(FAKE_STRING));
        assertThat(exercise.getCategoryId(), is(FAKE_INT));
        assertThat(exercise.getIncrement(), is(FAKE_DOUBLE));
        assertThat(exercise.getRestTimer(), is(FAKE_INT));
        assertThat(exercise.isRestAutoStart(), is(FAKE_BOOLEAN_TRUE));
        assertThat(exercise.isRestSound(), is(FAKE_BOOLEAN_TRUE));
        assertThat(exercise.isRestVibrate(), is(FAKE_BOOLEAN_TRUE));
        assertThat(exercise.getType(), is(FAKE_INT));
        assertThat(exercise.getDeleteDate(), is(date));


        //Check if deleted, then remove delete date, then check again
        assertThat(exercise.isDeleted(), is(FAKE_BOOLEAN_TRUE));
        exercise.setDeleted(FAKE_BOOLEAN_FALSE);
        assertThat(exercise.isDeleted(), is(FAKE_BOOLEAN_FALSE));
    }
}
