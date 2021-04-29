package movieDatabase;


public class Main {

    static MovieGUI movieGUI;

    public static void main(String[] args) {
        String databaseURI = DbConfig.dbURI;
        String openMovieDbURI = ApiConfig.OMDB_URL;

        MovieStore movieStore = new MovieStore(databaseURI);
        MovieSearch movieSearch = new MovieSearch(openMovieDbURI);
        MovieController movieController = new MovieController(movieStore, movieSearch);
        movieGUI = new MovieGUI(movieController);




    }


}
