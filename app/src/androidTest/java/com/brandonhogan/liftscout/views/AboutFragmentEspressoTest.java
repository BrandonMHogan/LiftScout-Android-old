package com.brandonhogan.liftscout.views;

import android.content.Context;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.activities.MainActivity;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.managers.UserManager;
import com.brandonhogan.liftscout.repository.model.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutFragmentEspressoTest {

    UserManager userManager;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void goToAbout() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("About"))
                .perform(click());

    }


    @Test
    public void checkLabelValues() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("About"))
                .perform(click());

        onView(ViewMatchers.withId(R.id.version_number))
                .check(matches(ViewMatchers.withText(BuildConfig.VERSION_NAME)));

        onView(withId(R.id.contact_email))
                .check(matches(withText(R.string.app_feedback)));

        onView(withId(R.id.developer_name))
                .check(matches(withText(R.string.app_developer)));

        onView(withId(R.id.designer_name))
                .check(matches(withText(R.string.app_designer)));
    }

}
