package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.userratings.*;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.exception.NoPageException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

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
                .skip(NoPageException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(1000000)
                .reader(imdbMoviesReader())
                .processor(imdbMovieProcessor())
                .writer(imdbWriter())
                .build();
    }

    @Bean
    public Step getMovieRatingsStep()
    {
        return stepBuilder.get("getMovieRatingsStep")
                .<ImdbUser, List<MovieRating>>chunk(1)
                .faultTolerant()
                .skip(NoPageException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(1000000)
                .reader(imdbUsersReader())
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
                .start(getMovieRatingsStep())
                .build();
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
    public ItemStreamReader<ImdbUser> imdbUsersReader()
    {
        return new ImdbUserReader();
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