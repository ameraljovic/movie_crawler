package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.batch.chunk.MovieSiteWriter;
import ba.aljovic.amer.batch.chunk.MovieSiteProcessor;
import ba.aljovic.amer.batch.chunk.TmdbReader;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.MovieNotFoundException;
import ba.aljovic.amer.exception.TmdbMovieNotFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketTimeoutException;

@Configuration
public class MovieSiteJobConfiguration extends JobConfiguration
{
    @Bean
    public Step movieSiteSlaveStep()
    {
        return stepBuilder.get("movieSiteSlaveStep")
                .<Movie, Movie>chunk(5)
                .reader(tmdbReader(0, 10))
                .processor(movieSiteProcessor())
                .writer(movieSiteWriter())
                .faultTolerant()
                .skip(TmdbMovieNotFoundException.class)
                .skip(MovieNotFoundException.class)
                .skipLimit(100000)
                .retry(SocketTimeoutException.class)
                .retryLimit(10000)
                .listener(movieSiteProcessorListener)
                .build();
    }

    @Bean
    public Step movieSiteMasterStep()
    {
        return stepBuilder.get("movieSiteMasterStep")
                .partitioner(movieSiteSlaveStep())
                .partitioner("movieSiteSlaveStep", rangePartitioner(null, null))
                .taskExecutor(stepAsyncTaskExecutor)
                .gridSize(1)
                .build();
    }

    @Bean
    public Job movieSiteJob()
    {
        return jobBuilder.get("movieSiteJob")
                .incrementer(incrementer())
                .listener(movieSiteJobListener)
                .start(movieSiteMasterStep())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<Movie> tmdbReader(@Value ("#{stepExecutionContext['fromId']}") Integer fromId,
                                 @Value("#{stepExecutionContext['toId']}") Integer toId)
    {
        return new TmdbReader(fromId, toId);
    }

    @Bean
    public MovieSiteProcessor movieSiteProcessor()
    {
        return new MovieSiteProcessor();
    }

    @Bean
    public MovieSiteWriter movieSiteWriter()
    {
        return new MovieSiteWriter();
    }
}
