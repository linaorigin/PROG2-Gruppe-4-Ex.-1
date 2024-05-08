package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.data.MovieEntity;
import at.ac.fhcampuswien.fhmdb.data.MovieRepository;
import at.ac.fhcampuswien.fhmdb.data.WatchListRepository;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox<String> releaseYearComboBox;

    @FXML
    public JFXComboBox<String> ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public Button sceneBtn; //new ImageView(getClass().getResource("/at/ac/fhcampuswien/fhmdb/star.png").toExternalForm());

    public boolean homeScene = true;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private MovieRepository mRepo;
    private WatchListRepository wRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            mRepo = new MovieRepository();
            wRepo = new WatchListRepository();
            mRepo.addAllMoviesList(allMovies);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        observableMovies.addAll(allMovies);
        // sort list as it instances unsorted
        observableMovies.setAll(sortMovies(observableMovies,
                                           sortBtn.getText()
                                                  .equals("Sort (desc)")));


        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems()
                     .addAll(Genres.values());
        genreComboBox.getItems()
                     .add(0, "");

        releaseYearComboBox.getItems()
                           .addAll(allMovies.stream()
                                            .sorted(Comparator.comparing(Movie::getReleaseYear)
                                                              .reversed())
                                            .map(movie -> String.valueOf(movie.getReleaseYear()))
                                            .distinct()
                                            .toList());
        releaseYearComboBox.setPromptText("Release year");
        releaseYearComboBox.getItems()
                           .add(0, "");


        ratingComboBox.getItems()
                      .addAll(IntStream.rangeClosed(1, 9)
                                       .boxed()
                                       .sorted(Collections.reverseOrder())
                                       .map(String::valueOf)
                                       .toList());
        ratingComboBox.setPromptText("Min Rating");
        ratingComboBox.getItems()
                      .add(0, "");


        searchBtn.setOnAction(actionEvent -> {
            try {
                observableMovies.setAll(filterMovies(allMovies,
                                                     (!Objects.equals(searchField.getText(), "") ?
                                                             searchField.getText() :
                                                             null),
                                                     (genreComboBox.getValue() != "" ?
                                                             (Genres) genreComboBox.getValue() :
                                                             null),
                                                     (!Objects.equals(releaseYearComboBox.getValue(), "") ?
                                                             releaseYearComboBox.getValue() :
                                                             null),
                                                     (!Objects.equals(ratingComboBox.getValue(), "") ?
                                                             ratingComboBox.getValue() :
                                                             null)
                ));
                throw new IOException();
            } catch (IOException e) {
                observableMovies.setAll(filterLocalMovies(allMovies,
                                                          (!Objects.equals(searchField.getText(), "") ?
                                                                  searchField.getText() :
                                                                  null),
                                                          (genreComboBox.getValue() != "" ?
                                                                  (Genres) genreComboBox.getValue() :
                                                                  null),
                                                          (!Objects.equals(releaseYearComboBox.getValue(), "") ?
                                                                  Integer.parseInt(releaseYearComboBox.getValue()) :
                                                                  null),
                                                          (!Objects.equals(ratingComboBox.getValue(), "") ?
                                                                  Float.parseFloat(ratingComboBox.getValue()) :
                                                                  null)));
            }
            observableMovies.setAll(sortMovies(observableMovies,
                                               sortBtn.getText()
                                                      .equals("Sort (desc)")));
        });

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText()
                       .equals("Sort (asc)")) {
                sortBtn.setText("Sort (desc)");
                observableMovies.setAll(sortMovies(observableMovies, true));
            } else {
                sortBtn.setText("Sort (asc)");
                observableMovies.setAll(sortMovies(observableMovies, false));
            }
        });

        sceneBtn.setOnAction(actionEvent -> {
            setScene();
        });

    }

    private void setScene() {
        homeScene = !homeScene;
        if (homeScene) {
            observableMovies.setAll(allMovies);
            // sort list as it instances unsorted
            observableMovies.setAll(sortMovies(observableMovies,
                                               sortBtn.getText()
                                                      .equals("Sort (desc)")));
        } else {
            updateWatchListView();
        }
    }

    private void updateWatchListView() {
        try {
            List<String> a = wRepo.getAllWatchMovies()
                                  .stream()
                                  .map(m -> m.getImdbId())
                                  .toList();
            List<MovieEntity> me = mRepo.getMovies(a);
            List<Movie> m = MovieEntity.toMovies(me);
            observableMovies.setAll(m);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) ->
    {
        try {
            if (homeScene) {
                wRepo.addToWatchList(((Movie) clickedItem).getId());
            } else {
                wRepo.removeWatchList(((Movie) clickedItem).getId());
                updateWatchListView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    List<Movie> sortMovies(List<Movie> listToSort, boolean reverseOrder) {
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

    List<Movie> filterMovies(List<Movie> listToFilter,
                             String text,
                             Genres genre,
                             String releaseYear,
                             String rating) throws IOException {
        return MovieAPI.getMovies(text, genre, releaseYear, rating);
    }

    // local filtering
    List<Movie> filterLocalMovies(List<Movie> listToFilter, String text, Genres genre, int releaseYear, float rating) {
        return listToFilter.stream()
                           .filter(movie -> (movie.getTitle()
                                                  .toLowerCase()
                                                  .contains(text.toLowerCase())
                                             || movie.getDescription()
                                                     .toLowerCase()
                                                     .contains(text.toLowerCase()))
                                            && (genre == null
                                                ||
                                                Objects.equals(String.valueOf(genre), "")
                                                ||
                                                movie.getGenres()
                                                     .contains(genre))
                                            && (releaseYear == -1
                                                || releaseYear == movie.getReleaseYear())
                                            && (rating == -1f
                                                || rating <= movie.getReleaseYear()))
                           .toList();
    }

    String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> mainCastCount = movies.stream()
                                                .flatMap(movie -> movie.getMainCast()
                                                                       .stream())
                                                .collect(Collectors.groupingBy(
                                                        castMember -> castMember,
                                                        Collectors.counting()
                                                ));
        Map.Entry<String, Long> mostCommonCastMember = mainCastCount.entrySet()
                                                                    .stream()
                                                                    .max(Map.Entry.comparingByValue())
                                                                    .orElse(null);
        return mostCommonCastMember.getKey();
    }

    int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                     .map(movie -> movie.getTitle()
                                        .length())
                     .max(Integer::compare)
                     .orElse(0);
    }

    long countMoviesFrom(List<Movie> movies, String director) {
        return (int) movies.stream()
                           .filter(movie -> movie.getDirectors()
                                                 .contains(director))
                           .count();
    }

    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                     .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                     .collect(Collectors.toList());
    }
}