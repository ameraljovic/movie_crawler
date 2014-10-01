package ba.aljovic.amer.component.webservice;

import ba.aljovic.amer.batch.launcher.FailedMoviesJobLauncher;
import ba.aljovic.amer.batch.launcher.JinniJobLauncher;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class BatchController
{
    @Autowired
    private JinniJobLauncher jinniJobLauncher;

    @Autowired
    FailedMoviesJobLauncher failedMoviesJobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @RequestMapping(value = "/crawl/from/{fromId}/to/{toId}", method = RequestMethod.GET)
    public String launchJinniJob(@PathVariable Long fromId,
                                 @PathVariable Long toId) throws Exception
    {
        jinniJobLauncher.launch(fromId, toId);
        return "Crawling job started";
    }

    @RequestMapping(value = "/failedMovies/from/{fromId}/to/{toId}", method = RequestMethod.GET)
    public String launchFailedMoviesJob(@PathVariable Long fromId,
                                        @PathVariable Long toId) throws Exception
    {
        failedMoviesJobLauncher.launch(fromId, toId);
        return "Failed movies job started";
    }

    @RequestMapping(value = "/stopJob/{jobName}", method = RequestMethod.GET)
    public String stopJob(@PathVariable String jobName)
            throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException
    {
        Set<Long> executions = jobOperator.getRunningExecutions(jobName);
        for(Long executionId : executions)
        {
            jobOperator.stop(executionId);
        }
        return "jobName + \" successfully stopped.";
    }

}