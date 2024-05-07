package at.ac.fhcampuswien.fhmdb.data;

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

    public MovieRepository() throws SQLException {
        this.dao = getDatabaseManager().getMovieDao();
    }

    public void addAllMoviesList(List<Movie> movies) throws SQLException {
        for (Movie m : movies) {
            MovieEntity newMovie = new MovieEntity(m);
            if (dao.queryForMatching(newMovie)
                   .isEmpty()) {
                dao.create(newMovie);
            }
        }
    }

    public void addToMovieList(Movie movie) throws SQLException {
        MovieEntity newMovie = new MovieEntity(movie);
        if (dao.queryForMatching(newMovie)
               .isEmpty()) {
            dao.create(newMovie);
        }
    }

    public void removeFromMovieList(Movie movie) throws SQLException {
        MovieEntity newMovie = new MovieEntity(movie);
        if (!dao.queryForMatching(newMovie)
                .isEmpty()) {
            dao.delete(newMovie);
        }
    }

    public void removeAllMovies() throws SQLException {
        TableUtils.clearTable(getDatabaseManager().getConnectionSource(), MovieRepository.class);
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public List<MovieEntity> getMovies(List<String> ids) throws SQLException {
        return dao.queryBuilder()
                  .where()
                  .in("imdbId", ids)
                  .query();
    }

    public MovieEntity getMovie(String imdbID) throws SQLException {
        return dao.queryBuilder()
                  .where()
                  .eq("imdbId", imdbID)
                  .queryForFirst();
    }
}
