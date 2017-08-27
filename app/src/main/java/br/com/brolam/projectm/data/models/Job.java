package br.com.brolam.projectm.data.models;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brenomar on 08/08/17.
 */

public class Job {
    public static final String REFERENCE_NAME = "job";
    public static final String JOB_TYPE = "jobType";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String PUBLISHED_DATE = "publishedDate";
    public static final String DESCRIPTION = "description";

    public enum JobType {
        VACANT,
        INTERNSHIP,
        CONTEST
    }


    public static boolean isJobReference(String fullPathReference) {
        return fullPathReference.contains("/" + REFERENCE_NAME);
    }

    public static boolean isJobType(JobType jobType, HashMap job){
        String jobTypeName = (String)job.get(JOB_TYPE);
        return jobType.name().equals(jobTypeName);
    }

    public static class ComparatorByPublishedDate implements Comparator<String> {
        HashMap<String, HashMap> jobs;

        public ComparatorByPublishedDate(HashMap<String, HashMap> jobs) {
            this.jobs = jobs;
        }

        public int compare(String firstJobKey,
                           String secondJobKey) {
            HashMap firstJob = this.jobs.get(firstJobKey);
            HashMap secondJob = this.jobs.get(secondJobKey);
            Long fistPublished = (Long) firstJob.get(PUBLISHED_DATE);
            Long secondPublished = (Long) secondJob.get(PUBLISHED_DATE);
            return secondPublished.compareTo(fistPublished);
        }
    }
}
