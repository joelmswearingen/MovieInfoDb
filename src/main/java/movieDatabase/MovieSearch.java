package movieDatabase;

import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

import javax.swing.*;

public class MovieSearch extends SwingWorker<OmdbResponse, Void> {

    private final String omdbURI;
    private String searchTerm;
    private String searchYear;

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


    // this gets passed via the controller and is expecting an OmdbResponse object back.
    // not sure how to get the response from done()
    public OmdbResponse searchOpenMovieDatabase(String searchTerm, String searchYear) {
        this.searchTerm = searchTerm;
        this.searchYear = searchYear;

        execute();

        return response;

    }

    @Override
    public OmdbResponse doInBackground() throws Exception {

        OmdbResponse response =  Unirest.get(omdbURI)
                .queryString("t", searchTerm)
                .queryString("y", searchYear)
                .queryString("type", "movie") // restrict results to movies only so that only single years are returned
                .queryString("plot", "full")
                .asObject(OmdbResponse.class)
                .getBody();

        Unirest.shutDown();

        return response; // this is what i need to somehow get back to the searchOpenMovieDatabase() to return to the MovieGUI
        // my understanding of SwingWorker is that once doInBackground() is done, it automatically moves to the done()
        // and within done you can take next steps. using get() to get the results from the doInBackground()

    }

    @Override
    public void done() {
        try {
            OmdbResponse response = get();
            // get this back to searchOpenMovieDatabase() ???


        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }


//    public OmdbResponse searchOpenMovieDatabase(String searchTerm, String searchYear) {
//
//        return Unirest.get(omdbURI)
//                .queryString("t", searchTerm)
//                .queryString("y", searchYear)
//                .queryString("type", "movie") // restrict results to movies only so that only single years are returned
//                .queryString("plot", "full")
//                .asObject(OmdbResponse.class)
//                .getBody();
//
//    }




}
