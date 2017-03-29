package com.brandonhogan.liftscout.model;

import com.brandonhogan.liftscout.repository.model.UserSetting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.brandonhogan.liftscout.Utils.constants.FAKE_BOOLEAN_TRUE;
import static com.brandonhogan.liftscout.Utils.constants.FAKE_STRING;
import static com.brandonhogan.liftscout.Utils.constants.FAKE_STRING_OTHER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class UserSettingModelUnitTest {

    @Test
    public void UserSettingModel_CorrectProperties_assertTrue() {
        UserSetting userSetting = new UserSetting();

        userSetting.setName(FAKE_STRING);
        userSetting.setValue(FAKE_STRING_OTHER);

        assertThat(userSetting.getName(), is(FAKE_STRING));
        assertThat(userSetting.getValue(), is(FAKE_STRING_OTHER));

        userSetting.setValue(FAKE_BOOLEAN_TRUE);
        assertThat(userSetting.getValueBoolean(), is(FAKE_BOOLEAN_TRUE));
    }
}
