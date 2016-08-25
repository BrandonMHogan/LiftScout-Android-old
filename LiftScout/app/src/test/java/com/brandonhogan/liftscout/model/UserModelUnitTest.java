package com.brandonhogan.liftscout.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.brandonhogan.liftscout.core.model.User;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserModelUnitTest {

    private static final String FAKE_APP_STRING = "Lift Scout";

    private static final double DELTA = 1e-15;

    @Test
    public void weight_isCorrect() throws Exception {
        double weight = 200;

        User user = new User();
        user.setWeight(weight);

        assertEquals(user.getWeight(), weight, DELTA);
    }

    @Test
    public void weight_massive_isCorrect() throws Exception {
        float weight = 20000000000000f;

        User user = new User();
        user.setWeight(weight);

        assertEquals(user.getWeight(), weight, DELTA);
    }
}