package at.ac.fhcampuswien.fhmdb.Observer;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObs(String message);
}
