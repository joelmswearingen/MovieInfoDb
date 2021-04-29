package movieDatabase;

public class ApiConfig {
    // set OMDb api key as a system environment variable
    static String OMDB_API_KEY = System.getenv("OMDB_API_KEY");

    // set OMBd url to include personalized API key
    static String OMDB_URL = "http://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&";

}
