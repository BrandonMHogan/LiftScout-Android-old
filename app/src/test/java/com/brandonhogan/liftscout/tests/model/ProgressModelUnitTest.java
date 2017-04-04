package com.brandonhogan.liftscout.tests.model;

import com.brandonhogan.liftscout.repository.model.Progress;
import com.brandonhogan.liftscout.repository.model.Set;
import com.brandonhogan.liftscout.utils.DateUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.realm.RealmList;

import static com.brandonhogan.liftscout.utils.constants.FAKE_DOUBLE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class ProgressModelUnitTest {

    @Test
    public void ProgressModel_CorrectProperties_assertTrue() {
        Progress progress = new Progress();

        java.util.Date date = new java.util.Date();
        RealmList<Set> sets = new RealmList<>();

        progress.setId(date.getTime());
        progress.setDate(date);
        progress.setWeight(FAKE_DOUBLE);
        progress.setSets(sets);

        assertThat(progress.getId(), is(date.getTime()));
        assertThat(progress.getDate(), is(DateUtil.trimTimeFromDate(date)));
        assertThat(progress.getWeight(), is(FAKE_DOUBLE));
        assertThat(progress.getSets(), is(sets));
    }
}
