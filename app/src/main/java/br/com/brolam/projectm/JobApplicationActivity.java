package br.com.brolam.projectm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.JobApplication;

public class JobApplicationActivity extends AppCompatActivity implements View.OnClickListener, OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> {
    private static final int READ_REQUEST_CODE = 42;
    private static final String USER_PROPERTIES = "userProperties";
    private static final String JOB_KEY = "job_key";
    private static final String JOB = "job";
    public static final int RESULT_OK_APPLICATION_BY_PHONE = 1;
    public static final int RESULT_OK_APPLICATION_BY_COMPUTER =2 ;
    Toolbar toolbar;
    private View progressView;
    private View contentLayout;
    private TextView summary;
    private RadioButton applicationByPhoneOption;
    private RadioButton applicationByComputerOption;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private DataBaseProvider dataBaseProvider;
    private Uri uriFileCV = null;
    private HashMap job;
    private HashMap userProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.job = (HashMap) getIntent().getSerializableExtra(JOB);
        this.userProperties = (HashMap) getIntent().getSerializableExtra(USER_PROPERTIES);
        if (( this.job == null ) || (this.userProperties == null)){
            this.finish();
        }
        setContentView(R.layout.activity_job_application);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.contentLayout = findViewById(R.id.contentLayout);
        this.progressView = findViewById(R.id.progressBar);
        this.summary = (TextView) findViewById(R.id.summary);
        this.applicationByPhoneOption = (RadioButton) findViewById(R.id.applicationByPhoneOption);
        this.applicationByComputerOption = (RadioButton) findViewById(R.id.applicationByComputerOption);
        setSupportActionBar(toolbar);
        this.applicationByPhoneOption.setOnClickListener(this);
        this.applicationByComputerOption.setOnClickListener(this);
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.dataBaseProvider = new DataBaseProvider(this.firebaseUser);
        updateScreen();
    }



    private void updateScreen() {
        this.toolbar.setTitle(this.job.get(Job.TITLE).toString());
        this.summary.setText(this.job.get(Job.SUMMARY).toString());
    }

    public static void show(Activity activity, int requestCode, HashMap userProperties, String jobKey, HashMap job ){
        Intent intent = new Intent(activity, JobApplicationActivity.class);
        intent.putExtra(USER_PROPERTIES, userProperties);
        intent.putExtra(JOB_KEY, jobKey);
        intent.putExtra(JOB, job);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        if ( this.applicationByPhoneOption.equals(view)){
            performFileSearch();
        } else if ( this.applicationByComputerOption.equals(view)){
            saveJobApplicationLink();
        }
    }

    //https://developer.android.com/guide/topics/providers/document-provider.html
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("application/pdf");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                this.uriFileCV = resultData.getData();
                Log.i(JobApplicationActivity.class.getName(), "Uri: " + uriFileCV.toString());
                sendFileCV();
            }
        }
    }

    private void sendFileCV(){
        assert  this.uriFileCV != null;
        String userKey = this.firebaseUser.getUid();
        String jobKey = getIntent().getStringExtra(JOB_KEY);
        String jobUrlFileCvPdf = JobApplication.getUrlFileCvPdf(userKey, jobKey);
        StorageReference reference = firebaseStorage.getReference(jobUrlFileCvPdf);
        if ( reference != null){
            List<UploadTask> activeUploadTasks = reference.getActiveUploadTasks();
            UploadTask uploadTask = null;
            if (activeUploadTasks.size() > 0) {
                uploadTask = activeUploadTasks.get(0);
            } else {
                uploadTask = reference.putFile(this.uriFileCV);
            }
            uploadTask.addOnFailureListener(this);
            uploadTask.addOnSuccessListener(this);
            showProgress(true);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.i(JobApplicationActivity.class.getName(), e.getMessage());
        showProgress(false);
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Log.i(JobApplicationActivity.class.getName(), taskSnapshot.toString());
        this.saveJobApplication();
    }

    private void saveJobApplication() {
        String jobKey = getIntent().getStringExtra(JOB_KEY);
        HashMap newJobApplication = JobApplication.getNewApplication(jobKey, new Date().getTime());
        this.dataBaseProvider.setJobApplication(newJobApplication);
        showProgress(false);
        this.setResult(RESULT_OK_APPLICATION_BY_PHONE);
        this.finish();
    }

    private void saveJobApplicationLink() {
        String jobKey = getIntent().getStringExtra(JOB_KEY);
        this.dataBaseProvider.setJobApplicationLink(this.userProperties, jobKey, this.job );
        showProgress(false);
        this.setResult(RESULT_OK_APPLICATION_BY_COMPUTER);
        this.finish();
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

            contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            contentLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
