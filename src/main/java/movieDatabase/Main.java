package movieDatabase;


import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

public class Main {

    static MovieGUI movieGUI;
    static RateMovieGUI rateMovieGUI;
    static MovieListGUI movieListGUI;

    public static void main(String[] args) {

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

        String databaseURI = DbConfig.dbURI;
        // String openMovieDbURI = ApiConfig.OMDB_URL;

        MovieStore movieStore = new MovieStore(databaseURI);
        // MovieSearch movieSearch = new MovieSearch(openMovieDbURI);
        MovieController movieController = new MovieController(movieStore);
        // MovieController movieController = new MovieController(movieStore, movieSearch);
        movieGUI = new MovieGUI(movieController);

    }

    public static void quit() {
        movieGUI.dispose();
    }




}
