package ba.aljovic.amer.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@ComponentScan
@Configuration
public class BatchConfiguration
{
    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    DataSource dataSource;

    @Autowired
    JobExplorer jobExplorer;

    @Bean
    public TaskExecutor stepAsyncTaskExecutor()
    {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public TaskExecutor jobAsyncTaskExecutor()
    {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public JobRegistry jobRegistry()
    {
        return new MapJobRegistry();
    }

    @Bean
    public JobOperator jobOperator()
    {
        SimpleJobOperator jobOperator = new SimpleJobOperator();

        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry());

        return jobOperator;
    }
}
