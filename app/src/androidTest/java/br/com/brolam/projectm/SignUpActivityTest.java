package br.com.brolam.projectm;


import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.brolam.projectm.base.ActivityBaseTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest extends ActivityBaseTest implements OnCompleteListener<Void> {

    private static final String TAG = "SignUpActivityTest";
    private static final String USER_NAME = "User";
    private static final String USER_SURNAME = "Test";
    public static final String USER_EMAIL = "test@brolam.com.br";
    public static final String USER_PASSWORD = "123.test";
    private FirebaseAuth firebaseAuth;
    private boolean isSetupCompleted = true;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        this.firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> firebaseAuthTask = firebaseAuth.signInWithEmailAndPassword(USER_EMAIL, USER_PASSWORD);
        firebaseAuthTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful()){
                    FirebaseUser userTest = firebaseAuth.getCurrentUser();
                    userTest.delete().addOnCompleteListener(SignUpActivityTest.this);
                } else {
                    SignUpActivityTest.this.isSetupCompleted = true;
                }
            }
        });
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        this.isSetupCompleted = true;
    }

    @Test
    public void doTest() throws InterruptedException {

        while (this.isSetupCompleted == false){
            Log.i(TAG, "Setup is not completed");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hideKeyboard();

        ViewInteraction signUpButton = onView(allOf(withId(R.id.sign_up_button)));
        clickAndWait(signUpButton);

        ViewInteraction inputName = onView(allOf(withId(R.id.name), isDisplayed()));
        setTextAndWait(inputName, USER_NAME);

        ViewInteraction inputSurname = onView(allOf(withId(R.id.surname), isDisplayed()));
        setTextAndWait(inputSurname, USER_SURNAME);

        ViewInteraction inputEmail = onView(allOf(withId(R.id.email), isDisplayed()));
        setTextAndWait(inputEmail, USER_EMAIL);

        ViewInteraction inputPassword = onView(allOf(withId(R.id.password), isDisplayed()));
        setTextAndWait(inputPassword, USER_PASSWORD);

        ViewInteraction nextButton = onView(allOf(withId(R.id.nextButton),isDisplayed()));
        clickAndWait(nextButton);

        ViewInteraction viewPricingActivity = onView(withId(R.id.activity_pricing_layout));
        checkIsDisplayed(viewPricingActivity);

        ViewInteraction premiumRadioButton = onView(allOf(withId(R.id.optionPremium),isDisplayed()));
        clickAndWait(premiumRadioButton);

        nextButton = onView(allOf(withId(R.id.nextButton),isDisplayed()));
        clickAndWait(nextButton);

        ViewInteraction viewMainActivity = onView(withId(R.id.activity_main_layout));
        viewMainActivity.check(matches(isDisplayed()));

    }
}
