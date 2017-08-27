package br.com.brolam.projectm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import br.com.brolam.projectm.R;
import br.com.brolam.projectm.adapters.holders.JobHolder;
import br.com.brolam.projectm.adapters.holders.JobHolder.JobHolderClickable;
import br.com.brolam.projectm.data.models.Job;

/**
 * Created by brenomar on 10/08/17.
 */

public class JobAdapter  extends RecyclerView.Adapter<JobHolder> implements ChildEventListener {
    private Context context;
    private HashMap<String,HashMap> jobs;
    private ArrayList<String> vacantKeys;
    private ArrayList<String> internshipKeys;
    private ArrayList<String> contestKeys;
    private JobHolderClickable jobHolderClickable;
    private Job.JobType selectedJobType = Job.JobType.VACANT;

    public JobAdapter(Context context, JobHolderClickable jobHolderClickable ){
        this.context = context;
        this.jobs = new HashMap<>();
        this.vacantKeys = new ArrayList<>();
        this.internshipKeys = new ArrayList<>();
        this.contestKeys = new ArrayList<>();
        this.jobHolderClickable = jobHolderClickable;
    }

    public void setSelectedJobType(Job.JobType selectedJobType) {
        this.selectedJobType = selectedJobType;
        Collections.sort(this.getJobsSelected(),new Job.ComparatorByPublishedDate(this.jobs));
        this.notifyDataSetChanged();
    }

    private ArrayList<String> getJobsSelected(){
        if (this.selectedJobType == Job.JobType.CONTEST) return contestKeys;
        if (this.selectedJobType == Job.JobType.INTERNSHIP) return internshipKeys;
        return vacantKeys;
    }

    @Override
    public JobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_job, parent, false);
        return new JobHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobHolder holder, int position) {
        String jobKey = this.getJobsSelected().get(position);
        HashMap job = jobs.get(jobKey);
        holder.bind(jobKey, job, this.jobHolderClickable );
    }

    @Override
    public int getItemCount() {
        return this.getJobsSelected().size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String jobKey = dataSnapshot.getKey();
        HashMap job = (HashMap) dataSnapshot.getValue();
        if (jobs.containsKey(jobKey) == false ){
            if (Job.isJobType(Job.JobType.VACANT, job)) this.vacantKeys.add(jobKey);
            if (Job.isJobType(Job.JobType.INTERNSHIP, job)) this.internshipKeys.add(jobKey);
            if (Job.isJobType(Job.JobType.CONTEST, job)) this.contestKeys.add(jobKey);
        }
        this.jobs.put(dataSnapshot.getKey(), (HashMap) dataSnapshot.getValue());
        Collections.sort(this.getJobsSelected(),new Job.ComparatorByPublishedDate(this.jobs));
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        jobs.put(dataSnapshot.getKey() , (HashMap) dataSnapshot.getValue());
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String jobKey = dataSnapshot.getKey();
        jobs.remove(jobKey);
        this.vacantKeys.remove(jobKey);
        this.internshipKeys.remove(jobKey);
        this.contestKeys.remove(jobKey);
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        jobs.put(dataSnapshot.getKey() , (HashMap) dataSnapshot.getValue());
        this.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
