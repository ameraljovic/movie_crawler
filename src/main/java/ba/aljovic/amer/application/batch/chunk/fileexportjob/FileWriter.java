package ba.aljovic.amer.application.batch.chunk.fileexportjob;

import ba.aljovic.amer.application.database.entities.jinnijob.Genome;
import ba.aljovic.amer.application.database.entities.jinnijob.GenomeValue;
import ba.aljovic.amer.application.database.entities.machinelearning.GenomeMapping;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import ba.aljovic.amer.application.database.repositories.GenomeMappingRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FileWriter
{
    private final List<GenomeMapping> allGenomes;
    private BufferedWriter bw;

    public FileWriter(GenomeMappingRepository genomeMappingRepository, ImdbUser user)
    {
        allGenomes = (List<GenomeMapping>)genomeMappingRepository.findAll();

        File file = new File("movie-ratings/" + user.getUsername() + ".txt");
        try
        {
            if (!file.exists())
            {
                file.getParentFile().mkdirs();
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
        sb.append(rating.getMovie().getTitle());
        sb.append("::");
        sb.append(rating.getUser().getUsername());
        sb.append("::");
        sb.append(rating.getRating());
        sb.append("::");

        List<GenomeValue> genomes = rating.getMovie()
                .getGenomes()
                .stream()
                .map(Genome::getGenomes)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        allGenomes.forEach(genome -> {

            sb.append(genomes.stream().map(GenomeValue::getName).collect(Collectors.toList()).contains(genome.getName())
                    ? 1 : 0);
            sb.append(":");
        });
        sb.append("::");
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
