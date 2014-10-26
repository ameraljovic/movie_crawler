package ba.aljovic.amer.component.service;

import ba.aljovic.amer.Application;
import ba.aljovic.amer.database.entity.Genome;
import ba.aljovic.amer.database.entity.Movie;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class JinniParserTest extends TestCase
{
    @Autowired
    private HttpRetriever httpRetriever;

    @Autowired
    private JinniParser jinniParser;

    @Test
    public void testParseMovie() throws Exception
    {
        // Expected values
        int expectedNumberOfGenomes = 12;
        List<Genome> genomes = new ArrayList<>();
        int[] expectedNumberOfGenomeValues = { 5, 11, 2, 2, 2, 2, 3, 1, 1, 2, 1, 1 };

        // Get html input
        String urlTitle = "kind-hearts-and-coronets";
        String html = httpRetriever.retrieveDocument("http://www.jinni.com/movies/" + urlTitle);

        // Execute test
        Movie movie = new Movie();
        movie = jinniParser.parseMovie(movie, html);

        // Assert test
        assertEquals("Number of genomes is " + expectedNumberOfGenomes,
                expectedNumberOfGenomes,
                movie.getGenomes().size());
        int i = 0;
        for (Genome genome : movie.getGenomes())
        {
            assertEquals("Number of genome values is " + expectedNumberOfGenomeValues[i],
                    expectedNumberOfGenomeValues[i],
                    genome.getGenomes().size());
            i++;
        }
    }
}