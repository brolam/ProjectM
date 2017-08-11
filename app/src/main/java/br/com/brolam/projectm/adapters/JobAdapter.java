package br.com.brolam.projectm.adapters;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;

import br.com.brolam.projectm.R;
import br.com.brolam.projectm.adapters.holders.JobHolder;
import br.com.brolam.projectm.adapters.holders.JobHolder.JobHolderClickable;

/**
 * Created by brenomar on 10/08/17.
 */

public class JobAdapter  extends RecyclerView.Adapter<JobHolder> implements ChildEventListener {
    private Context context;
    private ArrayMap<String,HashMap> jobsMapSortedMap;
    private JobHolderClickable jobHolderClickable;

    public JobAdapter(Context context, JobHolderClickable jobHolderClickable ){
        this.context = context;
        this.jobsMapSortedMap = new ArrayMap<>();
        this.jobHolderClickable = jobHolderClickable;
    }

    @Override
    public JobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_job, parent, false);
        return new JobHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobHolder holder, int position) {
        String jobKey = jobsMapSortedMap.keyAt(position);
        HashMap job = jobsMapSortedMap.get(jobKey);
        holder.bind(jobKey, job, this.jobHolderClickable );
    }

    @Override
    public int getItemCount() {
        return jobsMapSortedMap.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        jobsMapSortedMap.put(dataSnapshot.getKey() , (HashMap) dataSnapshot.getValue());
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        jobsMapSortedMap.put(dataSnapshot.getKey() , (HashMap) dataSnapshot.getValue());
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        jobsMapSortedMap.remove(dataSnapshot.getKey());
        this.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        jobsMapSortedMap.put(dataSnapshot.getKey() , (HashMap) dataSnapshot.getValue());
        this.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
