package com.brandonhogan.liftscout.tests.presenters;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.AppComponent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.IntroThemesContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.presenters.IntroThemesPresenter;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brandon on 4/6/2017.
 * Description :
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Injector.class, UserManager.class})
public class IntroThemesPresenterTest {

    private IntroThemesPresenter presenter;

    @Mock
    private IntroThemesContract.View mockView;

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
        presenter = new IntroThemesPresenter();
        presenter.setUserManager(userManager);
    }

    @Test
    public void test_setView() {
        presenter.setView(mockView);

        verify(mockView, times(1)).populateThemes(Themes.THEMES, Themes.THEMES.indexOf((Themes.ORIGINAL_LIGHT)));
    }

    @Test
    public void test_onThemeSelected() {
        presenter.setView(mockView);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.ORIGINAL_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Original_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.GREEN_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Green_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.PURPLE_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Purple_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.TEAL_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Teal_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.BLUE_GRAY_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Blue_Gray_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.BLACK_DARK)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Black_Dark);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.ORIGINAL_LIGHT)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Original_Light);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.GREEN_LIGHT)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Green_Light);

        presenter.onThemeSelected(Themes.THEMES.indexOf((Themes.PURPLE_LIGHT)));
        verify(mockView, times(1)).themeSelected(R.style.AppTheme_Purple_Light);
    }

    @Test
    public void test_onDestroy() {
        presenter.setView(mockView);
        presenter.onDestroy();
    }

    @Test
    public void test_onResume() {
        presenter.setView(mockView);
        presenter.onResume();
    }

    @Test
    public void test_onPause() {
        presenter.setView(mockView);
        presenter.onPause();
    }
}
