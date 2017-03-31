package com.brandonhogan.liftscout.views;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import com.brandonhogan.liftscout.BuildConfig;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Brandon on 3/29/2017.
 * Description :
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutFragmentEspressoTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivity = new IntentsTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void goToAbout() {

        final MainActivity activity = mActivity.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);


        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("About"))
                .perform(click());
    }

    @Test
    public void checkLabelValues() {
        onView(ViewMatchers.withId(R.id.version_number))
                .check(matches(ViewMatchers.withText(BuildConfig.VERSION_NAME)));

        onView(withId(R.id.contact_email))
                .perform(scrollTo())
                .check(matches(withText(R.string.app_feedback)));

        onView(withId(R.id.developer_name))
                .perform(scrollTo())
                .check(matches(withText(R.string.app_developer)));

        onView(withId(R.id.designer_name))
                .perform(scrollTo())
                .check(matches(withText(R.string.app_designer)));
    }

    @Test
    public void testDeveloperIntent() {
        String url = mActivity.getActivity().getString(R.string.app_developer_website);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(toPackage("com.android.chrome")).respondWith(result);

        onView(withId(R.id.developer_container))
                .perform(scrollTo());

//        onView(ViewMatchers.withId(R.id.developer_container))
//                .perform(click());

        onView(withId(R.id.title_icon))
                .perform(scrollTo(), click());
    }

    @Test
    public void testDesignerIntent() {
        String url = mActivity.getActivity().getString(R.string.app_designer_website);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(toPackage("com.android.chrome")).respondWith(result);

        onView(withId(R.id.designer_container))
                .perform(scrollTo());

//        onView(ViewMatchers.withId(R.id.designer_container))
//                .perform(click());

        onView(withId(R.id.title_icon))
                .perform(scrollTo(), click());
    }

}
