package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.batch.chunk.FailedMoviesReader;
import ba.aljovic.amer.batch.chunk.FailedMoviesWriter;
import ba.aljovic.amer.batch.chunk.JinniProcessor;
import ba.aljovic.amer.component.service.MovieRetriever;
import ba.aljovic.amer.database.MovieFacade;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketTimeoutException;

@Configuration
public class FailedMoviesJobConfiguration extends JobConfiguration
{
    @Autowired
    ItemReadListener<Movie> failedMoviesReaderListener;

    @Autowired
    MovieFacade movieFacade;

    @Autowired
    MovieRetriever movieRetriever;

    @Bean
    JobParametersIncrementer jobIncrementer()
    {
        return new RunIdIncrementer();
    }

    @Bean
    public Step failedMoviesStep()
    {
        return stepBuilder.get("failedMoviesStep")
                .<Movie, Movie>chunk(5)
                .reader(failedMoviesReader())
                .processor(failedMoviesProcessor())
                .writer(failedMoviesWriter())
                .faultTolerant()
                .skip(JinniMovieNotFoundException.class)
                .skip(SuspiciousMovieException.class)
                .skip(SocketTimeoutException.class)
                .skipLimit(100000)
                .retry(SocketTimeoutException.class)
                .retryLimit(10000)
                .listener(failedMoviesReaderListener)
                .build();
    }

    @Bean
    public Job failedMoviesJob()
    {
        return jobBuilder.get("failedMoviesJob")
                .incrementer(jobIncrementer())
                .listener(jinniJobListener)
                .start(failedMoviesStep())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Movie> failedMoviesReader()
    {
        FailedMoviesReader reader = new FailedMoviesReader();

        reader.setMovieFacade(movieFacade);
        reader.setMovieRetriever(movieRetriever);

        return reader;
    }

    @Bean
    public JinniProcessor failedMoviesProcessor()
    {
        return new JinniProcessor();
    }

    @Bean
    public ItemWriter<Movie> failedMoviesWriter()
    {
        return new FailedMoviesWriter();
    }
}
