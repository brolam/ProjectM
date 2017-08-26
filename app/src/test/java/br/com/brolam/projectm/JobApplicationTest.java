package br.com.brolam.projectm;


import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.JobApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobApplicationTest {

    @Test
    public void isReference(){
        String fullPathReference = "https://projectm-e2a21.firebaseio.com/jobApplications/5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        assertEquals(true, JobApplication.isReference(fullPathReference));
    }

    @Test
    public void getUrlFileCvPdf(){
        String userKey = "5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        String jobKey = "5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        String urlFileCv = String.format("cvs/%s/%s/curriculum.pdf", userKey, jobKey);
        assertEquals(urlFileCv, JobApplication.getUrlFileCvPdf(userKey, jobKey));
    }

    @Test
    public void getNewApplication(){
        String jobKey = "5PQBNu6346Tz3eqsOzSUZOuDEiv3";
        Long applicationDate = new Date().getTime();
        HashMap newJobApplication = JobApplication.getNewApplication(jobKey, applicationDate);
        String applicationDateExpectedPath = String.format("%s/%s", jobKey, JobApplication.APPLICATION_DATE);
        assertTrue(newJobApplication.containsKey(applicationDateExpectedPath));
        assertEquals(applicationDate, newJobApplication.get(applicationDateExpectedPath));

    }
}