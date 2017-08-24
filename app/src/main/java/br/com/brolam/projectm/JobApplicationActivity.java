package br.com.brolam.projectm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import br.com.brolam.projectm.data.models.JobApplication;

public class JobApplicationActivity extends AppCompatActivity implements View.OnClickListener, OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> {
    private static final int READ_REQUEST_CODE = 42;
    private static final String JOB_KEY = "job_key";
    private static final String TITLE = "title";
    private static final String SUMMARY = "summary";
    Toolbar toolbar;
    private TextView summary;
    private RadioButton applicationByPhoneOption;
    private RadioButton applicationByComputerOption;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private Uri uriFileCV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.summary = (TextView) findViewById(R.id.summary);
        this.applicationByPhoneOption = (RadioButton) findViewById(R.id.applicationByPhoneOption);
        this.applicationByComputerOption = (RadioButton) findViewById(R.id.applicationByComputerOption);
        setSupportActionBar(toolbar);
        this.applicationByPhoneOption.setOnClickListener(this);
        this.applicationByComputerOption.setOnClickListener(this);
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.firebaseStorage = FirebaseStorage.getInstance();
        updateScreen();
    }

    private void updateScreen() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        String summary = intent.getStringExtra(SUMMARY);
        this.toolbar.setTitle(title);
        this.summary.setText(summary);
    }

    public static void show(Activity activity, int requestCode, String jobKey, String title, String summary ){
        Intent intent = new Intent(activity, JobApplicationActivity.class);
        intent.putExtra(JOB_KEY, jobKey);
        intent.putExtra(TITLE, title);
        intent.putExtra(SUMMARY, summary);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        if ( this.applicationByPhoneOption.equals(view)){
            performFileSearch();
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
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.i(JobApplicationActivity.class.getName(), e.getMessage());
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Log.i(JobApplicationActivity.class.getName(), taskSnapshot.toString());
        this.setResult(RESULT_OK);
        this.finish();
    }
}
