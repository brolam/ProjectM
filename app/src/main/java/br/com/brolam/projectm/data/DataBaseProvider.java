package br.com.brolam.projectm.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.brolam.projectm.MainActivity;
import br.com.brolam.projectm.data.models.UserAccount;
import br.com.brolam.projectm.data.models.UserProperties;

/**
 * Created by brenomar on 02/08/17.
 */

public class DataBaseProvider {
    private FirebaseUser firebaseUser;
    private static FirebaseDatabase firebaseDatabase;
    private DatabaseReference referenceUserProperties;
    private DatabaseReference referenceUserAccount;

    public DataBaseProvider(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        if ( firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            firebaseDatabase.setLogLevel(Logger.Level.DEBUG);
        }
        if ( firebaseUser == null )
            throw new RuntimeException("Firebase User is not sign Up!");
        this.referenceUserProperties = firebaseDatabase.getReference(UserProperties.PATH_USER_PROPERTIES).child(this.firebaseUser.getUid());
        this.referenceUserAccount = firebaseDatabase.getReference(UserAccount.REFERENCE_NAME).child(this.firebaseUser.getUid());
    }

    public void setUserProperties(HashMap<String, Object> userProperties) {
        this.referenceUserProperties.updateChildren(userProperties);
    }

    public void setUserAccount(HashMap<String, Object> userAccount) {
        this.referenceUserAccount.updateChildren(userAccount);
    }

    public void addUserPropertiesListener(ValueEventListener valueEventListener) {
        this.referenceUserProperties.addValueEventListener(valueEventListener);
    }

    public void removeUserPropertiesListener(ValueEventListener valueEventListener) {
        this.referenceUserProperties.removeEventListener(valueEventListener);
    }


    public void addUserAccountListener(ValueEventListener valueEventListener) {
        this.referenceUserAccount.addValueEventListener(valueEventListener);
    }

    public void removeUserAccountListener(ValueEventListener valueEventListener) {
        this.referenceUserAccount.removeEventListener(valueEventListener);
    }

    public void queryUserAccount() {
        this.referenceUserAccount.orderByKey();
    }
}
