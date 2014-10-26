package ba.aljovic.amer.component.service;

import ba.aljovic.amer.exception.TmdbMovieNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MovieRetrieverTest
{
    private MovieRetriever movieRetriever;

    @Before
    public void prepare()
    {
        movieRetriever = new MovieRetriever();

        HttpRetriever httpRetriever = new HttpRetriever();
        httpRetriever.init();
        movieRetriever.setHttpRetriever(httpRetriever);

        movieRetriever.setJinniParser(new JinniParser());

        movieRetriever.setJsonParser(new JSONParser());
    }

    @Test
    public void testRetrieveTitle() throws IOException, TmdbMovieNotFoundException
    {
        String expectedTitle = "Chungking Express";
        String title = movieRetriever.retrieveTitle(11104L);
        assertEquals("Title is not '" + expectedTitle + "'", expectedTitle, title);
    }

    @Test(expected = TmdbMovieNotFoundException.class)
    public void testRetrieveTitleFail() throws IOException, TmdbMovieNotFoundException
    {
        movieRetriever.retrieveTitle(1L);
    }

    @Test
    public void testRetrieveImdbId() throws IOException, TmdbMovieNotFoundException
    {
        String expectedImdbId = "tt0109424";
        String imdbId = movieRetriever.retrieveImdbId(11104L);
        assertEquals("Imdb id is not " + expectedImdbId, expectedImdbId, imdbId);
    }

    @Test(expected = TmdbMovieNotFoundException.class)
    public void testRetrieveImdbIdFail() throws IOException, TmdbMovieNotFoundException
    {
        movieRetriever.retrieveImdbId(10L);
    }

    @Test
    public void normalTitle()
    {
        String title = "Before Sunrise";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("before-sunrise"));
    }

    @Test
    public void oneWord()
    {
        String title = "Life";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("life"));
    }

    @Test
    public void number()
    {
        String title = "2046";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("2046"));
    }

    @Test
    public void minus()
    {
        String title = "Kick-Ass";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("kick-ass"));
    }

    @Test
    public void colon()
    {
        String title = "Sophie Scholl: The Final Days";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("sophie-scholl-the-final-days"));
    }

    @Test
    public void apostrophe()
    {
        String title = "Felicia's Journey";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("felicias-journey"));
    }

    @Test
    public void parentheses()
    {
        String title = "(500) Days of Summer";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("500-days-of-summer"));
    }

    @Test
    public void andCharacter()
    {
        String title = "Willy Wonka & the Chocolate Factory";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("willy-wonka-and-the-chocolate-factory"));
    }

    @Test
    public void punctuation()
    {
        String title = "Good Bye, Lenin!";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("good-bye-lenin"));
    }

    @Test
    public void punctuation2()
    {
        String title = "The Lord of the Rings: The Two Towers";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("the-lord-of-the-rings-the-two-towers"));
    }

    @Test
    public void latinCharacters()
    {
        String title = "Caché";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("cache"));
    }

    @Test
    public void latinPlusNumbers()
    {
        String title = "Cléo from 5 to 7";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("cleo-from-5-to-7"));
    }

    @Test
    public void apostrophePlusPunctuation()
    {
        String title = "Shaft's Big Score!";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("shafts-big-score"));
    }

    @Test
    public void slash()
    {
        String title = "Bollywood/Hollywood";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("bollywood-hollywood"));
    }

    @Test
    public void oneCharacterOnly()
    {
        String title = "o";
        String urlTitle = movieRetriever.toUrl(title);
        assertThat("Title", urlTitle, is("o"));
    }
}