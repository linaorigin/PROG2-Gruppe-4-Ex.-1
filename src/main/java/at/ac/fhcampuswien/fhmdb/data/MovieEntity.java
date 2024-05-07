package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    @DatabaseField(columnName = "title")
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
        this.genres = genres.stream()
                            .map(n -> String.valueOf(n))
                            .collect(Collectors.joining("-"));
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }
}
