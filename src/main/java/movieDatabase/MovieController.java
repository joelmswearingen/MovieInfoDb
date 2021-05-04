package movieDatabase;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class MovieController {

    MovieStore movieStore;
    MovieSearch movieSearch;

    MovieController(MovieStore store, MovieSearch search) {
        movieStore = store;
        movieSearch = search;
    }


    protected OmdbResponse openMovieDatabaseQuery(String searchTerm, String searchYear) {
        return movieSearch.searchOpenMovieDatabase(searchTerm, searchYear);
    }


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
