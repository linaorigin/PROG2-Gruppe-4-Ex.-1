package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        sortMovies(observableMovies, sortBtn.getText().equals("Sort (desc)"));

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genres.values());

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        searchBtn.setOnAction(actionEvent -> {
            observableMovies.setAll(filterMovies(allMovies, searchField.getText(), (Genres) genreComboBox.getValue()));
            sortMovies(observableMovies, sortBtn.getText().equals("Sort (desc)"));
        });

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
                sortBtn.setText("Sort (desc)");
                sortMovies(observableMovies, true);
            } else {
                // TODO sort observableMovies descending
                sortBtn.setText("Sort (asc)");
                sortMovies(observableMovies, false);
            }
        });


    }

    void sortMovies(List<Movie> listToSort, boolean reverseOrder) {
        listToSort.sort((m1, m2) -> {
            return m1.getTitle().compareTo(m2.getTitle());
        });
        if (reverseOrder) {
            Collections.reverse(listToSort);
        }
    }

    List<Movie> filterMovies(List<Movie> listToFilter, String text, Genres genre) {
        if (genre != Genres.REMOVE_FILTER) {
            return listToFilter.stream()
                    .filter(movie -> (movie.getTitle()
                            .toLowerCase()
                            .contains(text.toLowerCase())
                            || movie.getDescription()
                            .toLowerCase()
                            .contains(text.toLowerCase()))
                            && (genre == null
                            || movie.getGenres().contains(genre)))
                    .toList();
        } else {
            return listToFilter.stream()
                    .filter(movie -> (movie.getTitle()
                            .toLowerCase()
                            .contains(text.toLowerCase())
                            || movie.getDescription()
                            .toLowerCase()
                            .contains(text.toLowerCase())))
                    .toList();
        }
    }
}