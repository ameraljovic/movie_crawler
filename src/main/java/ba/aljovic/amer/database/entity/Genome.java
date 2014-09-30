package ba.aljovic.amer.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table (name = "genomes")
public class Genome
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genome", cascade = CascadeType.PERSIST)
    private Collection<GenomeValue> genomes;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    public Genome()
    {
        genomes = new ArrayList<>();
    }

    public Genome(String name)
    {
        this.name = name;
        genomes = new ArrayList<>();
    }

    public void add(String genome)
    {
        GenomeValue genomeValue = new GenomeValue();
        genomeValue.setName(genome);
        genomeValue.setGenome(this);
        genomes.add(genomeValue);
    }

    //region GETTERS & SETTERS
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Collection<GenomeValue> getGenomes()
    {
        return genomes;
    }

    public void setGenomes(Collection<GenomeValue> genomes)
    {
        this.genomes = genomes;
    }

    public Movie getMovie()
    {
        return movie;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genome that = (Genome)o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
    //endregion
}
