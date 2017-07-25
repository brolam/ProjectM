package br.com.brolam.projectm.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by brenomar on 25/07/17.
 */

public class AuthHelper {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public AuthHelper(){
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
    }

    public boolean isLoginOn(){
        return this.currentUser != null;
    }
}
