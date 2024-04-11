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
    List<Movie> streamTestMovieList;
    HomeController homeController;

    @BeforeEach
    void init_movieList_for_testing() {
        homeController = new HomeController();
        movieList = Movie.initializeMovies();
    }

    @Test
    void emptySearch_returns_all_movies() {
        // given
        String searchText = "";
        Genres genre = null;
        String releaseYear = null;
        String rating = null;

        // when
        List<Movie> actualMovieList = homeController.filterMovies(movieList, searchText, genre, releaseYear, rating);

        // then
        assertIterableEquals(actualMovieList, movieList);
    }

    @Test
    void filterGenre_returns_only_movies_with_genre() {
        // given
        List<Movie> actualMovieList;
        String searchText = "";
        Genres genre = Genres.ADVENTURE;
        String releaseYear = null;
        String rating = null;

        // when
        actualMovieList = homeController.filterMovies(movieList, searchText, genre, releaseYear, rating);

        // then
        List<Movie> expectedMovieList = movieList.stream()
                                                 .filter(m -> m.getGenres()
                                                               .contains(genre))
                                                 .toList();
        assertIterableEquals(expectedMovieList, actualMovieList);
    }

    /*
        @Test
        void filterText_returns_movies_with_matching_text_in_title_and_description() {
            // given
            List<Movie> actualMovieList;
            String searchText = "un";
            Genres genre = null;
            String releaseYear = null;
            String rating = null;

            // when
            actualMovieList = homeController.filterMovies(movieList, searchText, genre, releaseYear, rating);

            // then
            List<Movie> expectedMovieList = movieList.stream()
                                                     .filter(m -> m.getTitle()
                                                                   .contains(searchText) ||
                                                                  m.getDescription()
                                                                   .contains(searchText))
                                                     .toList();
            assertIterableEquals(expectedMovieList, actualMovieList);
        }
    */
    @Test
    void upper_and_lower_case_irrelevant() {
        //given
        List<Movie> movieListUp = movieList;
        List<Movie> movieListLow = movieList;
        String searchTextUp = "Da";
        String searchTextLow = "da";
        Genres genre = null;
        String releaseYear = null;
        String rating = null;

        //when
        movieListUp = homeController.filterMovies(movieListUp, searchTextUp, genre, releaseYear, rating);
        movieListLow = homeController.filterMovies(movieListLow, searchTextLow, genre, releaseYear, rating);

        //then
        assertEquals(movieListLow, movieListUp);
    }

    @Test
    void ascending_and_descending_filters_work() {
        //given
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
        expectedDesc.sort(Comparator.comparing(Movie::getTitle)
                                    .reversed());

        assertAll(
                () -> assertEquals(expectedAsc, movieListAsc, "The list should be sorted in ascending order"),
                () -> assertEquals(expectedDesc, movieListDesc, "The list should be sorted in descending order")
        );
    }

    /*
        @Test
        void filter_movies_by_text_and_genre() {
            List<Movie> allMovies = List.of(
                    new Movie("Action Movie One", "Description of the first action movie", List.of(Genres.ACTION),
                              2000,
                              "SO good, wow, insane",
                              "test00",
                              2,
                              List.of("insanelyGoodDirector"),
                              List.of("goodWriter"),
                              List.of("superGoodActor"),
                              10),
                    new Movie("Comedy Movie One", "This is a funny movie", List.of(Genres.COMEDY),
                              2000,
                              "SO good, wow, insane",
                              "test00",
                              2,
                              List.of("insanelyGoodDirector"),
                              List.of("goodWriter"),
                              List.of("superGoodActor"),
                              10),
                    new Movie("Action Comedy Movie",
                              "An action movie with a twist of comedy",
                              List.of(Genres.ACTION, Genres.COMEDY),
                              2000,
                              "SO good, wow, insane",
                              "test00",
                              2,
                              List.of("insanelyGoodDirector"),
                              List.of("goodWriter"),
                              List.of("superGoodActor"),
                              10),
                    new Movie("Drama Movie One", "A very dramatic movie", List.of(Genres.DRAMA),
                              2000,
                              "SO good, wow, insane",
                              "test00",
                              2,
                              List.of("insanelyGoodDirector"),
                              List.of("goodWriter"),
                              List.of("superGoodActor"),
                              10));

            String searchText = "action";
            Genres selectedGenre = Genres.COMEDY;
            String releaseYear = null;
            String rating = null;

            List<Movie> filteredMovies = controller.filterMovies(allMovies, searchText, selectedGenre, releaseYear, rating);

            List<Movie> expectedMovies = List.of(allMovies.get(2));

            assertEquals(expectedMovies,
                         filteredMovies,
                         "The filtered list should only contain movies that match both the search text and the selected genre.");
        }
    */
    @Test
    void no_movies_shown_when_filters_do_not_match() {
        List<Movie> allMovies = Movie.initializeMovies();

        String searchText = "Nonexistent Movie";
        Genres searchGenre = Genres.SCIENCE_FICTION;
        String releaseYear = null;
        String rating = null;

        List<Movie> filteredMovies = homeController.filterMovies(allMovies,
                                                                 searchText,
                                                                 searchGenre,
                                                                 releaseYear,
                                                                 rating);

        assertTrue(filteredMovies.isEmpty(),
                   "No movies should be shown, when neither of the search variables fit any movies or descriptions.");
    }


    @Test
    void are_filters_removable() {
        String searchText = null;
        Genres removeFilters = null;
        String releaseYear = null;
        String rating = null;

        List<Movie> filteredList = homeController.filterMovies(movieList,
                                                               searchText,
                                                               removeFilters,
                                                               releaseYear,
                                                               rating); // Attempt to remove filters

        // Comparing the lists
        assertEquals(movieList.size(),
                     filteredList.size(),
                     "The lists should have the same size after removing the filters.");
        assertTrue(movieList.containsAll(filteredList) && filteredList.containsAll(movieList),
                   "The movie lists should be equal after removing the filters.");
    }

    @Test
    void doesMostPopularActorWork() {
        List<Movie> testMovies = List.of(
                new Movie("fakeId1",
                          "Insanely good horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane",
                          "test00",
                          2,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter"),
                          List.of("superGoodActor"),
                          10),
                new Movie("fakeId2",
                          "Insanely good horror movie2 ",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane, truly incredible",
                          "test01",
                          3,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter and mediumWriters"),
                          List.of("superGoodActor", "mediumActor"),
                          10)
        );

        String filteredActor = homeController.getMostPopularActor(testMovies);
        assertEquals("superGoodActor", filteredActor);
    }

    @Test
    void doesItReturnLongestMovieTitle() {
        String longestTitleTest = "Insanely good and very long horror movie";

        List<Movie> testMovies = List.of(
                new Movie("fakeId1",
                          "Insanely good horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane",
                          "test00",
                          2,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter"),
                          List.of("superGoodActor"),
                          10),
                new Movie("fakeId2",
                          "Insanely good and very long horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane, truly incredible",
                          "test01",
                          3,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter and mediumWriters"),
                          List.of("superGoodActor", "mediumActor"),
                          10)
        );
        int longestTitle = homeController.getLongestMovieTitle(testMovies);
        int longestTitleTestCount = longestTitleTest.length();

        assertEquals(longestTitleTestCount, longestTitle);
    }

    @Test
    void testMoviesFromDirector() {
        long numberOfMoviesTest = 2;

        List<Movie> testMovies = List.of(
                new Movie("fakeId1",
                          "Insanely good horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane",
                          "test00",
                          2,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter"),
                          List.of("superGoodActor"),
                          10),
                new Movie("fakeId2",
                          "Insanely good and very long horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane, truly incredible",
                          "test02",
                          3,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter and mediumWriters"),
                          List.of("superGoodActor", "mediumActor"),
                          10),
                new Movie("fakeId3",
                          "Insanely good and very long horror movie 2 ",
                          List.of(Genres.HORROR),
                          2002,
                          "SO good, wow, insane, truly incredible, i cant believe it ",
                          "test03",
                          3,
                          List.of("mediumDirector"),
                          List.of("mediumWriters"),
                          List.of("mediumActor"),
                          3)
        );
        long numberOfMovies = homeController.countMoviesFrom(testMovies, "insanelyGoodDirector");
        assertEquals(numberOfMoviesTest, numberOfMovies);
    }

    @Test
    void testMoviesBetween() {
        List<Movie> testMovies = List.of(
                new Movie("fakeId1",
                          "Insanely good horror movie",
                          List.of(Genres.HORROR),
                          1990,
                          "SO good, wow, insane",
                          "test00",
                          2,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter"),
                          List.of("superGoodActor"),
                          10),
                new Movie("fakeId2",
                          "Insanely good and very long horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane, truly incredible",
                          "test02",
                          3,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter and mediumWriters"),
                          List.of("superGoodActor", "mediumActor"),
                          10),
                new Movie("fakeId3",
                          "Insanely good and very long horror movie 2 ",
                          List.of(Genres.HORROR),
                          2002,
                          "SO good, wow, insane, truly incredible, i cant believe it ",
                          "test03",
                          3,
                          List.of("mediumDirector"),
                          List.of("mediumWriters"),
                          List.of("mediumActor"),
                          3)
        );

        List<Movie> filteredList = homeController.getMoviesBetweenYears(testMovies, 1990, 2000);
        List<Movie> expectedList = List.of(testMovies.get(0), testMovies.get(1));

        assertEquals(expectedList, filteredList);

    }

    @Test
    void testMoviesWrongBetween() {
        List<Movie> testMovies = List.of(
                new Movie("fakeId1",
                          "Insanely good horror movie",
                          List.of(Genres.HORROR),
                          1990,
                          "SO good, wow, insane",
                          "test00",
                          2,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter"),
                          List.of("superGoodActor"),
                          10),
                new Movie("fakeId2",
                          "Insanely good and very long horror movie",
                          List.of(Genres.HORROR),
                          2000,
                          "SO good, wow, insane, truly incredible",
                          "test02",
                          3,
                          List.of("insanelyGoodDirector"),
                          List.of("goodWriter and mediumWriters"),
                          List.of("superGoodActor", "mediumActor"),
                          10),
                new Movie("fakeId3",
                          "Insanely good and very long horror movie 2 ",
                          List.of(Genres.HORROR),
                          2002,
                          "SO good, wow, insane, truly incredible, i cant believe it ",
                          "test03",
                          3,
                          List.of("mediumDirector"),
                          List.of("mediumWriters"),
                          List.of("mediumActor"),
                          3)
        );

        List<Movie> filteredList = homeController.getMoviesBetweenYears(testMovies, 2000, 1990);
        List<Movie> expectedList = List.of();

        assertEquals(expectedList, filteredList);

    }
}