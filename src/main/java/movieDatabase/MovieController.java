package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages the connection between the GUI and the code that talks to the database.
 * In general, it either passes objects or boolean values to let the GUI know processing has been successful or not. */

import java.sql.SQLException;
import java.util.List;

public class MovieController {

    MovieStore movieStore;
    // MovieSearch movieSearch;

    MovieController(MovieStore store) {
        movieStore = store;
    }

//    MovieController(MovieStore store, MovieSearch search) {
//        movieStore = store;
//        movieSearch = search;
//    }


//    protected OmdbResponse openMovieDatabaseQuery(String searchTerm, String searchYear) {
//        return movieSearch.searchOpenMovieDatabase(searchTerm, searchYear);
//    }


    protected boolean addMovieToDatabase(Movie movieToAdd) {
        try {
            movieStore.addMovie(movieToAdd);
            return true;
        } catch (SQLException sqle) {
            return false;
        }
    }

    protected boolean updateMovieInDatabase(Movie movieToUpdate) {
        try {
            movieStore.updateMovieUserRating(movieToUpdate);
            return true;
        } catch (SQLException sqle) {
            return false;
        }
    }

    protected boolean searchDbByMovieTitle(String omdbMovieTitle, String omdbMovieYear) {
        return movieStore.searchByTitle(omdbMovieTitle, omdbMovieYear);
    }

    protected List<Movie> getAllMoviesFromDatabase() {
        return movieStore.getAllMovies();
    }

    protected boolean deleteMovieFromDatabase(int movieId) {
        try {
            movieStore.deleteByMovieId(movieId);
            return true;
        } catch (SQLException sqle) {
            return false;
        }

    }

    protected double getAverageMovieRating() {
        return movieStore.getAverageRating();
    }

    protected void quitProgram() {
        Main.quit();
    }


}
