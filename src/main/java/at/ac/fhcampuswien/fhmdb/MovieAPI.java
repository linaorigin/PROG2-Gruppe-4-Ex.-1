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

    public static List<Movie> getMovies(String userinput, Genres genre, String releaseYear, String ratingFrom) {
        HttpUrl url = HttpUrl.parse("https://prog2.fh-campuswien.ac.at/movies");

        String query = (userinput != null ? "query=" + userinput : "") +
                       (genre != null ? "genre=" + genre : "") +
                       (releaseYear != null ? "releaseYear=" + releaseYear : "") +
                       (ratingFrom != null ? "ratingFrom=" + ratingFrom : "");
        if (!query.isEmpty()) {
            url.newBuilder()
               .query(query)
               .build();
        }

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        Movie[] movies;
        try (Response response = client.newCall(request)
                                       .execute()) {
            if (!response.isSuccessful()) {
                System.out.println("req head: " + request.headers());
                System.out.println("req body: " + request.toString());
                System.out.println("res head: " + response.headers());
                System.out.println("res body: " + response.toString());
                throw new IOException("Unexpected code " + response);
            }
            movies = mapper.readValue(response.body()
                                              .string(), Movie[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            if (!response.isSuccessful()) {
                System.out.println("req head: " + request.headers());
                System.out.println("req body: " + request.toString());
                System.out.println("res head: " + response.headers());
                System.out.println("res body: " + response.toString());
                throw new IOException("Unexpected code " + response);
            }

            movie = mapper.readValue(response.body()
                                             .string(), Movie.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }
}
