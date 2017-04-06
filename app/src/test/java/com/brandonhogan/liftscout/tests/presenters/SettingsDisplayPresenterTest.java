package com.brandonhogan.liftscout.tests.presenters;

import com.brandonhogan.liftscout.injection.components.AppComponent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsDisplayContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.presenters.SettingsDisplayPresenter;
import com.brandonhogan.liftscout.utils.constants.Themes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Injector.class, UserManager.class})
public class SettingsDisplayPresenterTest {

    private SettingsDisplayPresenter settingsDisplayPresenter;

    @Mock
    private SettingsDisplayContract.View mockView;

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
        when(userManager.getThemeValue()).thenReturn(Themes.ORIGINAL_LIGHT);

        // Creates,
        settingsDisplayPresenter = new SettingsDisplayPresenter();
        settingsDisplayPresenter.setUserManager(userManager);
    }

    @Test
    public void test_setView() {
        ArrayList<String> themes = Themes.THEMES;
        settingsDisplayPresenter.setView(mockView);
        verify(mockView, times(1)).populateThemes(themes, themes.indexOf((Themes.ORIGINAL_LIGHT)));
    }

    @Test
    public void test_changeTheme() {
        settingsDisplayPresenter.setView(mockView);
        settingsDisplayPresenter.changeTheme();
        verify(mockView, times(1)).saveSuccess(true);
    }

    @Test
    public void test_onThemeSelected() {
        settingsDisplayPresenter.setView(mockView);

        settingsDisplayPresenter.onThemeSelected(Themes.THEMES.indexOf(Themes.ORIGINAL_LIGHT));
        verify(mockView, times(0)).showAlert();

        settingsDisplayPresenter.onThemeSelected(Themes.THEMES.indexOf(Themes.ORIGINAL_DARK));
        verify(mockView, times(1)).showAlert();
    }

    @Test
    public void test_getOriginalThemeIndex() {
        settingsDisplayPresenter.setView(mockView);

        int index = settingsDisplayPresenter.getOriginalThemeIndex();
        assertEquals(index, Themes.THEMES.indexOf(Themes.ORIGINAL_LIGHT));
    }

    @Test
    public void test_onDestroy() {
        settingsDisplayPresenter.setView(mockView);
        settingsDisplayPresenter.onDestroy();
    }

    @Test
    public void test_onResume() {
        settingsDisplayPresenter.setView(mockView);
        settingsDisplayPresenter.onResume();
    }

    @Test
    public void test_onPause() {
        settingsDisplayPresenter.setView(mockView);
        settingsDisplayPresenter.onPause();
    }
}
