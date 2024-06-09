package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.data.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.data.MovieRepository;
import javafx.util.Callback;

import java.sql.SQLException;

import static at.ac.fhcampuswien.fhmdb.data.DatabaseManager.getDatabaseManager;

public class HomeControllerFactory implements Callback<Class<?>, Object> {
    private HomeController homeControllerInstance;

    @Override
    public Object call(Class<?> aClass) {
        if (aClass == HomeController.class) {
            if (homeControllerInstance == null) {
                try {
                    homeControllerInstance = new HomeController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return homeControllerInstance;
        }
        try {
            return aClass.getDeclaredConstructor()
                         .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

