package br.com.brolam.projectm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class JobActivity extends AppCompatActivity implements ValueEventListener {
    private static final String JOB_KEY = "jobKey";

    private Toolbar toolbar;
    private TextView description;
    private DataBaseProvider dataBaseProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        this.description = (TextView) this.findViewById(R.id.description);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( firebaseUser != null ){
            this.dataBaseProvider = new DataBaseProvider(firebaseUser);
            this.dataBaseProvider.addOneJobListener(this, getJobKey());
        } else {
            this.finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Breno");
        getSupportActionBar().setSubtitle("Marques");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( this.dataBaseProvider != null){
            this.dataBaseProvider.removeOneJobListener(this, getJobKey());
        }
    }

    public static void show(Activity activity, String jobKey, int requestCode){
        Intent intent = new Intent(activity, JobActivity.class);
        intent.putExtra(JOB_KEY, jobKey);
        activity.startActivityForResult(intent, requestCode);
    }

    private String getJobKey(){
       return this.getIntent().hasExtra(JOB_KEY)? this.getIntent().getStringExtra(JOB_KEY) : "";
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final HashMap job = (HashMap) dataSnapshot.getValue();
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle((String)job.get(Job.TITLE));
            actionBar.setSubtitle(JobHolder.getTextPublished((Long)job.get(Job.PUBLISHED_DATE)));
        }
        this.description.setText((String)job.get(Job.DESCRIPTION));
        toolbar.post(new Runnable()
        {
            @Override
            public void run()
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle((String)job.get(Job.TITLE));
                getSupportActionBar().setSubtitle(JobHolder.getTextPublished((Long)job.get(Job.PUBLISHED_DATE)));
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
