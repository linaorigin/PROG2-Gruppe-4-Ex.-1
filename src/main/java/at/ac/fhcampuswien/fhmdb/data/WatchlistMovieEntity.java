package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist-movie")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = "imdbId")
    private String imdbId;

    public String getImdbId() {
        return imdbId;
    }


    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(String imdbId) {
        this.imdbId = imdbId;
    }

}
