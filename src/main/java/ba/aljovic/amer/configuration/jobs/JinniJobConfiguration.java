package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.batch.chunk.JinniProcessor;
import ba.aljovic.amer.batch.chunk.JinniWriter;
import ba.aljovic.amer.batch.chunk.TmdbReader;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.exception.TmdbMovieNotFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.net.SocketTimeoutException;

@Configuration
public class JinniJobConfiguration extends JobConfiguration
{
    @Bean
    public Step jinniSlaveStep()
    {
        return stepBuilder.get("jinniSlaveStep")
                .<Movie, Movie>chunk(5)
                .reader(tmdbReader(0, 10))
                .processor(jinniProcessor())
                .writer(jinniWriter())
                .faultTolerant()
                .processorNonTransactional()
                .skip(TmdbMovieNotFoundException.class)
                .skip(JinniMovieNotFoundException.class)
                .skip(DataIntegrityViolationException.class)
                .skipLimit(100000)
                .retry(SocketTimeoutException.class)
                .retryLimit(10000)
                .listener(jinniProcessorListener)
                .build();
    }

    @Bean
    public Step jinniMasterStep()
    {
        return stepBuilder.get("jinniMasterStep")
                .partitioner(jinniSlaveStep())
                .partitioner("jinniSlaveStep", rangePartitioner(null, null))
                .taskExecutor(stepAsyncTaskExecutor)
                .gridSize(1)
                .build();
    }

    @Bean
    public Job jinniJob()
    {
        return jobBuilder.get("jinniJob")
                .incrementer(incrementer())
                .listener(jinniJobListener)
                .start(jinniMasterStep())
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
    public JinniProcessor jinniProcessor()
    {
        return new JinniProcessor();
    }

    @Bean
    public JinniWriter jinniWriter()
    {
        return new JinniWriter();
    }
}
