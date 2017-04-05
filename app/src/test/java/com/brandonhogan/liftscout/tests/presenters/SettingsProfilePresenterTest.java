package com.brandonhogan.liftscout.tests.presenters;

import android.app.Application;

import com.brandonhogan.liftscout.injection.components.AppComponent;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.presenters.SettingsProfilePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.observers.DisposableObserver;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class SettingsProfilePresenterTest {

    private SettingsProfilePresenter settingsProfilePresenter;

    @Mock
    private SettingsProfileContract.View mockView;

    @Mock Injector injector;
    @Mock AppComponent appComp;
    @Mock
    Application application;


    @Before
    public void setUp() {
        //Injector.initComponents(application);

//        when(appComp).getMock();
//
//        settingsProfilePresenter = new SettingsProfilePresenter();
//        settingsProfilePresenter.setView(mockView);
    }

    @Test
    public void testEmpty() throws Exception {
//        verify(settingsProfilePresenter).setView(mockView);
    }
}
