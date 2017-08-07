package br.com.brolam.projectm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.UserAccount;
import br.com.brolam.projectm.data.models.UserProperties;

/**
 * A login screen that offers login via email/password.
 */
public class PricingActivity extends AppCompatActivity implements OnClickListener {
    private static String TAG = "SignUpActivity";
    private FirebaseAuth firebaseAuth;
    DataBaseProvider dataBaseProvider;

    // UI references.
    private View form;
    private RadioButton optingFree;
    private RadioButton optingPremium;
    Button nextButton;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.form = findViewById(R.id.form);
        this.optingFree = (RadioButton) findViewById(R.id.optionFree);
        this.optingPremium = (RadioButton) findViewById(R.id.optionPremium);

        this.nextButton = (Button) findViewById(R.id.nextButton);
        this.nextButton.setOnClickListener(this);

        mProgressView = findViewById(R.id.login_progress);
        FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            this.setResult(RESULT_CANCELED);
            this.finish();
        } else {
            this.dataBaseProvider = new DataBaseProvider(firebaseUser);
        }
    }

    public static void select(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PricingActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        if (this.nextButton.equals(view)) {
            parsePriceSelected();
        }
    }

    private void parsePriceSelected() {
        UserAccount.AccountTypes accountType = getSelectedAccountType();
        if (accountType == UserAccount.AccountTypes.UNDEFINED) {
            Snackbar.make(this.nextButton, R.string.pricing_activity_select_account_type, Snackbar.LENGTH_LONG)
                    .setAction(R.string.app_name, null).show();
        } else
            setUserAccountType(accountType);
    }


    private void setUserAccountType(UserAccount.AccountTypes selectedAccountType) {
        HashMap<String, Object> newUserAccount = UserAccount.getNewUserAccount(selectedAccountType);
        showProgress(true);
        this.dataBaseProvider.setUserAccount(newUserAccount);
        this.dataBaseProvider.addUserAccountListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PricingActivity.this.setResult(RESULT_OK);
                PricingActivity.this.showProgress(false);
                PricingActivity.this.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                PricingActivity.this.showProgress(false);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            this.form.setVisibility(show ? View.GONE : View.VISIBLE);
            this.form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
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
            this.form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public UserAccount.AccountTypes getSelectedAccountType() {
        if (this.optingFree.isChecked())
            return UserAccount.AccountTypes.FREE;
        else if (this.optingPremium.isChecked())
            return UserAccount.AccountTypes.PREMIUM;
        else
            return UserAccount.AccountTypes.UNDEFINED;
    }
}

