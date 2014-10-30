package ba.aljovic.amer.application.component.service;

import ba.aljovic.amer.application.database.entity.Genome;
import ba.aljovic.amer.application.database.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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
    public void testRetrieveDocument() throws IOException
    {
        String urlTitle = "the-empire-strikes-back";
        String html = httpRetriever.retrieveDocument("http://www.jinni.com/movies/" + urlTitle);
        assertNotNull(html);
    }


    @Test(expected = IllegalStateException.class)
    public void testRetrieveDocumentNoHttp() throws IOException
    {
        String urlTitle = "star-wars-iv";
        httpRetriever.retrieveDocument("www.jinni.com/movies/" + urlTitle);
    }

    @Test
    public void testRetrieveDocumentFail() throws IOException
    {
        String urlTitle = "star-wars-iv";
        String html = httpRetriever.retrieveDocument("http://www.jinni.com/movies/" + urlTitle);
        assertNull(html);
    }
}