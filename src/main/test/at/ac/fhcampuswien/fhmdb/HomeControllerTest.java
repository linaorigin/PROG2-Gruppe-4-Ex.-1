package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Test
    void emptySearch_returns_all_movies(){
        // given
        HomeController homeController = new HomeController();
        List<Movie> movieList = Movie.initializeMovies();
        String searchText = "";
        Genres genre = null;

        // when
        homeController.filterMovies(movieList,searchText,genre);

        // then
        List<Movie> expectedMovieList = Movie.initializeMovies();
        assertEquals(expectedMovieList,movieList);
    }

    @Test
    void filterGenre_returns_only_movies_with_genre(){
        // given
        HomeController homeController = new HomeController();
        List<Movie> movieList = Movie.initializeMovies();
        String searchText = "";
        Genres genre = Genres.ADVENTURE;

        // when
        movieList = homeController.filterMovies(movieList,searchText,genre);

        // then
        List<Movie> expectedMovieList = Movie.initializeMovies();
        expectedMovieList = expectedMovieList.stream().filter(m -> m.getGenres().contains(genre)).toList();
        assertEquals(expectedMovieList,movieList);
    }
}