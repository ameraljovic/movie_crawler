package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.RangePartitioner;
import ba.aljovic.amer.application.component.service.HttpRetriever;
import ba.aljovic.amer.application.component.service.JinniParser;
import ba.aljovic.amer.application.component.service.MovieRetriever;
import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.skip.ExceptionClassifierSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;

public class JobConfiguration
{
    @SuppressWarnings ("SpringJavaAutowiringInspection")
    @Autowired
    protected JobBuilderFactory jobBuilder;

    @SuppressWarnings ("SpringJavaAutowiringInspection")
    @Autowired
    protected StepBuilderFactory stepBuilder;

    @Autowired
    protected ItemProcessListener<Movie, Movie> jinniProcessorListener;

    @Autowired
    protected JobExecutionListener jinniJobListener;

    @Autowired
    protected ItemReadListener<Movie> failedMoviesReaderListener;

    @Autowired
    protected TaskExecutor asyncTaskExecutor;

    @Autowired
    protected ImdbUsersRepository imdbUsersRepository;

    @Autowired
    protected JinniParser jinniParser;

    @Autowired
    protected HttpRetriever httpRetriever;

    @Autowired
    protected MovieFacade movieFacade;

    @Autowired
    protected MovieRetriever movieRetriever;

    @Bean
    @StepScope
    public Partitioner rangePartitioner(@Value ("#{jobParameters['FROM_ID']}") Integer fromId,
                                        @Value ("#{jobParameters['RANGE']}") Integer range)
    {
        return new RangePartitioner(fromId, range);
    }

    @Bean
    public JobParametersIncrementer incrementer()
    {
        return new RunIdIncrementer();
    }

    @Bean
    public SkipPolicy skipPolicy()
    {
        return new ExceptionClassifierSkipPolicy();
    }

    @Bean
    public RetryPolicy retryPolicy()
    {
        return new ExceptionClassifierRetryPolicy();
    }
}
