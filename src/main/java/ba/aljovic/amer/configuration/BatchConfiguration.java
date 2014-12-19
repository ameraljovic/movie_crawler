package ba.aljovic.amer.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration
{
    @Bean
    public TaskExecutor asyncTaskExecutor()
    {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public TaskExecutor jobAsyncTaskExecutor()
    {
        return new SimpleAsyncTaskExecutor();
    }
}
