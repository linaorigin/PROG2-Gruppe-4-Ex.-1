package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.h2.engine.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static at.ac.fhcampuswien.fhmdb.data.DatabaseManager.getDatabaseManager;

public class MovieRepository {
    private final Dao<MovieEntity, Long> dao;

    private static MovieRepository instance;

    private MovieRepository() throws DatabaseException {
        this.dao = getDatabaseManager().getMovieDao();
    }

    public static synchronized MovieRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    public void addAllMoviesList(List<Movie> movies) throws DatabaseException {
        try {
            for (Movie m : movies) {
                MovieEntity newMovie = new MovieEntity(m);
                if (dao.queryForEq("imdbId", newMovie.getImdbId())
                       .isEmpty()) {
                    dao.create(newMovie);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void addToMovieList(Movie movie) throws DatabaseException {
        MovieEntity newMovie = new MovieEntity(movie);
        try {
            if (dao.queryForEq("imdbId", newMovie.getImdbId())
                   .isEmpty()) {
                dao.create(newMovie);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void removeFromMovieList(Movie movie) throws DatabaseException {
        MovieEntity newMovie = new MovieEntity(movie);
        try {
            if (!dao.queryForEq("imdbId", newMovie.getImdbId())
                    .isEmpty()) {
                dao.delete(newMovie);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void removeAllMovies() throws DatabaseException {
        try {
            TableUtils.clearTable(getDatabaseManager().getConnectionSource(), MovieRepository.class);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<MovieEntity> getMovies(List<String> ids) throws DatabaseException {
        try {
            return dao.queryBuilder()
                      .where()
                      .in("imdbId", ids)
                      .query();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public MovieEntity getMovie(String imdbID) throws DatabaseException {
        try {
            return dao.queryBuilder()
                      .where()
                      .eq("imdbId", imdbID)
                      .queryForFirst();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
