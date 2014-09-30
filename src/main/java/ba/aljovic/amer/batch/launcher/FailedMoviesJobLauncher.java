package ba.aljovic.amer.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedMoviesJobLauncher extends BaseJobLauncher
{
    @Autowired
    private Job failedMoviesJob;

    public void launch(Long fromId, Long range) throws Exception
    {
        JobParameters jobParameters = createJobParameters(fromId, range);
        jobLauncher.run(failedMoviesJob, jobParameters);
    }
}
