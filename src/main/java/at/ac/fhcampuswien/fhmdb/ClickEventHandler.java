package at.ac.fhcampuswien.fhmdb;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}
