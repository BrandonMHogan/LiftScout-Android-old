package com.brandonhogan.liftscout.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;
import android.content.SharedPreferences;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.repository.model.Category;
import com.brandonhogan.liftscout.repository.model.User;

/**
 * Created by Brandon on 3/24/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class CategoryModelUnitTest {

    private static final String FAKE_STRING = "Lift Scout";

    @Mock
    Context mMockContext;

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        // Given a mocked Context injected into the object under test...
        when(mMockContext.getString(R.string.app_name))
                .thenReturn(FAKE_STRING);
        Category myCategory = new Category();
        myCategory.setName("Lift Scout");

        // ...when the string is returned from the object under test...
        String result = myCategory.getName();

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_STRING));
    }
}
