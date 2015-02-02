package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.ImdbUsersPartitioner;
import ba.aljovic.amer.application.batch.chunk.userratings.*;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.exception.PageNotFoundException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

@Configuration
public class ImdbUserRatingsJobConfiguration extends JobConfiguration
{
    @Bean
    public Step getMoviesStep()
    {
        return stepBuilder.get("getMoviesStep")
                .<ImdbMovie, List<ImdbUser>>chunk(1)
                .faultTolerant()
                .skip(PageNotFoundException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(1000000)
                .reader(imdbMoviesReader())
                .processor(imdbMovieProcessor())
                .writer(imdbWriter())
                .build();
    }

    @Bean
    public Step getMovieRatingsMasterStep()
    {
        return stepBuilder.get("getMovieRatingsMasterStep")
                .partitioner(getMovieRatingsSlaveStep())
                .partitioner("getMovieRatingsSlaveStep", imdbUsersPartitioner())
                .taskExecutor(asyncTaskExecutor)
                .gridSize(8)
                .build();
    }

    @Bean
    public Step getMovieRatingsSlaveStep()
    {
        return stepBuilder.get("")
                .<ImdbUser, List<MovieRating>>chunk(1)
                .faultTolerant()

                .skip(PageNotFoundException.class)
                .skipLimit(1000000)

                .retry(SocketTimeoutException.class)
                .retry(ConnectionPoolTimeoutException.class)
                .retry(ConnectTimeoutException.class)
                .retry(IOException.class)
                .retryLimit(1000000)

                .reader(imdbUsersReader(null, null))
                .processor(imdbUserProcessor())
                .writer(movieRatingsWriter())

                .build();
    }

    @Bean
    public Job imdbMoviesJob()
    {
        return jobBuilder.get("imdbMoviesJob")
                .incrementer(incrementer())
                .start(getMoviesStep())
                .build();
    }

    @Bean
    public Job imdbRatingsJob()
    {
        return jobBuilder.get("imdbRatingsJob")
                .incrementer(incrementer())
                .start(getMovieRatingsMasterStep())
                .build();
    }

    @Bean
    public Partitioner imdbUsersPartitioner()
    {
        return new ImdbUsersPartitioner();
    }

    @Bean
    @StepScope
    public ItemReader<ImdbMovie> imdbMoviesReader()
    {
        return new ImdbMoviesReader();
    }

    @Bean
    public ItemProcessor<ImdbMovie, List<ImdbUser>> imdbMovieProcessor()
    {
        return new ImdbMovieProcessor();
    }

    @Bean
    public ItemWriter<List<ImdbUser>> imdbWriter()
    {
        return new ImdbUsersWriter();
    }

    @Bean
    @StepScope
    public ItemStreamReader<ImdbUser> imdbUsersReader(
            @Value ("#{stepExecutionContext['fromId']}") Long fromId,
            @Value ("#{stepExecutionContext['toId']}") Long toId)
    {
        return new ImdbUserReader(fromId, toId);
    }

    @Bean
    public ItemProcessor<ImdbUser, List<MovieRating>> imdbUserProcessor()
    {
        return new ImdbUserProcessor();
    }

    @Bean
    public ItemWriter<List<MovieRating>> movieRatingsWriter()
    {
        return new MovieRatingWriter();
    }
}