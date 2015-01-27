package ba.aljovic.amer.application.batch;

import ba.aljovic.amer.Application;
import ba.aljovic.amer.application.batch.launcher.UserRatingsJobLauncher;
import ba.aljovic.amer.application.database.UtilsFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@IntegrationTest ({"spring.profiles.active=test"})
public class UserRatingsJobTest
{
    @Autowired
    UserRatingsJobLauncher launcher;

    @Autowired
    private UtilsFacade utilsFacade;

    @Test
    public void test() throws Exception
    {
        launcher.launchImdbMoviesJob();
        assertEquals("Number of movies should be 250",
                250, utilsFacade.findAllImdbMovies().size());
    }
}
