package br.com.brolam.projectm.data.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by brenomar on 08/08/17.
 */

public class Job {
    public static final String REFERENCE_NAME = "job";
    public static final String TITLE = "title" ;
    public static final String SUMMARY = "summary" ;
    public static final String PUBLISHED_DATE = "publishedDate";
    public static final String DESCRIPTION = "description" ;


    public static boolean isJobReference(String fullPathReference) {
        return fullPathReference.contains("/" + REFERENCE_NAME);
    }
}
