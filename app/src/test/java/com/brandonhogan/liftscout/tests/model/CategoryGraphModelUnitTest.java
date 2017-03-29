package com.brandonhogan.liftscout.tests.model;

import com.brandonhogan.liftscout.repository.model.CategoryGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.utils.constants.FAKE_INT;
import static com.brandonhogan.liftscout.utils.constants.FAKE_INT_OTHER;
import static com.brandonhogan.liftscout.utils.constants.FAKE_STRING;
import static com.brandonhogan.liftscout.utils.constants.FAKE_STRING_OTHER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class CategoryGraphModelUnitTest {

    @Test
    public void CategoryGraphModel_CorrectProperties_assertTrue() {
        CategoryGraph categoryGraph = new CategoryGraph(FAKE_STRING, FAKE_INT, FAKE_INT);

        assertThat(categoryGraph.getName(), is(FAKE_STRING));
        assertThat(categoryGraph.getColor(), is(FAKE_INT));
        assertThat(categoryGraph.getValue(), is(FAKE_INT));

        categoryGraph.setName(FAKE_STRING_OTHER);
        categoryGraph.setColor(FAKE_INT_OTHER);
        categoryGraph.setValue(FAKE_INT_OTHER);

        assertThat(categoryGraph.getName(), is(FAKE_STRING_OTHER));
        assertThat(categoryGraph.getColor(), is(FAKE_INT_OTHER));
        assertThat(categoryGraph.getValue(), is(FAKE_INT_OTHER));
    }

}
