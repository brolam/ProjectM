package br.com.brolam.projectm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.UserProperties;

/**
 * A login screen that offers login via email/password.
 */
public class PricingActivity extends AppCompatActivity implements OnClickListener {
    private static String TAG = "SignUpActivity";
    private FirebaseAuth firebaseAuth;

    // UI references.
    private RadioButton optingFree;
    private RadioButton optingPremium;
    Button nextButton;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        this.firebaseAuth = FirebaseAuth.getInstance();
        // Set up the login form.
        this.optingFree = (RadioButton) findViewById(R.id.optionFree);
        this.optingPremium = (RadioButton) findViewById(R.id.optionPremium);

        this.nextButton = (Button) findViewById(R.id.nextButton);
        this.nextButton.setOnClickListener(this);

        mProgressView = findViewById(R.id.login_progress);
    }

    public static void select(Activity activity, int requestCode){
        Intent intent = new Intent(activity, PricingActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        if ( this.nextButton.equals(view)){
         parsePriceSelected();
        }
    }

    private void parsePriceSelected() {
        this.setResult(RESULT_OK);
        this.finish();
    }

    /*
    private void createUser(String email, String password, final String surname) {
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                DataBaseProvider dataBaseProvider = new DataBaseProvider(firebaseAuth.getCurrentUser());
                                dataBaseProvider.setUserProperties(UserProperties.getNewUserProperties(surname));
                                Log.d(TAG, "createUserWithEmail:success");
                                PricingActivity.this.finish();
                            }
                        } finally {
                            showProgress(false);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                showProgress(false);
                mEmailView.setError(getString(R.string.error_incorrect_email_or_password));
                mEmailView.requestFocus();
            }
        });
    }
*/

    /**
     * Shows the progress UI and hides the login form.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    */


}

