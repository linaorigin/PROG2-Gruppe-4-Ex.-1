package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final String ratingStar = new String(new byte[]{(byte) 0xE2, (byte) 0xAD, (byte) 0x90},
                                                 StandardCharsets.UTF_8);
    private final HBox stats = new HBox(rating, releaseYear);
    private final VBox layout = new VBox(title, stats, detail);

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
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty()
                 .set(title.getFont()
                           .font(20));
            detail.setMaxWidth(this.getScene()
                                   .getWidth() - 30);
            detail.setWrapText(true);
            stats.setSpacing(10);
            layout.setPadding(new Insets(10));
            layout.spacingProperty()
                  .set(10);
            layout.alignmentProperty()
                  .set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

