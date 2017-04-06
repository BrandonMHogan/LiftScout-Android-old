package com.brandonhogan.liftscout.tests.repository.model;

import android.content.Context;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.repository.model.Category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_INT;
import static com.brandonhogan.liftscout.utils.UnitTestConstants.FAKE_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Brandon on 3/24/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class CategoryModelUnitTest {


    @Mock
    Context mMockContext;

    @Test
    public void CategoryModel_CorrectId_ReturnsTrue() {
        Category myCategory = new Category();
        myCategory.setId(FAKE_INT);

        // ...when the string is returned from the object under test...
        int result = myCategory.getId();

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_INT));
    }

    @Test
    public void CategoryModel_CorrectName_ReturnsTrue() {
        // Given a mocked Context injected into the object under test...
        when(mMockContext.getString(R.string.category_abs))
                .thenReturn(FAKE_STRING);

        Category myCategory = new Category();
        myCategory.setName(mMockContext.getString(R.string.category_abs));

        // ...when the string is returned from the object under test...
        String result = myCategory.getName();

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_STRING));
    }

    @Test
    public void CategoryModel_CorrectColor_ReturnsTrue() {
        Category myCategory = new Category();
        myCategory.setColor(FAKE_INT);

        // ...when the string is returned from the object under test...
        int result = myCategory.getColor();

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_INT));
    }

    @Test
    public void CategoryModel_isDeleted() {
        Category myCategory = new Category();

        myCategory.setDeleted(true);
        assertThat(myCategory.isDeleted(), is(true));

        myCategory.setDeleted(false);
        assertThat(myCategory.isDeleted(), is(false));

        Date date = new Date();
        myCategory.setDeleteDate(date);
        assertThat(myCategory.getDeleteDate(), is(date));

        myCategory.setDeletable(false);
        assertThat(myCategory.isDeletable(), is(false));

        myCategory.setDeletable(true);
        assertThat(myCategory.isDeletable(), is(true));
    }
}
