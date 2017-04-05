package com.brandonhogan.liftscout.tests.model;

import com.brandonhogan.liftscout.repository.model.Record;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_BOOLEAN_TRUE;
import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_DOUBLE;
import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_INT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class RecordModelUnitTest {

    @Test
    public void RecordModel_CorrectProperties_assertTrue() {
        Record record = new Record();
        java.util.Date date = new java.util.Date();

        record.setRepId(FAKE_INT);
        record.setExerciseId(FAKE_INT);
        record.setDate(date);
        record.setRecord(FAKE_BOOLEAN_TRUE);
        record.setRepRange(FAKE_INT);
        record.setRepWeight(FAKE_DOUBLE);


        assertThat(record.getRepId(), is(FAKE_INT));
        assertThat(record.getExerciseId(), is(FAKE_INT));
        assertThat(record.getDate(), is(date));
        assertThat(record.isRecord(), is(FAKE_BOOLEAN_TRUE));
        assertThat(record.getRepRange(), is(FAKE_INT));
        assertThat(record.getRepWeight(), is(FAKE_DOUBLE));
    }
}
