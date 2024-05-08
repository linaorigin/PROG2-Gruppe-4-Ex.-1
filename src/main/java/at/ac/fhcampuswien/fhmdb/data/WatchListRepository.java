package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import static at.ac.fhcampuswien.fhmdb.data.DatabaseManager.getDatabaseManager;

public class WatchListRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchListRepository() throws SQLException {
        this.dao = getDatabaseManager().getWatchlistDao();
    }

    public WatchListRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public void addToWatchList(String ID) throws SQLException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        if (dao.queryForEq("imdbId", newMovie.getImdbId())
               .isEmpty()) {
            dao.create(newMovie);
        }
    }

    public void removeWatchList(String ID) throws SQLException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        if (!dao.queryForEq("imdbId", newMovie.getImdbId())
                .isEmpty()) {
            dao.delete(dao.queryForEq("imdbId", newMovie.getImdbId()));
        }
    }

    public void removeAllWatchMovies() throws SQLException {
        TableUtils.clearTable(getDatabaseManager().getConnectionSource(), WatchListRepository.class);
    }

    public List<WatchlistMovieEntity> getAllWatchMovies() throws SQLException {
        return dao.queryForAll();
    }

}
