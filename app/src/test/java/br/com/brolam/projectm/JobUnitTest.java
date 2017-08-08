package br.com.brolam.projectm;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.com.brolam.projectm.data.models.Job;

import static org.junit.Assert.*;

public class JobUnitTest {
    @Test
    public void getNewJob(){
        String title = "Android Software Engineer";
        String description = "What are some examples of problems a software engineer will solve? Shipping valuable features requires close coordination between devops, database, API, frontend, and mobile workstreams; Nubank engineers commonly pair and rotate focus between these responsibilities. We consistently work with new technologies, and thus value professionals who are open to learning new things, regardless of pre-existing comfort zones. Nubank mobile software engineers might solve any of the following problems: • Ship new features for the mobile apps using modern languages like Swift and Kotlin";
        Date publishedDate = Calendar.getInstance().getTime();
        HashMap<String, Object> newJob = Job.getNewJob(title, description, publishedDate);
        assertEquals(title, newJob.get(Job.TITLE));
        assertEquals(description, newJob.get(Job.DESCRIPTION));
        assertEquals(publishedDate, new Date((Long)newJob.get(Job.PUBLISHED_DATE)));
    }

    @Test
    public void isJobReference(){
        String fullPathReference = "https://projectm-e2a21.firebaseio.com/job/5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        assertEquals(true, Job.isJobReference(fullPathReference));

    }
}