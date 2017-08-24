package br.com.brolam.projectm;

import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class JobApplicationActivityTest extends JobActivityTest {

    @Override
    public void doTest() throws InterruptedException {
        super.doTest();
        ViewInteraction buttonToApply = onView(allOf(withId(R.id.buttonToApply),isDisplayed()));
        clickAndWait(buttonToApply);

        ViewInteraction viewJobToApplyLayout = onView(withId(R.id.job_to_apply_layout));
        viewJobToApplyLayout.check(matches(isDisplayed()));

        ViewInteraction applicationByPhoneOptionButton = onView(allOf(withId(R.id.applicationByPhoneOption),isDisplayed()));
        clickAndWait(applicationByPhoneOptionButton);

    }
}