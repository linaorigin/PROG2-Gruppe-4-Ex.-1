package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genres;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class MovieAPIRequestBuilder {

    private final HttpUrl baseUrl;
    private String query = null;
    private String genre = null;
    private String releaseYear = null;
    private String ratingFrom = null;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = HttpUrl.parse(baseUrl);
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(Genres genre) {
        if (genre != null) {
            this.genre = String.valueOf(genre);
        }
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public Request build() {
        HttpUrl url = baseUrl;
        HttpUrl.Builder queryBuilder = url.newBuilder();
        // build query string
        if (query != null) {
            queryBuilder.addQueryParameter("query", query);
        }
        if (genre != null) {
            queryBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null) {
            queryBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (ratingFrom != null) {
            queryBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }
        url = queryBuilder.build();

        return new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();
    }
}
