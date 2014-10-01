package ba.aljovic.amer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@ComponentScan
@Configuration
public class BatchConfiguration
{
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
}
