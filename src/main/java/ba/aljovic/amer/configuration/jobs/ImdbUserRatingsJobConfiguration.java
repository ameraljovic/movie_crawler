package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.userratings.ImdbMovieProcessor;
import ba.aljovic.amer.application.batch.chunk.userratings.ImdbMoviesReader;
import ba.aljovic.amer.application.batch.chunk.userratings.ImdbUsersWriter;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ImdbUserRatingsJobConfiguration extends JobConfiguration
{
    @Bean
    public Step getMoviesStep()
    {
        return stepBuilder.get("getMoviesStep")
                .<ImdbMovie, List<ImdbUser>>chunk(1)
                .reader(imdbMoviesReader())
                .processor(imdbMovieProcessor())
                .writer(imdbWriter())
                .build();
    }

    @Bean
    public Job userRatingsJob()
    {
        return jobBuilder.get("userRatingsJob")
                .incrementer(incrementer())
                .start(getMoviesStep())
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
}