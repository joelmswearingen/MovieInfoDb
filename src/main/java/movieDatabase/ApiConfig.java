package movieDatabase;

import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

public class ApiConfig {
    // set OMDb api key as a system environment variable
    static String OMDB_API_KEY = System.getenv("OMDB_API_KEY");

    // set OMBd url to include personalized API key
    static String OMDB_URL = "http://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&";

    public ApiConfig() {

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




}
