package ba.aljovic.amer;

import ba.aljovic.amer.component.service.MovieRetriever;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;

@RunWith (SpringJUnit4ClassRunner.class)
@ComponentScan (basePackages = "ba.aljovic.amer.component")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = Application.class)
public class FailedMoviesReaderTest
{
    @Autowired
    private MovieRetriever movieRetriever;

    @Test
    public void test1() throws SuspiciousMovieException, IOException, JinniMovieNotFoundException
    {
        movieRetriever.retrieveUrlBySearch("Star Wars: Episode IV - A New Hope");
    }

    @Test
    public void test2() throws SuspiciousMovieException, IOException, JinniMovieNotFoundException
    {
        movieRetriever.retrieveUrlBySearch("The Matrix Reloaded");
    }
}
