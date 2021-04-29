package movieDatabase;

import java.util.List;

public class MovieController {

    MovieStore movieStore;
    MovieSearch movieSearch;

    MovieController(MovieStore store, MovieSearch search) {
        movieStore = store;
        movieSearch = search;


    }

    protected OmdbResponse openMovieDatabaseQuery(String searchTerm) {
        return movieSearch.searchOpenMovieDatabase(searchTerm);
    }
}
