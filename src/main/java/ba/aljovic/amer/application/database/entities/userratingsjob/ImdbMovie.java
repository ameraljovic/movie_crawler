package ba.aljovic.amer.application.database.entities.userratingsjob;

import javax.persistence.*;

@Entity()
@Table(name = "staging_imdb_movies")
public class ImdbMovie
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    //region GETTERS & SETTERS
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    //endregion
}