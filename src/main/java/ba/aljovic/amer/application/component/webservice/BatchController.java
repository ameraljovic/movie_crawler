package ba.aljovic.amer.application.component.webservice;

import ba.aljovic.amer.application.batch.launcher.FailedMoviesJobLauncher;
import ba.aljovic.amer.application.batch.launcher.JinniJobLauncher;
import ba.aljovic.amer.application.batch.launcher.UserRatingsJobLauncher;
import ba.aljovic.amer.application.database.UtilsFacade;
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
    private FailedMoviesJobLauncher failedMoviesJobLauncher;

    @Autowired
    private UserRatingsJobLauncher userRatingsJobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private UtilsFacade utilsFacade;

    @RequestMapping(value = "/crawl/from/{fromId}/range/{range}", method = RequestMethod.GET)
    public String launchJinniJob(@PathVariable Long fromId,
                                 @PathVariable Long range) throws Exception
    {
        jinniJobLauncher.launch(fromId, range);
        return "Crawling job started";
    }

    @RequestMapping(value = "/failedMovies/from/{fromId}/range/{range}", method = RequestMethod.GET)
    public String launchFailedMoviesJob(@PathVariable Long fromId,
                                        @PathVariable Long range) throws Exception
    {
        failedMoviesJobLauncher.launch(fromId, range);
        return "Failed movies job started";
    }

    @RequestMapping(value = "/userRatings", method = RequestMethod.GET)
    public String launchUserRatingsJob() throws Exception
    {
        userRatingsJobLauncher.launch();
        return "User ratings job started";
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
        return jobName + " successfully stopped.";
    }

    @RequestMapping(value = "/resetDatabase", method = RequestMethod.GET)
    public String resetDatabase()
    {
        utilsFacade.deleteALl();
        return "Deleted all tables";
    }

}