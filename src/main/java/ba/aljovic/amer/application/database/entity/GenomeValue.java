package ba.aljovic.amer.application.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "genome_values")
public class GenomeValue
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "genome_id", referencedColumnName = "id")
    private Genome genome;

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

    public Genome getGenome()
    {
        return genome;
    }

    public void setGenome(Genome genome)
    {
        this.genome = genome;
    }
    //endregion
}
