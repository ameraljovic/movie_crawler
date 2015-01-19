package ba.aljovic.amer.application.database.entities.jinnijob;

import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "movies")
public class Movie
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "movie",
               fetch = FetchType.EAGER,
               cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private Collection<Genome> genomes;

    @OneToMany(mappedBy = "movie",
               fetch = FetchType.LAZY)
    private Collection<MovieRating> ratings;

    public Movie()
    {
        genomes = new ArrayList<>();
    }

    public Movie(String title, String imdbId, Integer tmdbId, String url)
    {
        this.title = title;
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
        this.url = url;

        genomes = new ArrayList<>();
    }

    public void add(Genome genome)
    {
        genome.setMovie(this);
        genomes.add(genome);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    //region GETTERS & SETTERS
    public String getTitle()
    {
        return title;
    }

    public String getImdbId()
    {
        return imdbId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getTmdbId()
    {
        return tmdbId;
    }

    public Collection<Genome> getGenomes()
    {
        return genomes;
    }

    public void setGenomes(Collection<Genome> genomes)
    {
        this.genomes = genomes;
    }

    public Collection<MovieRating> getRatings()
    {
        return ratings;
    }

    public void setRatings(Collection<MovieRating> ratings)
    {
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie)o;

        if (id != null ? !id.equals(movie.id) : movie.id != null) return false;
        if (imdbId != null ? !imdbId.equals(movie.imdbId) : movie.imdbId != null) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (tmdbId != null ? !tmdbId.equals(movie.tmdbId) : movie.tmdbId != null) return false;
        if (url != null ? !url.equals(movie.url) : movie.url != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (imdbId != null ? imdbId.hashCode() : 0);
        result = 31 * result + (tmdbId != null ? tmdbId.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
    //endregion
}
