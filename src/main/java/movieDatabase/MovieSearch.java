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

    }

    public MovieSearch(String searchTerm, String searchYear) {
        this.searchTerm = searchTerm;
        this.searchYear = searchYear;
        this.omdbURI = ApiConfig.OMDB_URL;

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

        return response;

    }

    @Override
    public void done() {
        try {
            OmdbResponse response = get();
            Main.movieGUI.updateMovie(response);
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
