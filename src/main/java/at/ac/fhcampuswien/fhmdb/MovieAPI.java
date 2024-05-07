package at.ac.fhcampuswien.fhmdb;

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
                                        String ratingFrom) throws IOException {
        HttpUrl url = HttpUrl.parse("https://prog2.fh-campuswien.ac.at/movies");

        HttpUrl.Builder queryBuilder = url.newBuilder();
        // build query string
        if (userInput != null) {
            queryBuilder.addQueryParameter("query", userInput);
        }
        if (genre != null) {
            queryBuilder.addQueryParameter("genre", String.valueOf(genre));
        }
        if (releaseYear != null) {
            queryBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (ratingFrom != null) {
            queryBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }
        url = queryBuilder.build();

        /*String query = (userInput != null ? queryBuilder.addQueryParameter("query", userInput) : "") +
                       (genre != null ? "genre=" + genre : "") +
                       (releaseYear != null ? "releaseYear=" + releaseYear : "") +
                       (ratingFrom != null ? "ratingFrom=" + ratingFrom : "");

        // attach query string to url if not empty
        if (!query.isEmpty()) {
            url = url.newBuilder()
                     .query(query)
                     .build();
        }
        */
        // build request
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        Movie[] movies;
        Response response = client.newCall(request)
                                  .execute();
        // dummy handling unsuccessful http response
        if (!response.isSuccessful()) {
            System.out.println("req head: " + request.headers());
            System.out.println("req body: " + request);
            System.out.println("res head: " + response.headers());
            System.out.println("res body: " + response);
            throw new IOException("Unexpected code " + response);
        }

        // jackson map response json string to array of Movies
        movies = mapper.readValue(response.body()
                                          .string(), Movie[].class);

        return List.of(movies);
    }

    public static Movie getMovieById(String id) {
        Request request = new Request.Builder()
                .url("https://prog2.fh-campuswien.ac.at/movies/" + id)
                .header("User-Agent", "http.agent")
                .build();

        Movie movie;
        try (Response response = client.newCall(request)
                                       .execute()) {
            // dummy handling unsuccessful http response
            if (!response.isSuccessful()) {
                System.out.println("req head: " + request.headers());
                System.out.println("req body: " + request);
                System.out.println("res head: " + response.headers());
                System.out.println("res body: " + response);
                throw new IOException("Unexpected code " + response);
            }

            // jackson map single json string to Movie
            movie = mapper.readValue(response.body()
                                             .string(), Movie.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }
}
