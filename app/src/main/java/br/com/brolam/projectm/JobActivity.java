package br.com.brolam.projectm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import java.util.HashMap;
import br.com.brolam.projectm.adapters.holders.JobHolder;
import br.com.brolam.projectm.data.models.Job;

public class JobActivity extends AppCompatActivity {
    private static final String JOB_KEY = "jobKey";
    private static final String JOB = "job";

    private Toolbar toolbar;
    private TextView summary;
    private TextView publishedDate;
    private TextView description;
    private HashMap job;

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
        this.description = (TextView) this.findViewById(R.id.description);
        this.job = (HashMap) getIntent().getSerializableExtra(JOB);
        if ( this.job == null ){
            this.finish();
        }
        update();
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

}
