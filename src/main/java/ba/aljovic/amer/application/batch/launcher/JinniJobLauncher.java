package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JinniJobLauncher extends BaseJobLauncher
{
    private Job jinniJob;

    @Autowired
    public JinniJobLauncher(JobLauncher jobLauncher, Job jinniJob)
    {
        super(jobLauncher);
        this.jinniJob = jinniJob;
    }

    public JobExecution launch(Long fromId, Long range) throws Exception
    {
        JobParameters jobParameters = createJobParameters(fromId, range);
        return jobLauncher.run(jinniJob, jobParameters);
    }
}