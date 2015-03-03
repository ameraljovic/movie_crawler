package ba.aljovic.amer.application.batch.chunk.fileexportjob;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.database.repositories.GenomeRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class FileWriter
{
    private BufferedWriter bw;

    private GenomeRepository genomeRepository;

    public FileWriter(GenomeRepository genomeRepository, ImdbUser user)
    {
        this.genomeRepository = genomeRepository;
        File file = new File("./build/" + user.getUsername() + ".txt");
        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
            bw = new BufferedWriter(new java.io.FileWriter(file));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void closeFileStream()
    {
        try
        {
            bw.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(MovieRating rating)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(rating.getMovie().getId());
        sb.append("::");
        sb.append(rating.getUser().getId());
        sb.append("::");
        sb.append(rating.getRating());

        genomeRepository.findByMovie(rating.getMovie())
                .forEach(genomeValue -> {
                    sb.append(":");
                    sb.append(genomeValue.getId());
                });
        sb.append(System.lineSeparator());

        writeToFile(sb);
    }

    public void writeToFile(StringBuilder sb)
    {
        try
        {
            bw.write(sb.toString());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
