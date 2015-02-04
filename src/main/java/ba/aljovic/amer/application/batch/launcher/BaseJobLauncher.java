package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class BaseJobLauncher
{
    protected JobLauncher jobLauncher;

    @Autowired
    public BaseJobLauncher(JobLauncher jobLauncher)
    {
        this.jobLauncher = jobLauncher;
    }

    protected static JobParameters createJobParameters(Long fromId, Long range)
    {
        JobParameters jobParameters;
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addLong("FROM_ID", fromId);
        builder.addLong("RANGE", range);
        jobParameters = builder.toJobParameters();
        return jobParameters;
    }

    protected static JobParameters createJobParameters()
    {
        JobParameters jobParameters;
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addLong("RANDOM", new Date().getTime());
        jobParameters = builder.toJobParameters();
        return jobParameters;
    }

    protected static JobParameters createJobParameters(Long jobId)
    {
        JobParameters jobParameters;
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addLong("JOB_ID", jobId);
        jobParameters = builder.toJobParameters();
        return jobParameters;
    }
}
