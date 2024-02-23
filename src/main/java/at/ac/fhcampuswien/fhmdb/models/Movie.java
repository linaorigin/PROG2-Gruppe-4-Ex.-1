package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genres> genres;

    public Movie(String title, String description, List<Genres> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Damsel", "Eine pflichtbewusste Jungfrau willigt ein, einen gut aussehenden Prinzen zu heiraten, und muss feststellen, dass die königliche Familie sie als Opfer rekrutiert hat, um eine alte Schuld zu begleichen. 2024, 1h48m, PG-13", List.of(Genres.ACTION, Genres.FANTASY, Genres.ADVENTURE)));
        movies.add(new Movie("The Creator", "Ein postapokalyptischer Thriller über eine Zukunft, die von einem Krieg zwischen Menschen und KI geprägt ist. 2023, 2h 13m, PG-12", List.of(Genres.ACTION, Genres.THRILLER)));
        movies.add(new Movie("Deadpool", "Ein früherer Special-Forces-Soldat, der zu einem Söldner geworden ist, wird einem verbotenen Experiment unterworfen, nach dem er die Kraft der beschleunigten Heilung erhält und fortan als Deadpool auftritt. 2016, 1h 48m, PG-16", List.of(Genres.ACTION, Genres.COMEDY, Genres.FANTASY)));
        // TODO add some dummy data here

        return movies;
    }
}
