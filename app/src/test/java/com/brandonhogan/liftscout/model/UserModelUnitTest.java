package com.brandonhogan.liftscout.model;

import android.content.Context;

import com.brandonhogan.liftscout.repository.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserModelUnitTest {

    private static final String FAKE_STRING = "Fake Category Name";
    private static final int FAKE_INT = 123456;
    private static final double FAKE_DOUBLE = 1231.23;

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
    }
}