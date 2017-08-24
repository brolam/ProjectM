package br.com.brolam.projectm;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.HashMap;
import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.JobApplication;
import br.com.brolam.projectm.data.models.UserAccount;
import br.com.brolam.projectm.data.models.UserProperties;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DataBaseProviderTest implements ValueEventListener {
    private static String TAG = "DataBaseProviderTest";
    private FirebaseUser firebaseUser;
    private HashMap<String, Object> expectedUserProperties = null;
    private HashMap<String, Object> expectedUserAccount = null;
    private HashMap<String, Object> expectedJobApplications = null;
    private boolean isSetExpectedUserProperties;
    private boolean isSetExpectedUserAccount = false;
    private boolean isSetExpectedJobApplication = false;
    private boolean isSetupCompleted = false;

    @Before
    public void setup() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> firebaseAuthTask = firebaseAuth.createUserWithEmailAndPassword(SignUpActivityTest.USER_EMAIL, SignUpActivityTest.USER_PASSWORD);
        firebaseAuthTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DataBaseProviderTest.this.firebaseUser = firebaseAuth.getCurrentUser();
                DataBaseProviderTest.this.isSetupCompleted = true;
            }
        });
    }

    @Test
    public void setUserProperties() throws Exception {
        while (this.isSetupCompleted == false){
            Log.i(TAG, "Setup is not completed");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DataBaseProvider dataBaseProvider = new DataBaseProvider(this.firebaseUser);
        dataBaseProvider.addUserPropertiesListener(this);
        HashMap<String, Object> userProperties = UserProperties.getNewUserProperties("User", "Test");
        dataBaseProvider.setUserProperties(userProperties);
        while (this.isSetExpectedUserProperties == false){
            Log.i(TAG, "The expectedUserProperties UserProperties is not Attributed!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(this.expectedUserProperties, userProperties);
    }

    @Test
    public void setUserAccountType() throws Exception {
        while (this.isSetupCompleted == false){
            Log.i(TAG, "Setup is not completed");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DataBaseProvider dataBaseProvider = new DataBaseProvider(this.firebaseUser);
        dataBaseProvider.addUserAccountListener(this);
        HashMap<String, Object> userAccount = UserAccount.getNewUserAccount(UserAccount.AccountTypes.PREMIUM);
        dataBaseProvider.setUserAccount(userAccount);
        while (this.isSetExpectedUserAccount == false){
            Log.i(TAG, "The expectedUserAccount is not Attributed!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(this.expectedUserAccount, userAccount);
    }

    @Test
    public void setJobApplication(){
        while (this.isSetupCompleted == false){
            Log.i(TAG, "Setup is not completed");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String userKey = "5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        String jobKey = "5PQBNu6346Tz3eqsOzSUZOuDEiv3";
        Long applicationDate = new Date().getTime();
        HashMap newApplication = JobApplication.getNewApplication(userKey, jobKey, applicationDate);
        DataBaseProvider dataBaseProvider = new DataBaseProvider(this.firebaseUser);
        //dataBaseProvider.addJobApplicationListener(this);
        //dataBaseProvider.setJobApplication(newApplication);
        assertTrue(this.expectedJobApplications.containsKey(jobKey));
        HashMap expectedJobApplication = (HashMap) this.expectedJobApplications.get(jobKey);
        assertEquals(expectedJobApplication.get(JobApplication.APPLICATION_DATE), applicationDate);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String referenceFullPath = dataSnapshot.getRef().toString();
        if ( referenceFullPath.contains(UserProperties.REFERENCE_NAME)) {
            this.isSetExpectedUserProperties = true;
            this.expectedUserProperties = (HashMap<String, Object>) dataSnapshot.getValue();
        } else if ( referenceFullPath.contains(UserAccount.REFERENCE_NAME)) {
            this.isSetExpectedUserAccount = true;
            this.expectedUserAccount = (HashMap<String, Object>) dataSnapshot.getValue();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        this.isSetExpectedUserProperties = true;
        this.isSetExpectedUserAccount = true;
    }
}
