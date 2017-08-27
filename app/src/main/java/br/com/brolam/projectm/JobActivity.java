package br.com.brolam.projectm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import br.com.brolam.projectm.adapters.holders.JobHolder;
import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.JobApplication;

public class JobActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    private static final String JOB_KEY = "jobKey";
    private static final String JOB = "job";
    private static final int REQUEST_JOB_TO_APPLY = 100;

    private Toolbar toolbar;
    private TextView summary;
    private TextView publishedDate;
    private View linearLayoutApplicationDate;
    private TextView applicationDate;
    private Button buttonToApply;
    private TextView description;
    private HashMap job;
    private DataBaseProvider dataBaseProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        this.summary  = (TextView) this.findViewById(R.id.summary);
        this.publishedDate  = (TextView) this.findViewById(R.id.publishedDate);
        this.applicationDate  = (TextView) this.findViewById(R.id.applicationDate);
        this.linearLayoutApplicationDate = this.findViewById(R.id.linearLayoutApplicationDate);
        this.description = (TextView) this.findViewById(R.id.description);
        this.buttonToApply = (Button) this.findViewById(R.id.buttonToApply);
        this.job = (HashMap) getIntent().getSerializableExtra(JOB);
        if ( this.job == null ){
            this.finish();
        }
        buttonToApply.setOnClickListener(this);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.dataBaseProvider = new DataBaseProvider(firebaseUser);
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.dataBaseProvider.addJobApplicationListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.dataBaseProvider.removeJobApplicationListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job, menu);
        return true;
    }

    public static void show(Activity activity,String jobKey, HashMap job, int requestCode){
        Intent intent = new Intent(activity, JobActivity.class);
        intent.putExtra(JOB_KEY, jobKey);
        intent.putExtra(JOB, job);
        activity.startActivityForResult(intent, requestCode);
    }

    public void update() {
        this.setTitle((String)job.get(Job.TITLE));
        this.publishedDate.setText(JobHolder.getTextPublished((Long) job.get(Job.PUBLISHED_DATE)));
        this.summary.setText((String)job.get(Job.SUMMARY));
        this.description.setText((String) job.get(Job.DESCRIPTION));
    }

    @Override
    public void onClick(View view) {
        if ( this.buttonToApply.equals(view)){
            String jobKey = this.getIntent().getStringExtra(JOB_KEY);
            String title = (String)job.get(Job.TITLE);
            String summary =(String)job.get(Job.SUMMARY);
            JobApplicationActivity.show(this, REQUEST_JOB_TO_APPLY, jobKey, title, summary);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == REQUEST_JOB_TO_APPLY){
            if ( resultCode == RESULT_OK){
                notifyJobApplicationSuccessfully();
            }
        }
    }

    private void notifyJobApplicationSuccessfully() {
        Snackbar.make(this.description, R.string.job_application_notify_sucessful, Snackbar.LENGTH_LONG)
                .setAction(R.string.app_name, null).show();

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String jobKey = getIntent().getStringExtra(JOB_KEY);
        HashMap<String, Object>  jobApplications = (HashMap<String, Object>) dataSnapshot.getValue();
        if (jobApplications == null) return;
        if ( jobApplications.containsKey(jobKey) ){
            showJobApplication((HashMap<String, Object>)jobApplications.get(jobKey));
        }
    }

    private void showJobApplication(HashMap<String, Object> jobApplication) {
        String strApplicationDate = JobHolder.getTextPublished((Long) jobApplication.get(JobApplication.APPLICATION_DATE));
        this.linearLayoutApplicationDate.setVisibility(View.VISIBLE);
        this.applicationDate.setText(strApplicationDate);
        this.buttonToApply.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
