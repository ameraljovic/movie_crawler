package ba.aljovic.amer.application.batch.chunk.fileexportjob;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.database.repositories.GenomeRepository;
import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import ba.aljovic.amer.application.database.repositories.MovieRatingRepository;
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
    private MovieRatingRepository movieRatingRepository;
    private GenomeRepository genomeRepository;

    @Autowired
    public void setUsersRepository(ImdbUsersRepository usersRepository)
    {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setMovieRatingRepository(MovieRatingRepository movieRatingRepository)
    {
        this.movieRatingRepository = movieRatingRepository;
    }

    @Autowired
    public void setGenomeRepository(GenomeRepository genomeRepository)
    {
        this.genomeRepository = genomeRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
        List<ImdbUser> imdbUsers = (List<ImdbUser>)usersRepository.findAll();
        imdbUsers.subList(0, 100)
                .parallelStream()
                .forEach(user -> {
                    logger.info("Writing movie ratings for user " + user.getUsername());
                    List<MovieRating> ratings = movieRatingRepository.findByUser(user);
                    if (!ratings.isEmpty())
                    {
                        FileWriter fileWriter = new FileWriter(genomeRepository, user);
                        ratings.forEach(rating -> {
                            logger.info("Writing movie ratings for user " + user.getUsername() +
                                    " and movie " + rating.getMovie().getTitle());
                            fileWriter.writeToFile(rating);
                        });
                        fileWriter.closeFileStream();
                    }
                });
        return RepeatStatus.FINISHED;
    }
}
