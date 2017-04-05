package com.brandonhogan.liftscout.tests.presenters;


import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.AppComponent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.presenters.SettingsProfilePresenter;

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
public class SettingsProfilePresenterTest {

    private SettingsProfilePresenter settingsProfilePresenter;

    @Mock
    private SettingsProfileContract.View mockView;

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
        when(userManager.getMeasurementValue()).thenReturn("Pounds");

        // Creates,
        settingsProfilePresenter = new SettingsProfilePresenter();
        settingsProfilePresenter.setUserManager(userManager);
    }

    @Test
    public void test_setView() {
        ArrayList<String> measurements = new ArrayList<>();
        measurements.add("Pounds");
        measurements.add("Kilograms");

        settingsProfilePresenter.setView(mockView);
        verify(mockView, times(1)).populateMeasurements(measurements, measurements.indexOf(measurements.get(0)));
    }

    @Test
    public void test_onMeasurementSelected() {
        settingsProfilePresenter.setView(mockView);
        settingsProfilePresenter.onMeasurementSelected(0);
        verify(mockView, times(1)).saveSuccess(R.string.setting_profile_measurement_saved);
    }

    @Test
    public void test_onDestroy() {
        settingsProfilePresenter.setView(mockView);
        settingsProfilePresenter.onDestroy();
    }
}
