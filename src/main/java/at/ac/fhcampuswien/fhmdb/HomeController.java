package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.Observer.Observer;
import at.ac.fhcampuswien.fhmdb.data.MovieEntity;
import at.ac.fhcampuswien.fhmdb.data.MovieRepository;
import at.ac.fhcampuswien.fhmdb.data.WatchListRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import at.ac.fhcampuswien.fhmdb.states.AscendingState;
import at.ac.fhcampuswien.fhmdb.states.DescendingState;
import at.ac.fhcampuswien.fhmdb.states.State;
import at.ac.fhcampuswien.fhmdb.states.UnsortedState;


import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class HomeController implements Initializable, Observer {


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

    public List<Movie> allMovies;

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private MovieRepository mRepo;
    private WatchListRepository wRepo;
    private State state;
    private final State ascendingSortState = new AscendingState();
    private final State descendingSortState = new DescendingState();
    private State unsortedSortState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            mRepo = MovieRepository.getInstance();
            wRepo = WatchListRepository.getInstance();
            wRepo.addObserver(this);
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        try {
            allMovies = Movie.initializeMovies();
        } catch (MovieAPIException e) {
            try {
                allMovies = MovieEntity.toMovies(mRepo.getAllMovies());
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }


        observableMovies.addAll(allMovies);
        // sort list as it instances unsorted
        unsortedSortState = new UnsortedState(allMovies);
        state = unsortedSortState;
        sortBtn.setText("Unsorted");


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
            if (state instanceof UnsortedState) {
                state = ascendingSortState;
                sortBtn.setText("Sort (asc)");
            }
            observableMovies.setAll(state.sortMovies(observableMovies));
        });

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (!(state instanceof AscendingState) /* UnsortedState || (!homeScene && state instanceof DescendingState) */) {
                state = ascendingSortState;
                sortBtn.setText("Sort (asc)");
            } else /* if (state instanceof AscendingState)*/ {
                state = descendingSortState;
                sortBtn.setText("Sort (desc)");
            }/* else if (homeScene) {
                state = unsortedSortState;
                sortBtn.setText("Unsorted");
            }*/
            observableMovies.setAll(state.sortMovies(observableMovies));
        });

        sceneBtn.setOnAction(actionEvent -> setScene());

    }

    private void setScene() {
        homeScene = !homeScene;
        if (homeScene) {
            observableMovies.setAll(state.sortMovies(allMovies));
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
            if (state instanceof UnsortedState) {
                state = ascendingSortState;
                sortBtn.setText("Sort (asc)");
            }
            observableMovies.setAll(state.sortMovies(m));
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
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
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    };

    /*
        void sortMovies(List<Movie> listToSort, boolean changeState) {
            if (state instanceof UnsortedState || (!homeScene && state instanceof DescendingState)) {
                state = ascendingSortState;
                sortBtn.setText("Sort (asc)");
            } else if (state instanceof AscendingState) {
                state = descendingSortState;
                sortBtn.setText("Sort (desc)");
            } else if (homeScene) {
                state = unsortedSortState;
                sortBtn.setText("Unsorted");
            }
            observableMovies.setAll(state.sortMovies(listToSort));
        }
    */
    List<Movie> filterMovies(List<Movie> listToFilter,
                             String text,
                             Genres genre,
                             String releaseYear,
                             String rating) {
        try {
            return MovieAPI.getMovies(text, genre, releaseYear, rating);
        } catch (MovieAPIException ex) {
            return filterLocalMovies(listToFilter,
                                     (text != null ? text : ""),
                                     genre,
                                     Integer.parseInt((releaseYear != null ? releaseYear : String.valueOf(-1))),
                                     Float.parseFloat((rating != null ? rating : String.valueOf(-1f))));
        }
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
                                                || rating <= movie.getRating()))
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

    @Override
    public void update(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}