package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;
import java.util.List;

public class DescendingState implements State {
    @Override
    public List<Movie> sortMovies(List<Movie> movies) {
        return movies.stream()
                     .sorted(Comparator.comparing(Movie::getTitle)
                                       .reversed())
                     .toList();
    }

    @Override
    public State changeState() {
        return new AscendingState();
    }


}
