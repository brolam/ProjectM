package br.com.brolam.projectm.data.models;

import java.util.HashMap;

/**
 * Created by brenomar on 08/08/17.
 */

public class JobApplicationLink {
    public static final String REFERENCE_NAME = "jobApplicationsLink";
    public static final String USER_KEY = "userKey";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String JOB_KEY = "jobKey";
    public static final String JOB_TITLE = "jobTitle";
    public static final String JOB_SUMMARY = "jobSummary";
    public static final String CREATED_DATE = "createdDate";

    public static boolean isReference(String fullPathReference) {
        return fullPathReference.contains("/" + REFERENCE_NAME);
    }

    public static HashMap<String, Object>  getNewJobApplicationLink(String userKey, String userName, String userEmail, String jobKey, String jobTitle, String jobSummary,  Long createdDate) {
        HashMap<String, Object> newJobApplicationLink = new HashMap<>();
        newJobApplicationLink.put(USER_KEY, userKey);
        newJobApplicationLink.put(USER_NAME, userName);
        newJobApplicationLink.put(USER_EMAIL, userEmail);
        newJobApplicationLink.put(JOB_KEY, jobKey);
        newJobApplicationLink.put(JOB_TITLE, jobTitle);
        newJobApplicationLink.put(JOB_SUMMARY, jobSummary);
        newJobApplicationLink.put(CREATED_DATE, createdDate);
        return newJobApplicationLink;
    }
}
