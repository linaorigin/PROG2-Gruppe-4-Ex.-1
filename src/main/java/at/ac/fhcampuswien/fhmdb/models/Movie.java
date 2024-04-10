package at.ac.fhcampuswien.fhmdb.models;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    private final String id;
    private final String title;
    private final List<Genres> genres;
    private final int releaseYear;
    private final String description;
    private final String imgUrl;
    private final int lengthInMinutes;
    private final String[] directors;
    private final String[] writers;
    private final String[] mainCast;
    private final int rating;

    @JsonCreator
    public Movie(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("genres") List<Genres> genres,
                 @JsonProperty("releaseYear") int releaseYear,
                 @JsonProperty("description") String description,
                 @JsonProperty("imgUrl") String imgUrl,
                 @JsonProperty("lengthInMinutes") int lengthInMinutes,
                 @JsonProperty("directors") String[] directors,
                 @JsonProperty("writers") String[] writers,
                 @JsonProperty("mainCast") String[] mainCast,
                 @JsonProperty("rating") int rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }
/*
    public Movie(String title, String description, List<Genres> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }
*/
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
        /*
        movies.add(new Movie("Damsel", "Eine pflichtbewusste Jungfrau willigt ein, einen gut aussehenden Prinzen zu heiraten, und muss feststellen, dass die königliche Familie sie als Opfer rekrutiert hat, um eine alte Schuld zu begleichen. 2024, 1h48m, PG-13", List.of(Genres.ACTION, Genres.FANTASY, Genres.ADVENTURE)));
        movies.add(new Movie("The Creator", "Ein postapokalyptischer Thriller über eine Zukunft, die von einem Krieg zwischen Menschen und KI geprägt ist. 2023, 2h 13m, PG-12", List.of(Genres.ACTION, Genres.THRILLER)));
        movies.add(new Movie("Deadpool", "Ein früherer Special-Forces-Soldat, der zu einem Söldner geworden ist, wird einem verbotenen Experiment unterworfen, nach dem er die Kraft der beschleunigten Heilung erhält und fortan als Deadpool auftritt. 2016, 1h 48m, PG-16", List.of(Genres.ACTION, Genres.COMEDY, Genres.FANTASY)));
        movies.add(new Movie("Spider-Man: Across the Spider-Verse", "Miles Morales kehrt im 2. Teil der Spider-Verse-Saga zurück, reist durch das Multiversum und trifft auf ein Team von Spider-People. Uneinig über den Umgang mit einer neuen Bedrohung definiert Miles neu, was es bedeutet, ein Held zu sein. 2023, 2h20m, PG-12", List.of(Genres.ANIMATION, Genres.ACTION, Genres.ADVENTURE)));
        movies.add(new Movie("Alles steht Kopf", "Die junge Riley wird plötzlich aus ihrem Leben im mittleren Westen gerissen, als Sie nach San Francisco umziehen muss. Ihre Emotionen - Freude, Angst, Wut, Abscheu und Traurigkeit - stehen dabei im Konflikt mit der Erkundung einer neuen Umgebung. 2015, 1h35m", List.of(Genres.ANIMATION, Genres.ADVENTURE, Genres.COMEDY)));
        movies.add(new Movie("The Iron Claw", "Die wahre Geschichte der unzertrennlichen Von Erich-Brüder, die in den frühen 1980er Jahren in der hart umkämpften Welt des professionellen Wrestlings Geschichte schrieben. 2023, 2h12m, PG-12", List.of(Genres.BIOGRAPHY, Genres.DRAMA, Genres.SPORT)));
        movies.add(new Movie("Nightmare Before Christmas","Jack Skellington, der König von Halloween Town, entdeckt Christmas Town, ohne jedoch genau deren Konzept zu verstehen. 1993, 1h16m, PG-6", List.of(Genres.ANIMATION, Genres.FAMILY, Genres.FANTASY)));
        movies.add(new Movie("Vielleicht lieber morgen","Ein introvertierter Neuling an der High School wird von zwei älteren Schülern unter ihre Fittiche genommen, die ihn in der wirklichen Welt willkommen heißen. 2012, 1h43m, PG-12", List.of(Genres.DRAMA)));
        movies.add(new Movie("Chihiros Reise ins Zauberland","Während ihre Familie in die Vorstadt zieht, durchstreift ein missmutiges 10-jähriges Mädchen eine Welt, die von Göttern, Hexen und Geistern regiert wird, und in der sich Menschen in Bestien verwandeln. 2001, 2h5m, PG-0",List.of(Genres.ANIMATION, Genres.FAMILY, Genres.ADVENTURE)));


        // TODO add some dummy data here
*/
        return movies;
    }
/*
    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        return Objects.equals(title, other.getTitle()) && Objects.equals(genres, other.getGenres());
    }

    @Override
    public int hashCode(){
        return Objects.hash(title,genres);
    }
*/
}
