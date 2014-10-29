package ba.aljovic.amer.batch;

import ba.aljovic.amer.Application;
import ba.aljovic.amer.batch.launcher.JinniJobLauncher;
import ba.aljovic.amer.database.UtilsFacade;
import ba.aljovic.amer.database.entity.Genome;
import ba.aljovic.amer.database.entity.Movie;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest({"spring.profiles.active=test"})
public class JinniJobTest
{
    @Autowired
    private JinniJobLauncher jinniJobLauncher;

    @Autowired
    private UtilsFacade utilsFacade;

    @Test
    public void testJinniJobSuccess() throws Exception
    {
        int[] expectedNumberOfGenomes = { 9, 0, 9, 7};

        JobExecution jobExecution = jinniJobLauncher.launch(2L, 10L);

        assertEquals("Status of job execution is completed.",
                jobExecution.getStatus(),
                BatchStatus.COMPLETED);
        assertEquals("Number of crawled movies is 4.", 4, utilsFacade.getNumberOfMovies());

        List<Movie> movies = utilsFacade.findAll();
        int i = 0;
        for (Movie movie : movies)
        {
            Collection<Genome> genomes = movie.getGenomes();
            assertEquals("Number of genomes for movie " + movie.getTitle() + " is " + expectedNumberOfGenomes,
                         expectedNumberOfGenomes[i],
                         genomes.size());
            for (Genome genome : genomes)
            {
                assertTrue("Number of genome values is greater than zero.", genome.getGenomes().size() > 0);
            }
            i++;
        }
    }

    @After
    public void cleanUp()
    {
        utilsFacade.deleteALl();
    }
}
