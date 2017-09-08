package br.com.brolam.projectm.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.JobApplication;
import br.com.brolam.projectm.data.models.JobApplicationLink;
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
    private DatabaseReference referenceJobApplication;
    private Query queryJobs;

    public DataBaseProvider(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        if ( firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            firebaseDatabase.setLogLevel(Logger.Level.DEBUG);
        }
        if ( firebaseUser == null )
            throw new RuntimeException("Firebase User is not sign Up!");
        this.referenceUserProperties = firebaseDatabase.getReference(UserProperties.REFERENCE_NAME).child(this.firebaseUser.getUid());
        this.referenceUserAccount = firebaseDatabase.getReference(UserAccount.REFERENCE_NAME).child(this.firebaseUser.getUid());
        this.referenceJobApplication = firebaseDatabase.getReference(JobApplication.REFERENCE_NAME).child(this.firebaseUser.getUid());
        this.queryJobs = firebaseDatabase.getReference(Job.REFERENCE_NAME).orderByPriority();
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

    public void addQueryJobsListener(ChildEventListener childEventListener) {
        this.queryJobs.addChildEventListener(childEventListener);
    }

    public void removeQueryJobsListener(ChildEventListener childEventListener) {
        this.queryJobs.addChildEventListener(childEventListener);
    }

    public void addOneJobListener(ValueEventListener valueEventListener, String jobKey){
        this.queryJobs.getRef().child(jobKey).addValueEventListener(valueEventListener);
    }

    public void removeOneJobListener(ValueEventListener valueEventListener, String jobKey){
        this.queryJobs.getRef().child(jobKey).removeEventListener(valueEventListener);
    }

    public void setJobApplication(HashMap jobApplication) {
        this.referenceJobApplication.updateChildren(jobApplication);
    }

    public void addJobApplicationListener(ValueEventListener valueEventListener) {
        this.referenceJobApplication.addValueEventListener(valueEventListener);
    }

    public void removeJobApplicationListener(ValueEventListener valueEventListener) {
        this.referenceJobApplication.removeEventListener(valueEventListener);
    }

    public void setJobApplicationLink(HashMap userProperties, String jobKey, HashMap job){
        DatabaseReference pushJobApplicationLink = firebaseDatabase.getReference(JobApplicationLink.REFERENCE_NAME).push();
        HashMap jobApplicationLink = JobApplicationLink.getNewJobApplicationLink(
                this.firebaseUser.getUid(),
                UserProperties.getDisplayName(userProperties),
                this.firebaseUser.getEmail(),
                jobKey,
                (String)job.get(Job.TITLE),
                (String)job.get(Job.SUMMARY),
                new Date().getTime()
        );
        pushJobApplicationLink.updateChildren(jobApplicationLink);
    }

}
