package movieDatabase;

public class MovieController {

    MovieStore movieStore;
    MovieSearch movieSearch;

    MovieController(MovieStore store, MovieSearch search) {
        movieStore = store;
        movieSearch = search;


    }
}
