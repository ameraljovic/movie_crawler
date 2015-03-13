package ba.aljovic.amer.application.batch.chunk.fileexportjob;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.database.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        List<ImdbUser> imdbUsers = (List<ImdbUser>)usersRepository.findAll();
        imdbUsers.forEach(user -> {
            List<MovieRating>ratings = user.getRatings();
            if (!ratings.isEmpty())
            {
                logger.info("Writing movie ratings for user " + user.getUsername());
                FileWriter fileWriter = new FileWriter(genomeMappingRepository,  user);
                ratings.forEach(fileWriter::writeToFile);
                fileWriter.closeFileStream();
            }
        });
        return RepeatStatus.FINISHED;
    }
}
