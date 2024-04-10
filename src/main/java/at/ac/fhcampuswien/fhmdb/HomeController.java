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
import java.util.stream.Collectors;

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

    public List<Movie> allMovies = MovieAPI.getMovies(null,null,null,null);

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
            observableMovies.setAll(filterMovies(allMovies,searchField.getText(),(Genres) genreComboBox.getValue()));
            observableMovies.setAll(sortMovies(observableMovies, sortBtn.getText().equals("Sort (desc)")));
        });

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
                sortBtn.setText("Sort (desc)");
                observableMovies.setAll(sortMovies(observableMovies,true));
            } else {
                // TODO sort observableMovies descending
                sortBtn.setText("Sort (asc)");
                observableMovies.setAll(sortMovies(observableMovies,false));
            }
        });


    }
    List<Movie> sortMovies(List<Movie> listToSort, boolean reverseOrder){
        if (reverseOrder) {
            return listToSort.stream()
                             .sorted(Comparator.comparing(Movie::getTitle)
                                               .reversed())
                             .toList();
        }
        return listToSort.stream()
                         .sorted(Comparator.comparing(Movie::getTitle))
                         .toList();
    }

    List<Movie> filterMovies(List<Movie> listToFilter, String text, Genres genre) {
        return listToFilter.stream()
                .filter(movie -> (movie.getTitle()
                        .toLowerCase()
                        .contains(text.toLowerCase())
                        || movie.getDescription()
                        .toLowerCase()
                        .contains(text.toLowerCase()))
                        && (genre == null
                        || genre == Genres.REMOVE_FILTER
                        || movie.getGenres().contains(genre)))
                .toList();
    }
    private String getMostPopularActor(List<Movie> movies){
        Map<String, Long> mainCastCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(
                        castMember -> castMember,
                        Collectors.counting()
                ));
        Map.Entry<String,Long> mostCommonCastMember = mainCastCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        return mostCommonCastMember.getKey();
    }

    private int getLongestMovieTitle(List<Movie> movies){
    return movies.stream()
            .map(movie -> movie.getTitle().length())
            .max(Integer::compare).get();
    }
    private long countMoviesFrom(List<Movie> movies, String director){
        return (int) movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }
    private List<Movie> getMoviesBetweenYears(List<Movie> movies,int startYear,int endYear){
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear()<=endYear)
                .collect(Collectors.toList());
    }
}