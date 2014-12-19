package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.JinniProcessor;
import ba.aljovic.amer.application.batch.chunk.JinniWriter;
import ba.aljovic.amer.application.batch.chunk.TmdbReader;
import ba.aljovic.amer.application.database.entity.Movie;
import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.application.exception.TmdbMovieNotFoundException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
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
                .skip(IOException.class)
                .skipLimit(100000)
                .retry(SocketTimeoutException.class)
                .retry(ConnectionPoolTimeoutException.class)
                .retry(ConnectTimeoutException.class)
                .retry(IOException.class)
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
                .taskExecutor(asyncTaskExecutor)
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
