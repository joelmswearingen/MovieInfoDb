package movieDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieStore {

    // declare final variable to use for dbURI
    private final String dbURI;

    // method accepts database URI passed from Main method
    MovieStore(String databaseURI) {
        // set variable
        this.dbURI = databaseURI;

        // create movie table, if it does not already exists
        try (Connection connection = DriverManager.getConnection(dbURI);
             Statement createMovieTable = connection.createStatement()) {

            String createMovieTableSQL = "CREATE TABLE IF NOT EXISTS movie " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "movieTitle TEXT, " +
                    "year INTEGER, " +
                    "moviePlot TEXT, " +
                    "metascore INTEGER, " +
                    "userRating REAL CHECK (userRating >= 0 AND userRating <= 5) )";

            createMovieTable.execute(createMovieTableSQL);

        } catch (SQLException sqle) {
            System.err.println("Error creating 'movie' table. Error: " + sqle);
        }


    }
}
