package at.ac.fhcampuswien.fhmdb.models;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.ac.fhcampuswien.fhmdb.MovieAPI;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Alert;

public class Movie {
    private final String id;
    private final String title;
    private final List<Genres> genres;
    private final int releaseYear;
    private final String description;
    private final String imgUrl;
    private final int lengthInMinutes;
    private final List<String> directors;
    private final List<String> writers;
    private final List<String> mainCast;
    private final float rating;

    // give jackson context to use constructor for json -> Movie mapping
    @JsonCreator
    public Movie(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("genres") List<Genres> genres,
                 @JsonProperty("releaseYear") int releaseYear,
                 @JsonProperty("description") String description,
                 @JsonProperty("imgUrl") String imgUrl,
                 @JsonProperty("lengthInMinutes") int lengthInMinutes,
                 @JsonProperty("directors") List<String> directors,
                 @JsonProperty("writers") List<String> writers,
                 @JsonProperty("mainCast") List<String> mainCast,
                 @JsonProperty("rating") float rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public float getRating() {
        return rating;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getWriters() {
        return writers;
    }


    public static List<Movie> initializeMovies() {
        List<Movie> m = new ArrayList<>();
        try {
            m = MovieAPI.getMovies(null, null, null, null);
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("IOException: MovieAPI");
            a.setContentText(ex.getLocalizedMessage());
            a.showAndWait();
        }
        return m;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Movie other = (Movie) obj;
        return Objects.equals(title, other.getTitle()) &&
               Objects.equals(genres, other.getGenres()) &&
               Objects.equals(releaseYear, other.getReleaseYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genres, releaseYear);
    }

}
