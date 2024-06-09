package states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;
import java.util.List;

public class AscendingState implements State {
    @Override
    public List<Movie> sortMovies(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .toList();
    }
}
