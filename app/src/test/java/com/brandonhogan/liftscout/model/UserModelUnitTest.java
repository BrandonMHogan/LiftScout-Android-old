package com.brandonhogan.liftscout.model;

import android.content.Context;

import com.brandonhogan.liftscout.repository.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static com.brandonhogan.liftscout.Utils.constants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserModelUnitTest {


    @Mock
    Context mMockContext;


    @Test
    public void UserModel_CorrectProperties_assertTrue() {
        User user = new User();

        java.util.Date date = new java.util.Date();

        user.setId(FAKE_INT);
        user.setName(FAKE_STRING);
        user.setBirthDate(date);
        user.setLastUsed(date);
        user.setStartDate(date);
        user.setWeight(FAKE_DOUBLE);

        // ...then the result should be the expected one.
        assertThat(user.getId(), is(FAKE_INT));
        assertThat(user.getName(), is(FAKE_STRING));
        assertThat(user.getBirthDate().toString(), is(date.toString()));
        assertThat(user.getLastUsed().toString(), is(date.toString()));
        assertThat(user.getStartDate().toString(), is(date.toString()));
        assertThat(user.getWeight(), is(FAKE_DOUBLE));

        assertThat(user.getAge(), is(0));
    }

    @Test
    public void UserModel_CorrectAge_assertTrue() {

        final int AGE = 12;

        User user = new User();
        Calendar now = Calendar.getInstance();

        // Subtract age from current date.
        now.add(Calendar.YEAR, -AGE);
        user.setBirthDate(now.getTime());

        // Age should return AGE
        assertThat(user.getAge(), is(AGE));
    }
}