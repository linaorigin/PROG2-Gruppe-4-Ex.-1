package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movie")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = "imdbId")
    private String imdbId;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "genres")
    private String genres;
    @DatabaseField(columnName = "releaseYear")
    private int releaseYear;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "imgUrl")
    private String imgUrl;
    @DatabaseField(columnName = "lengthInMinutes")
    private int lengthInMinutes;
    @DatabaseField(columnName = "rating")
    private float rating;

    public String getImdbId() {
        return imdbId;
    }


    public MovieEntity() {
    }

    public MovieEntity(String imdbId,
                       String title,
                       List<Genres> genres,
                       int releaseYear,
                       String description,
                       String imgUrl,
                       int lengthInMinutes,
                       float rating) {
        this.imdbId = imdbId;
        this.title = title;
        this.genres = genresToString(genres);
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    public MovieEntity(Movie movie) {
        new MovieEntity(movie.getId(), movie.getTitle(), movie.getGenres(), movie.getReleaseYear(),
                        movie.getDescription(), movie.getImgUrl(), movie.getLengthInMinutes(), movie.getRating());
    }

    String genresToString(List<Genres> genres) {
        return genres.stream()
                     .map(n -> String.valueOf(n))
                     .collect(Collectors.joining(","));
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> outMovieEntities = new ArrayList<>();
        for (Movie m : movies) {
            outMovieEntities.add(new MovieEntity(m));
        }
        return outMovieEntities;
    }

    public static List<Movie> toMovies(List<MovieEntity> movies) {
        List<Movie> outMovies = new ArrayList<>();
        for (MovieEntity m : movies) {
            outMovies.add(new Movie(m.imdbId,
                                    m.title,
                                    Arrays.stream(m.genres.split(","))
                                          .map(e -> Genres.valueOf(e))
                                          .toList(),
                                    m.releaseYear,
                                    m.description,
                                    m.imgUrl,
                                    m.lengthInMinutes,
                                    null,
                                    null,
                                    null,
                                    m.rating));
        }
        return outMovies;
    }
}
