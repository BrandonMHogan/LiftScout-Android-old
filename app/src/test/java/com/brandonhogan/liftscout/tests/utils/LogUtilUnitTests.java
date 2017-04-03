package com.brandonhogan.liftscout.tests.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.brandonhogan.liftscout.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static com.brandonhogan.liftscout.utils.constants.FAKE_STRING;
import static com.brandonhogan.liftscout.utils.constants.FAKE_STRING_OTHER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */


@RunWith(MockitoJUnitRunner.class)
public class LogUtilUnitTests {

    @NonNull
    private Bundle mockBundle() {
        final Map<String, String> fakeBundle = new HashMap<>();
        Bundle bundle = mock(Bundle.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                String key = ((String) arguments[0]);
                String value = ((String) arguments[1]);
                fakeBundle.put(key, value);
                return null;
            }
        }).when(bundle).putString(anyString(), anyString());
        when(bundle.get(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                String key = ((String) arguments[0]);
                return fakeBundle.get(key);
            }
        });
        return bundle;
    }

    @Test
    public void LogUtil_Runs_assertTrue() {

        String booleanKey = "Boolean Key";

        Bundle bundle = mockBundle();
        bundle.putString(FAKE_STRING, FAKE_STRING_OTHER);


        assertThat(LogUtil.printIntents(bundle, FAKE_STRING), is(true));
    }
}
