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

import java.util.Calendar;
import java.util.HashMap;

import br.com.brolam.projectm.data.DataBaseProvider;
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
    private boolean isAttributedExpectedUserProperties = false;
    private boolean isSetupCompleted = false;

    @Before
    public void setup(){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> firebaseAuthTask = firebaseAuth.signInWithEmailAndPassword(SignUpActivityTest.USER_EMAIL, SignUpActivityTest.USER_PASSWORD);
        firebaseAuthTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful()){
                    DataBaseProviderTest.this.firebaseUser = firebaseAuth.getCurrentUser();
                }
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
        String surName = "Test";
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1978, 2, 23, 0, 0, 0);
        HashMap<String, Object> userProperties = UserProperties.getNewUserProperties(surName, dateOfBirth.getTime());
        dataBaseProvider.setUserProperties(userProperties);
        while (this.isAttributedExpectedUserProperties == false){
            Log.i(TAG, "The expectedUserProperties is not Attributed!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(this.expectedUserProperties, userProperties);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.isAttributedExpectedUserProperties = true;
        this.expectedUserProperties = (HashMap<String, Object>) dataSnapshot.getValue();

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        this.isAttributedExpectedUserProperties = true;
    }
}
