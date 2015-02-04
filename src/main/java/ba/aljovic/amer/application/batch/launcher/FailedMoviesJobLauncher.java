package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedMoviesJobLauncher extends BaseJobLauncher
{
    private Job failedMoviesJob;

    @Autowired
    public FailedMoviesJobLauncher(JobLauncher jobLauncher, Job failedMoviesJob)
    {
        super(jobLauncher);
        this.failedMoviesJob = failedMoviesJob;
    }

    public void launch() throws Exception
    {
        JobParameters jobParameters = createJobParameters();
        jobLauncher.run(failedMoviesJob, jobParameters);
    }
}
