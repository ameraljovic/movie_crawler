package ba.aljovic.amer;

import ba.aljovic.amer.component.service.MovieRetriever;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith (SpringJUnit4ClassRunner.class)
@ComponentScan (basePackages = "ba.aljovic.amer.component")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = Application.class)
public class UrlCreationTest
{
    @Autowired
    private MovieRetriever movieRetriever;

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
