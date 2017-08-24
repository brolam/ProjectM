package br.com.brolam.projectm;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class JobActivityTest extends SignUpActivityTest {

    @Override
    public void doTest() throws InterruptedException {
        super.doTest();
        Espresso.onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }
}
