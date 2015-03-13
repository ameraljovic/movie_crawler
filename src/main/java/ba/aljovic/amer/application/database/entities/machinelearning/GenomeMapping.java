package ba.aljovic.amer.application.database.entities.machinelearning;

import javax.persistence.*;

@Entity
@Table(name = "genome_mapping")
public class GenomeMapping
{
    @Id
    @Column (name = "id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column (name = "name")
    private String name;

    //region GETTERS & SETTERS

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    //endregion
}
