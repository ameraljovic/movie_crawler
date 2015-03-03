package ba.aljovic.amer.application.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileExportJobLauncher extends BaseJobLauncher
{
    private Job fileExportJob;

    @Autowired
    public FileExportJobLauncher(JobLauncher jobLauncher, Job fileExportJob)
    {
        super(jobLauncher);
        this.fileExportJob = fileExportJob;
    }

    public JobExecution launch() throws Exception
    {
        JobParameters jobParameters = createJobParameters();
        return jobLauncher.run(fileExportJob, jobParameters);
    }
}
