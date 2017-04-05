package com.brandonhogan.liftscout.tests.presenters;

import com.brandonhogan.liftscout.interfaces.contracts.SettingsProfileContract;
import com.brandonhogan.liftscout.presenters.SettingsProfilePresenter;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Brandon on 4/5/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class SettingsProfilePresenterTest {

    private SettingsProfilePresenter settingsProfilePresenter;

    @Mock
    private SettingsProfileContract.View mockView;

    @Before
    public void setUp() {
      //  settingsProfilePresenter = new SettingsProfilePresenter(mockView);
      //  userDetailsPresenter.setView(mockUserDetailsView);
    }

}
