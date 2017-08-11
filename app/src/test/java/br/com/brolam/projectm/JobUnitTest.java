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
}