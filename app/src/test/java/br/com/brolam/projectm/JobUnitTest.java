package br.com.brolam.projectm;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.com.brolam.projectm.data.models.Job;

import static org.junit.Assert.*;

public class JobUnitTest {

    @Test
    public void isJobReference(){
        String fullPathReference = "https://projectm-e2a21.firebaseio.com/job/5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        assertEquals(true, Job.isJobReference(fullPathReference));
    }

    @Test
    public void isJobType(){
        HashMap jobVacant = new HashMap();
        HashMap jobInternship = new HashMap();
        HashMap jobContest = new HashMap();
        jobVacant.put(Job.JOB_TYPE, "VACANT");
        jobInternship.put(Job.JOB_TYPE, "INTERNSHIP");
        jobContest.put(Job.JOB_TYPE, "CONTEST");
        assertTrue(Job.isJobType(Job.JobType.VACANT, jobVacant));
        assertTrue(Job.isJobType(Job.JobType.INTERNSHIP, jobInternship));
        assertTrue(Job.isJobType(Job.JobType.CONTEST, jobContest));
    }
}