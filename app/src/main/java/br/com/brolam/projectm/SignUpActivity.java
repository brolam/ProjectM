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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.UserProperties;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {
    private static int REQUEST_CODE_PRICING_SELECT = 100;
    private static String TAG = "SignUpActivity";
    private FirebaseAuth firebaseAuth;

    // UI references.
    private EditText mNameView;
    private EditText mSurNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        this.firebaseAuth = FirebaseAuth.getInstance();
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name);
        mSurNameView = (EditText) findViewById(R.id.surname);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    parseUser();
                    return true;
                }
                return false;
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                parseUser();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public static void doRegister(Activity activity, int requestCode){
        Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void parseUser() {
        // Reset errors.
        mNameView.setError(null);
        mSurNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name  = mNameView.getText().toString();
        String surname  = mSurNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name
        if (!UserProperties.isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            if ( focusView == null) focusView = mNameView;
            cancel = true;
        }

        // Check for a valid surname
        if (!UserProperties.isSurnameValid(surname)) {
            mSurNameView.setError(getString(R.string.error_invalid_surname));
            if ( focusView == null) focusView = mSurNameView;
            cancel = true;
        }

        // Check for a valid Email
        if (!UserProperties.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            if ( focusView == null) focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid Password.
        if ( !UserProperties.isPasswordValid (password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            if ( focusView == null) focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            createUser(email, password, surname);
        }
    }

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
                                SignUpActivity.this.setResult(RESULT_OK);
                                SignUpActivity.this.selectPrice();
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

    private void selectPrice() {
        PricingActivity.select(this, REQUEST_CODE_PRICING_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == REQUEST_CODE_PRICING_SELECT) this.finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

}

