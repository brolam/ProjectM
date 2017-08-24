package br.com.brolam.projectm.base;

import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by brenomar on 24/08/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActivityBaseTest {


    protected void hideKeyboard() throws InterruptedException {
        pressBack();
        Thread.sleep(1000);
    }
    protected void checkIsDisplayed(ViewInteraction viewInteraction) throws InterruptedException {
        viewInteraction.check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    protected void setTextAndWait(ViewInteraction viewInteraction, String text) throws InterruptedException {
        viewInteraction.perform(replaceText(text), closeSoftKeyboard());
        Thread.sleep(1000);
    }

    protected void clickAndWait(ViewInteraction viewInteraction) throws InterruptedException {
        viewInteraction.perform(click());
        Thread.sleep(1000);
    }
}
