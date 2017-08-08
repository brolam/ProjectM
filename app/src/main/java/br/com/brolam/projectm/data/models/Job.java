package br.com.brolam.projectm.data.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by brenomar on 08/08/17.
 */

public class Job {
    public static final String REFERENCE_NAME = "job";
    public static final String TITLE = "title" ;
    public static final String DESCRIPTION = "description" ;
    public static final String PUBLISHED_DATE = "publishedDate";

    public static HashMap<String,Object> getNewJob(String title, String description, Date publishedDate) {
        HashMap<String,Object> newJob = new HashMap<>();
        newJob.put(TITLE, title);
        newJob.put(DESCRIPTION, description);
        newJob.put(PUBLISHED_DATE, publishedDate.getTime());
        return newJob;
    }

    public static boolean isJobReference(String fullPathReference) {
        return fullPathReference.contains("/" + REFERENCE_NAME);
    }
}
