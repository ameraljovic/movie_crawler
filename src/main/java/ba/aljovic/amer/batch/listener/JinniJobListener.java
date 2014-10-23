package ba.aljovic.amer.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class JinniJobListener implements JobExecutionListener
{
    private Logger logger = Logger.getLogger(getClass().toString());

    @Override
    public void beforeJob(JobExecution jobExecution) {}

    @Override
    public void afterJob(JobExecution jobExecution)
    {
        ExitStatus exitStatus = jobExecution.getExitStatus();
        if (exitStatus.equals(ExitStatus.FAILED) ||
                exitStatus.equals(ExitStatus.STOPPED) || exitStatus.equals(ExitStatus.UNKNOWN))
        {
            logger.severe("Job execution failed");
            for (StepExecution stepExecution : jobExecution.getStepExecutions())
            {
                logger.severe(stepExecution.getExitStatus().getExitDescription());
                System.out.println(jobExecution.getExitStatus());
            }
        }
        else if (exitStatus.equals(ExitStatus.COMPLETED))
            logger.info("Job execution finished with exit status " + jobExecution.getExitStatus().toString());
        else
            logger.severe("Job execution finished with exit status " + jobExecution.getExitStatus().toString());
    }
}
