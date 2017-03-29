package com.brandonhogan.liftscout.tests.utils;

import android.os.Bundle;

import com.brandonhogan.liftscout.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.utils.constants.FAKE_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */


@RunWith(MockitoJUnitRunner.class)
public class LogUtilUnitTests {

    @Mock
    Bundle bundle;

    @Test
    public void LogUtil_Runs_assertTrue() {
        assertThat(LogUtil.printIntents(bundle, FAKE_STRING), is(true));
    }
}
