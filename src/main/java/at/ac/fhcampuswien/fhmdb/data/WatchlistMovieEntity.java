package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = "imdbId")
    private String imdbId;

    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(String imdbId) {
        this.imdbId = imdbId;
    }

}
