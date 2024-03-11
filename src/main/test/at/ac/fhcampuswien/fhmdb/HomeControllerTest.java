package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
    @Test
    void upper_and_lower_case_irrelevant(){
        //given
        HomeController homeController = new HomeController();
        List<Movie> movieListUp = Movie.initializeMovies();
        List<Movie> movieListLow = Movie.initializeMovies();
        String searchTextUp = "Da";
        String searchTextLow = "da";
        Genres genre = null;

        //when
        movieListUp = homeController.filterMovies(movieListUp,searchTextUp,genre);
        movieListLow = homeController.filterMovies(movieListLow,searchTextLow,genre);

        //then
        assertEquals(movieListLow,movieListUp);
    }

    @Test
    void ascending_and_descending_filters_work(){
        //given
        HomeController homeController = new HomeController();
        List<Movie> movieListAsc = Movie.initializeMovies();
        List<Movie> movieListDesc = Movie.initializeMovies();
        boolean desc = true;
        boolean asc = false;

        //when
        homeController.sortMovies(movieListAsc,asc);
        homeController.sortMovies(movieListDesc,desc);

        //then
        List<Movie> expectedAsc = Movie.initializeMovies();

        expectedAsc.sort(Comparator.comparing(Movie::getTitle));

        assertEquals(expectedAsc, movieListAsc, "The list should be sorted in ascending order");

    }

    @Test
    public void test_filter_movies_by_text_and_genre() {
        HomeController controller = new HomeController();
        List<Movie> allMovies = List.of(
                new Movie("Action Movie One", "Description of the first action movie", List.of(Genres.ACTION)),
                new Movie("Comedy Movie One", "This is a funny movie", List.of(Genres.COMEDY)),
                new Movie("Action Comedy Movie", "An action movie with a twist of comedy", List.of(Genres.ACTION, Genres.COMEDY)),
                new Movie("Drama Movie One", "A very dramatic movie", List.of(Genres.DRAMA)));


        String searchText = "action";
        Genres selectedGenre = Genres.COMEDY;

        List<Movie> filteredMovies = controller.filterMovies(allMovies, searchText, selectedGenre);


        List<Movie> expectedMovies = List.of(
                new Movie("Action Comedy Movie", "An action movie with a twist of comedy", (List<Genres>) List.of(Genres.ACTION, Genres.COMEDY))
        );

        assertEquals(expectedMovies, filteredMovies, "The filtered list should only contain movies that match both the search text and the selected genre.");
    }
}