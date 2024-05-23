module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;

    requires okhttp3;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires ormlite.jdbc;
    requires java.sql;
    requires com.h2database;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    opens at.ac.fhcampuswien.fhmdb.models to com.fasterxml.jackson.databind;
    exports at.ac.fhcampuswien.fhmdb.models;
    opens at.ac.fhcampuswien.fhmdb.data to ormlite.jdbc;
    exports at.ac.fhcampuswien.fhmdb.exceptions;
}