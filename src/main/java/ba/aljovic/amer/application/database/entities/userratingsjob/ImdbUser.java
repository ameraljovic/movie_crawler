package ba.aljovic.amer.application.database.entities.userratingsjob;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "imdb_users")
public class ImdbUser
{
    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "movie")
    private List<MovieRating> ratings;

    //region GETTERS & SETTERS
    public Long getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public List<MovieRating> getRatings()
    {
        return ratings;
    }

    public void setRatings(List<MovieRating> ratings)
    {
        this.ratings = ratings;
    }
    //endregion
}