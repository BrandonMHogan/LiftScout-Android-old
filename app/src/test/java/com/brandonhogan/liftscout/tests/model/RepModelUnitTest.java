package com.brandonhogan.liftscout.tests.model;

import com.brandonhogan.liftscout.repository.model.Rep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_DOUBLE;
import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_INT;
import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class RepModelUnitTest {

    @Test
    public void RepModel_CorrectProperties_assertTrue() {
        Rep rep = new Rep();

        rep.setId(FAKE_INT);
        rep.setCount(FAKE_INT);
        rep.setWeight(FAKE_DOUBLE);
        rep.setNote(FAKE_STRING);

        assertThat(rep.getId(), is(FAKE_INT));
        assertThat(rep.getCount(), is(FAKE_INT));
        assertThat(rep.getWeight(), is(FAKE_DOUBLE));
        assertThat(rep.getNote(), is(FAKE_STRING));
    }
}
