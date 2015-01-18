package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRatingsJobLauncher extends BaseJobLauncher
{
    @Autowired
    private Job userRatingsJob;

    public JobExecution launch() throws Exception
    {
        return jobLauncher.run(userRatingsJob, createJobParameters());
    }
}