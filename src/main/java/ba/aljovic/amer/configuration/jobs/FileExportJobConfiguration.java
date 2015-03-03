package ba.aljovic.amer.configuration.jobs;

import ba.aljovic.amer.application.batch.chunk.fileexportjob.FileExportTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileExportJobConfiguration extends JobConfiguration
{
    @Bean
    public Job fileExportJob()
    {
        return jobBuilder.get("fileExportJob")
                .incrementer(incrementer())
                .start(fileExportStep())
                .build();
    }

    @Bean
    public Step fileExportStep()
    {
        return stepBuilder.get("fileExportStep")
                .tasklet(fileExportTasklet())
                .build();
    }

    @Bean
    Tasklet fileExportTasklet()
    {
        return new FileExportTasklet();
    }
}