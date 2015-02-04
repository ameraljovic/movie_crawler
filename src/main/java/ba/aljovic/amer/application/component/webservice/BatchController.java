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
    private JinniJobLauncher jinniJobLauncher;

    private FailedMoviesJobLauncher failedMoviesJobLauncher;

    private UserRatingsJobLauncher userRatingsJobLauncher;

    private JobOperator jobOperator;

    private UtilsFacade utilsFacade;

    @Autowired
    public void setJinniJobLauncher(JinniJobLauncher jinniJobLauncher)
    {
        this.jinniJobLauncher = jinniJobLauncher;
    }

    @Autowired
    public void setFailedMoviesJobLauncher(FailedMoviesJobLauncher failedMoviesJobLauncher)
    {
        this.failedMoviesJobLauncher = failedMoviesJobLauncher;
    }

    @Autowired
    public void setUserRatingsJobLauncher(UserRatingsJobLauncher userRatingsJobLauncher)
    {
        this.userRatingsJobLauncher = userRatingsJobLauncher;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator)
    {
        this.jobOperator = jobOperator;
    }

    @Autowired
    public void setUtilsFacade(UtilsFacade utilsFacade)
    {
        this.utilsFacade = utilsFacade;
    }

    @RequestMapping(value = "/crawl/from/{fromId}/range/{range}", method = RequestMethod.GET)
    public String launchJinniJob(@PathVariable Long fromId,
                                 @PathVariable Long range) throws Exception
    {
        jinniJobLauncher.launch(fromId, range);
        return "Crawling job started";
    }

    @RequestMapping(value = "/failedMovies", method = RequestMethod.GET)
    public String launchFailedMoviesJob() throws Exception
    {
        failedMoviesJobLauncher.launch();
        return "Failed movies job started";
    }

    @RequestMapping(value = "/imdbMovies/{jobId}", method = RequestMethod.GET)
    public String launchUserRatingsJob(@PathVariable Long jobId) throws Exception
    {
        userRatingsJobLauncher.launchImdbMoviesJob(jobId);
        return "User ratings job started";
    }

    @RequestMapping(value = "/imdbRatings/{jobId}", method = RequestMethod.GET)
    public String imdbRatingsJob(@PathVariable Long jobId) throws Exception
    {
        userRatingsJobLauncher.launchImdbRatingsJob(jobId);
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