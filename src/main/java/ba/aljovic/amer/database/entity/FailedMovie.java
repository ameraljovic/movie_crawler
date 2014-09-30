package ba.aljovic.amer.database.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "failed_movies", schema = "", catalog = "movg")
public class FailedMovie implements Serializable
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "title")
    private String title;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    //region GETTERS & SETTERS
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getStatusId()
    {
        return statusId;
    }

    public void setStatusId(Integer statusId)
    {
        this.statusId = statusId;
    }

    public String getImdbId()
    {
        return imdbId;
    }

    public void setImdbId(String imdbId)
    {
        this.imdbId = imdbId;
    }

    public Integer getTmdbId()
    {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId)
    {
        this.tmdbId = tmdbId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailedMovie that = (FailedMovie)o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (imdbId != null ? !imdbId.equals(that.imdbId) : that.imdbId != null) return false;
        if (statusId != null ? !statusId.equals(that.statusId) : that.statusId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (tmdbId != null ? !tmdbId.equals(that.tmdbId) : that.tmdbId != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (statusId != null ? statusId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (imdbId != null ? imdbId.hashCode() : 0);
        result = 31 * result + (tmdbId != null ? tmdbId.hashCode() : 0);
        return result;
    }

//endregion
}
