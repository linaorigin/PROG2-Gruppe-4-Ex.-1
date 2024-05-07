package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.nio.charset.StandardCharsets;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final String ratingStar = new String(new byte[]{(byte) 0xE2, (byte) 0xAD, (byte) 0x90},
                                                 StandardCharsets.UTF_8);
    private Button showDetails = new Button("show Details");
    private Button addToWatchlist = new Button("Watchlist");

    private final HBox stats = new HBox(rating, releaseYear);
    private final HBox buttonWrap = new HBox(showDetails, addToWatchlist);
    private final Pane grow = new Pane();
    private final HBox t = new HBox(title, grow, buttonWrap);
    private final VBox layout = new VBox(t, stats, detail);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass()
                .add("movie-cell");
            title.setText(movie.getTitle());
            rating.setText(movie.getRating() + " " + ratingStar);
            releaseYear.setText("Released: " + movie.getReleaseYear());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );


            // color scheme
            title.setTextFill(Color.web("#ffff00"));
            detail.setTextFill(Color.web("#ffffff"));
            releaseYear.setTextFill(Color.web("#ffffff"));
            rating.setTextFill(Color.web("#ff8c00"));
            //rating.getStyleClass()
            //      .add("text-white");
            showDetails.getStyleClass()
                       .add("movie-btn");
            addToWatchlist.getStyleClass()
                          .add("movie-btn");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty()
                 .set(title.getFont()
                           .font(20));
            detail.setMaxWidth(this.getScene()
                                   .getWidth() - 30);
            detail.setWrapText(true);
            stats.setSpacing(10);
            HBox.setHgrow(grow, Priority.ALWAYS);
            grow.setMaxWidth(Double.MAX_VALUE);
            showDetails.setAlignment(Pos.BASELINE_RIGHT);
            addToWatchlist.setAlignment(Pos.BASELINE_RIGHT);
            buttonWrap.setAlignment(Pos.BASELINE_RIGHT);
            buttonWrap.setSpacing(5);
            layout.setPadding(new Insets(10));
            layout.spacingProperty()
                  .set(10);
            layout.alignmentProperty()
                  .set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

