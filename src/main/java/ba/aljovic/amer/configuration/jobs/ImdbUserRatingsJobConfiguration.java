package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.userratings.ImdbMoviesReader;
import ba.aljovic.amer.application.batch.chunk.userratings.ImdbMoviesWriter;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImdbUserRatingsJobConfiguration extends JobConfiguration
{
    @Bean
    public Step getMoviesStep()
    {
        return stepBuilder.get("getMoviesStep")

                .<ImdbMovie, ImdbMovie>chunk(5)
                .reader(imdbMoviesReader())
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
    public ItemWriter<ImdbMovie> imdbWriter()
    {
        return new ImdbMoviesWriter();
    }
}