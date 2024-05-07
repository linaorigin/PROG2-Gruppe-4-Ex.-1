package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private final Dao<MovieEntity, Long> dao;


    public List<MovieEntity> getAllMovies() {
        return new ArrayList<MovieEntity>();
    }

    public MovieEntity getMovie() {
        
    }


}
