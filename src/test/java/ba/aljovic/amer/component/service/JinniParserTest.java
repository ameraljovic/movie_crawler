package ba.aljovic.amer.component.service;

import ba.aljovic.amer.database.entity.Genome;
import ba.aljovic.amer.database.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JinniParserTest
{
    private HttpRetriever httpRetriever = new HttpRetriever();
    private JinniParser jinniParser = new JinniParser();

    @Before
    public void prepare()
    {
        httpRetriever.init();
    }

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

    @Test
    public void testParseMovieFail() throws IOException
    {
        String urlTitle = "star-wars-iv";
        String html = httpRetriever.retrieveDocument("http://www.jinni.com/movies/" + urlTitle);
        assertNull(html);
    }
}