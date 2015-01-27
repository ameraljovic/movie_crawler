package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRatingsJobLauncher extends BaseJobLauncher
{
    @Autowired
    private Job imdbMoviesJob;

    @Autowired
    private Job imdbRatingsJob;

    public JobExecution launchImdbMoviesJob(Long jobId) throws Exception
    {
        return jobLauncher.run(imdbMoviesJob, createJobParameters(jobId));
    }

    public JobExecution launchImdbMoviesJob() throws Exception
    {
        return jobLauncher.run(imdbMoviesJob, createJobParameters());
    }

    public JobExecution launchImdbRatingsJob(Long jobId) throws Exception
    {
        return jobLauncher.run(imdbRatingsJob, createJobParameters(jobId));
    }

    public JobExecution launchImdbRatingsJob() throws Exception
    {
        return jobLauncher.run(imdbRatingsJob, createJobParameters());
    }
}