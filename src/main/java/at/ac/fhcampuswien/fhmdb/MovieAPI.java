package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.models.Genres;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import okhttp3.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MovieAPI {
    private static final OkHttpClient client = new OkHttpClient();
    static ObjectMapper mapper = new ObjectMapper();

    public static List<Movie> getMovies(String userInput,
                                        Genres genre,
                                        String releaseYear,
                                        String ratingFrom) throws MovieAPIException {
        String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
        Request req = new MovieAPIRequestBuilder(baseUrl).query(userInput)
                                                         .genre(genre)
                                                         .releaseYear(releaseYear)
                                                         .ratingFrom(ratingFrom)
                                                         .build();
        Movie[] movies;
        try (Response response = client.newCall(req)
                                       .execute()) {
            // jackson map response json string to array of Movies
            movies = mapper.readValue(response.body()
                                              .string(), Movie[].class);
        } catch (IOException e) {
            throw new MovieAPIException("MovieAPI failed");
        }

        return List.of(movies);
    }

    public static Movie getMovieById(String id) throws MovieAPIException {
        Request request = new MovieAPIRequestBuilder("https://prog2.fh-campuswien.ac.at/movies/" + id).build();

        Movie movie;
        try (Response response = client.newCall(request)
                                       .execute()) {
            // jackson map response json string to array of Movies
            movie = mapper.readValue(response.body()
                                             .string(), Movie.class);
        } catch (IOException e) {
            throw new MovieAPIException("");
        }
        return movie;
    }
}
