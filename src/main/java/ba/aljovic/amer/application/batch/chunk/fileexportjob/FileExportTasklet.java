package ba.aljovic.amer.application.batch.chunk.fileexportjob;

import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.database.repositories.GenomeMappingRepository;
import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

public class FileExportTasklet implements Tasklet
{
    private static Logger logger = LoggerFactory.getLogger(FileExportTasklet.class);
    private ImdbUsersRepository usersRepository;
    private GenomeMappingRepository genomeMappingRepository;

    @Autowired
    public void setUsersRepository(ImdbUsersRepository usersRepository)
    {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setGenomeMappingRepository(GenomeMappingRepository genomeMappingRepository)
    {
        this.genomeMappingRepository = genomeMappingRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
        long startTime = System.currentTimeMillis();

        usersRepository.findAll().forEach(user -> {
            List<MovieRating> ratings = user.getRatings();
            if (!ratings.isEmpty())
            {
                logger.info("Writing movie ratings for user " + user.getUsername());
                int sample = ratings.size() / 10;

                FileWriter testDataWriter = new FileWriter(genomeMappingRepository, user, "user-ratings-test");
                IntStream.range(0, sample)
                        .mapToObj(ratings::get)
                        .forEach(testDataWriter::writeToFile);
                testDataWriter.closeFileStream();

                FileWriter trainingDataWriter = new FileWriter(genomeMappingRepository, user, "user-ratings-training");
                IntStream.range(sample, ratings.size())
                        .mapToObj(ratings::get)
                        .forEach(trainingDataWriter::writeToFile);
                trainingDataWriter.closeFileStream();
            }
        });

        long endTime = System.currentTimeMillis();
        System.out.println("Job completed in " + (endTime - startTime) / 1000);

        return RepeatStatus.FINISHED;
    }
}
