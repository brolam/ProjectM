package br.com.brolam.projectm.data.models;

import java.util.HashMap;

/**
 * Created by brenomar on 08/08/17.
 */

public class JobApplication {
    public static final String REFERENCE_NAME = "jobApplications";
    public static final String USER_KEY = "userKey";
    public static final String JOB_KEY = "jobKey";
    public static final String APPLICATION_DATE = "applicationDate";


    public static boolean isReference(String fullPathReference) {
        return fullPathReference.contains("/" + REFERENCE_NAME);
    }

    public static String getUrlFileCvPdf(String userKey, String jobKey) {
        return String.format("cvs/%s/%s/curriculum.pdf", userKey, jobKey);
    }

    public static HashMap<String, Object>  getNewApplication(String jobKey, Long applicationDate) {
        HashMap<String, Object> newApplication = new HashMap<>();
        newApplication.put(jobKey + "/" + APPLICATION_DATE, applicationDate);
        return newApplication;
    }
}
