package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Genres;

import java.util.List;

public class WatchlistMovieEntity extends MovieEntity {



    public WatchlistMovieEntity(String imdbId,
                                 String title,
                                 List<Genres> genres,
                                 int releaseYear,
                                 String description,
                                 String imgUrl,
                                 int lengthInMinutes,
                                 float rating) {
        super(imdbId, title, genres, releaseYear, description, imgUrl, lengthInMinutes, rating);
    }

}
