package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.Observer.Observable;
import at.ac.fhcampuswien.fhmdb.Observer.Observer;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static at.ac.fhcampuswien.fhmdb.data.DatabaseManager.getDatabaseManager;

public class WatchListRepository implements Observable {

    private final Dao<WatchlistMovieEntity, Long> dao;

    private final List<Observer> observers;

    private static WatchListRepository instance;

    private WatchListRepository() throws DatabaseException {
        this.dao = getDatabaseManager().getWatchlistDao();
        this.observers = new ArrayList<>();
    }

    public static synchronized WatchListRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new WatchListRepository();
        }
        return instance;
    }

    public WatchListRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
        this.observers = new ArrayList<>();
    }

    public void addToWatchList(String ID) throws DatabaseException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        try {
            if (dao.queryForEq("imdbId", newMovie.getImdbId())
                   .isEmpty()) {
                dao.create(newMovie);
                notifyObs("Movie was added to watchlist");
            } else {
                notifyObs("Movie is already on watchlist");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void removeWatchList(String ID) throws DatabaseException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        try {
            if (!dao.queryForEq("imdbId", newMovie.getImdbId())
                    .isEmpty()) {
                dao.delete(dao.queryForEq("imdbId", newMovie.getImdbId()));
                notifyObs("Moviewas removed from watchlist");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void removeAllWatchMovies() throws DatabaseException {
        try {
            TableUtils.clearTable(getDatabaseManager().getConnectionSource(), WatchListRepository.class);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<WatchlistMovieEntity> getAllWatchMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObs(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
