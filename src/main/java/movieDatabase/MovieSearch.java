package movieDatabase;

import kong.unirest.Unirest;

public class MovieSearch {

    private final String omdbURI;

    public MovieSearch(String openMovieDbURI) {

        this.omdbURI = openMovieDbURI;

        // OmdbResponse response = Unirest.get(openMovieDbURI).queryString("t", )

    }
}
