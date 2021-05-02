package movieDatabase;

import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

public class MovieSearch {

    private final String omdbURI;

    public MovieSearch(String openMovieDbURI) {

        this.omdbURI = openMovieDbURI;

        // Configure Unirest to use Gson to convert JSON to Java object
        Unirest.config().setObjectMapper(new ObjectMapper() {
            private Gson gson = new Gson();
            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return gson.fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });

    }


    public OmdbResponse searchOpenMovieDatabase(String searchTerm) {

        return Unirest.get(omdbURI)
                .queryString("t", searchTerm)
                .queryString("type", "movie") // restrict results to movies only so that only single years are returned
                .queryString("plot", "full")
                .asObject(OmdbResponse.class).getBody();

    }
}
