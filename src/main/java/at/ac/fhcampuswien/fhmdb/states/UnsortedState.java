package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class UnsortedState implements State {
    private final List<Movie> ogOrder;

    public UnsortedState(List<Movie> ogOrder) {
        this.ogOrder = new ArrayList<>(ogOrder);
    }

    @Override
    public List<Movie> sortMovies(List<Movie> movies) {
        return new ArrayList<>(ogOrder);
    }
}
