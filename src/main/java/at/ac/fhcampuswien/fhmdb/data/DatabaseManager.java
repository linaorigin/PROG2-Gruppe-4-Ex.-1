package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {

    public static final String DB_URL = "jdbc:h2:file:./db/movies";
    private static final String username = "user";
    private static final String password = "pass";

    private static ConnectionSource conn;
    Dao<MovieEntity, Long> movieDao;

    Dao<WatchlistMovieEntity, Long> watchlistDao;

    private static DatabaseManager instance;

    private DatabaseManager() throws SQLException {
        createConnectionSource();
        movieDao = DaoManager.createDao(conn, MovieEntity.class);
        watchlistDao = DaoManager.createDao(conn, WatchlistMovieEntity.class);
        createTables();
    }

    public static DatabaseManager getDatabaseManager() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private static void createConnectionSource() throws SQLException {
        conn = new JdbcConnectionSource(DB_URL, username, password);
    }
    
    public ConnectionSource getConnectionSource() throws SQLException {
        if (conn == null) {
            createConnectionSource();
        }
        return conn;
    }

    public void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(conn, MovieEntity.class);
        TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }
}
