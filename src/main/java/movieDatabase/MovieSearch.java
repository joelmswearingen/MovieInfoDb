package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages API calls. These calls a completed on separate threads allowing the GUI
 * to continue to be responsive as API call complete */

import kong.unirest.Unirest;

import javax.swing.*;

public class MovieSearch extends SwingWorker<OmdbResponse, Void> {

    private final String omdbURI;
    private String searchTerm;
    private String searchYear;

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

    // once API call is complete this block will run, consuming the data obtained by the call
    // and calling a method in the MovieGUI to do further processing
    @Override
    public void done() {
        try {
            OmdbResponse response = get();
            Main.movieGUI.updateMovie(response);
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

//    uncomment if you do not want movie search to be completed in the background.
//    Make additional edits in MovieController and MovieGUI
//    public MovieSearch(String openMovieDbURI) {
//
//        this.omdbURI = openMovieDbURI;
//
//    }

//    public OmdbResponse searchOpenMovieDatabase(String searchTerm, String searchYear) {
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
