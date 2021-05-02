package movieDatabase;

import java.sql.SQLException;
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

    protected boolean searchDbByMovieTitle(String omdbMovieTitle, String omdbMovieYear) {
        return movieStore.searchByTitle(omdbMovieTitle, omdbMovieYear);
    }


}
