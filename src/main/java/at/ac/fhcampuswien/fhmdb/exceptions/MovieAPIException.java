package at.ac.fhcampuswien.fhmdb.exceptions;

public class MovieAPIException extends Exception {
    public MovieAPIException(String errorMessage) {
        super(errorMessage);
    }
}
