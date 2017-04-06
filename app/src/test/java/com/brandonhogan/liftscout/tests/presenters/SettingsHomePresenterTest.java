package com.brandonhogan.liftscout.tests.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.AppComponent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsHomeContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.presenters.SettingsHomePresenter;
import com.brandonhogan.liftscout.utils.constants.DefaultScreens;
import com.brandonhogan.liftscout.utils.constants.TodayTransforms;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Injector.class, UserManager.class})
public class SettingsHomePresenterTest {

    private SettingsHomePresenter settingsHomePresenter;

    @Mock
    private SettingsHomeContract.View mockView;

    @Mock
    AppComponent appComponent;

    @Mock
    UserManager userManager;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mocks the injector
        PowerMockito.mockStatic(Injector.class);
        BDDMockito.given(Injector.getAppComponent()).willReturn(appComponent);

        // Mocks out the userManager
        userManager = mock(UserManager.class);
        when(userManager.getHomeDefaultValue()).thenReturn(DefaultScreens.TODAY);
        when(userManager.getTransformValue()).thenReturn(TodayTransforms.DEFAULT);

        // Creates,
        settingsHomePresenter = new SettingsHomePresenter();
        settingsHomePresenter.setUserManager(userManager);
    }

    @Test
    public void test_setView() {
        settingsHomePresenter.setView(mockView);
        verify(mockView, times(1)).populateHomeDefaults(DefaultScreens.SCREENS, DefaultScreens.SCREENS.indexOf(DefaultScreens.TODAY));
        verify(mockView, times(1)).populateTransforms(TodayTransforms.TRANSFORMS, TodayTransforms.TRANSFORMS.indexOf(TodayTransforms.DEFAULT));
    }

    @Test
    public void test_onTransformSelected() {
        settingsHomePresenter.setView(mockView);

        settingsHomePresenter.onTransformSelected(TodayTransforms.TRANSFORMS.indexOf(TodayTransforms.BOUNCE));
        verify(mockView, times(1)).saveSuccess(R.string.setting_home_transform_saved);

        settingsHomePresenter.onTransformSelected(TodayTransforms.TRANSFORMS.indexOf(TodayTransforms.DEFAULT));
        verify(mockView, times(2)).saveSuccess(R.string.setting_home_transform_saved);
    }

    @Test
    public void test_onHomeDefaultSelected() {
        settingsHomePresenter.setView(mockView);

        settingsHomePresenter.onHomeDefaultSelected(DefaultScreens.SCREENS.indexOf(DefaultScreens.TODAY));
        verify(mockView, times(1)).saveSuccess(R.string.setting_home_screen_saved);

        settingsHomePresenter.onHomeDefaultSelected(DefaultScreens.SCREENS.indexOf(DefaultScreens.CALENDAR));
        verify(mockView, times(2)).saveSuccess(R.string.setting_home_screen_saved);
    }
}
