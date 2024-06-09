package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.Observer.Observable;
import at.ac.fhcampuswien.fhmdb.Observer.Observer;
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

    private WatchListRepository() throws SQLException {
        this.dao = getDatabaseManager().getWatchlistDao();
        this.observers = new ArrayList<>();
    }
    public static synchronized WatchListRepository getInstance()throws SQLException{
        if (instance == null){
            instance = new WatchListRepository();
        }
        return instance;
    }

    public WatchListRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
        this.observers = new ArrayList<>();
    }

    public void addToWatchList(String ID) throws SQLException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        if (dao.queryForEq("imdbId", newMovie.getImdbId())
               .isEmpty()) {
            dao.create(newMovie);
            notifyObs("Movie was added to watchlist");
        }else {
            notifyObs("Movie is already on watchlist");
        }
    }

    public void removeWatchList(String ID) throws SQLException {
        WatchlistMovieEntity newMovie = new WatchlistMovieEntity(ID);
        if (!dao.queryForEq("imdbId", newMovie.getImdbId())
                .isEmpty()) {
            dao.delete(dao.queryForEq("imdbId", newMovie.getImdbId()));
            notifyObs("Movie was removed from watchlist");
        }
    }

    public void removeAllWatchMovies() throws SQLException {
        TableUtils.clearTable(getDatabaseManager().getConnectionSource(), WatchListRepository.class);
    }

    public List<WatchlistMovieEntity> getAllWatchMovies() throws SQLException {
        return dao.queryForAll();
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
        for (Observer observer: observers){
            observer.update(message);
        }
    }
}
