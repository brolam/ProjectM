package br.com.brolam.projectm.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Date;
import br.com.brolam.projectm.R;
import br.com.brolam.projectm.data.models.Job;

/**
 * Created by brenomar on 10/08/17.
 */

public class JobHolder extends RecyclerView.ViewHolder   {

    public interface JobHolderClickable{
        void onJobClick(String jobKey, HashMap job);
    }

    private TextView title;
    private TextView summary;
    private TextView publishedDate;

    public JobHolder(View itemView) {
        super(itemView);
        this.title = (TextView)itemView.findViewById(R.id.title);
        this.summary = (TextView)itemView.findViewById(R.id.summary);
        this.publishedDate = (TextView)itemView.findViewById(R.id.publishedDate);
    }

    public void bind(final String jobKey, final HashMap job, final JobHolderClickable jobHolderClickable ){
        this.title.setText((String)job.get(Job.TITLE));
        this.summary.setText((String)job.get(Job.SUMMARY));
        this.publishedDate.setText(getTextPublished((Long)job.get(Job.PUBLISHED_DATE)));
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobHolderClickable.onJobClick(jobKey, job);
            }
        });
    }

    public static String getTextPublished(long publishedDate){
        return DateUtils.getRelativeTimeSpanString(
                publishedDate,
                new Date().getTime(),
                DateUtils.SECOND_IN_MILLIS).toString();

    }
}
