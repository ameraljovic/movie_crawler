package ba.aljovic.amer.batch.launcher;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseJobLauncher
{
    @Autowired
    protected JobLauncher jobLauncher;

    protected static JobParameters createJobParameters(Long fromId, Long range)
    {
        JobParameters jobParameters;
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addLong("FROM_ID", fromId);
        builder.addLong("RANGE", range);
        jobParameters = builder.toJobParameters();
        return jobParameters;
    }
}
