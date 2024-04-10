package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    List<Movie> movieList;

    @BeforeEach
    void init_movieList_for_testing(){
        movieList = Movie.initializeMovies();
    }

    @Test
    void emptySearch_returns_all_movies() {
        // given
        HomeController homeController = new HomeController();
        String searchText = "";
        Genres genre = null;

        // when
        List<Movie> actualMovieList = homeController.filterMovies(movieList,searchText,genre);

        // then
        assertIterableEquals(actualMovieList,movieList);
    }

    @Test
    void filterGenre_returns_only_movies_with_genre() {
        // given
        HomeController homeController = new HomeController();
        List<Movie> actualMovieList;
        String searchText = "";
        Genres genre = Genres.ADVENTURE;

        // when
        actualMovieList = homeController.filterMovies(movieList, searchText, genre);

        // then
        List<Movie> expectedMovieList = movieList.stream()
                                                 .filter(m -> m.getGenres()
                                                               .contains(genre))
                                                 .toList();
        assertIterableEquals(expectedMovieList, actualMovieList);
    }

    @Test
    void filterText_returns_movies_with_matching_text_in_title_and_description() {
        // given
        HomeController homeController = new HomeController();
        List<Movie> actualMovieList;
        String searchText = "un";
        Genres genre = null;

        // when
        actualMovieList = homeController.filterMovies(movieList, searchText, genre);

        // then
        List<Movie> expectedMovieList = movieList.stream()
                                                 .filter(m -> m.getTitle()
                                                               .contains(searchText) ||
                                                              m.getDescription()
                                                               .contains(searchText))
                                                 .toList();
        assertIterableEquals(expectedMovieList, actualMovieList);
    }

    @Test
    void upper_and_lower_case_irrelevant() {
        //given
        HomeController homeController = new HomeController();
        List<Movie> movieListUp = movieList;
        List<Movie> movieListLow = movieList;
        String searchTextUp = "Da";
        String searchTextLow = "da";
        Genres genre = null;

        //when
        movieListUp = homeController.filterMovies(movieListUp, searchTextUp, genre);
        movieListLow = homeController.filterMovies(movieListLow, searchTextLow, genre);

        //then
        assertEquals(movieListLow, movieListUp);
    }

    @Test
    void ascending_and_descending_filters_work() {
        //given
        HomeController homeController = new HomeController();
        List<Movie> movieListAsc;
        List<Movie> movieListDesc;
        boolean desc = true;
        boolean asc = false;

        //when
        movieListAsc = homeController.sortMovies(movieList, asc);
        movieListDesc = homeController.sortMovies(movieList, desc);

        //then
        List<Movie> expectedAsc = new ArrayList<>(movieList);
        List<Movie> expectedDesc = new ArrayList<>(movieList);

        expectedAsc.sort(Comparator.comparing(Movie::getTitle));
        expectedDesc.sort(Comparator.comparing(Movie::getTitle).reversed());

        assertAll(
                () -> assertEquals(expectedAsc, movieListAsc, "The list should be sorted in ascending order"),
                () -> assertEquals(expectedDesc, movieListDesc, "The list should be sorted in descending order")
                );
    }
/*
    @Test
    void filter_movies_by_text_and_genre() {
        HomeController controller = new HomeController();

        List<Movie> allMovies = List.of(
                new Movie("Action Movie One", "Description of the first action movie", List.of(Genres.ACTION)),
                new Movie("Comedy Movie One", "This is a funny movie", List.of(Genres.COMEDY)),
                new Movie("Action Comedy Movie", "An action movie with a twist of comedy", List.of(Genres.ACTION, Genres.COMEDY)),
                new Movie("Drama Movie One", "A very dramatic movie", List.of(Genres.DRAMA)));

        String searchText = "action";
        Genres selectedGenre = Genres.COMEDY;

        List<Movie> filteredMovies = controller.filterMovies(allMovies, searchText, selectedGenre);

        List<Movie> expectedMovies = List.of(allMovies.get(2));

        assertEquals(expectedMovies, filteredMovies, "The filtered list should only contain movies that match both the search text and the selected genre.");
    }
*/
    @Test
    void no_movies_shown_when_filters_do_not_match() {
        HomeController controller = new HomeController();
        List<Movie> allMovies = Movie.initializeMovies();

        String searchText = "Nonexistent Movie";
        Genres searchGenre = Genres.SCIENCE_FICTION;

        List<Movie> filteredMovies = controller.filterMovies(allMovies, searchText, searchGenre);

        assertTrue(filteredMovies.isEmpty(), "No movies should be shown, when neither of the search variables fit any movies or descriptions.");
    }

    @Test
    void are_filters_removable() {
        HomeController controller = new HomeController();
        Genres removeFilters = Genres.REMOVE_FILTER;

        List<Movie> filteredList = controller.filterMovies(movieList, "", removeFilters); // Attempt to remove filters

        // Comparing the lists
        assertEquals(movieList.size(), filteredList.size(), "The lists should have the same size after removing the filters.");
        assertTrue(movieList.containsAll(filteredList) && filteredList.containsAll(movieList), "The movie lists should be equal after removing the filters.");
    }

}