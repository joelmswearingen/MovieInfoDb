package movieDatabase;

import java.lang.reflect.Type;
import java.sql.*;

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
                    "userRating REAL CHECK (userRating >= 1 AND userRating <= 5)," +
                    "dateAdded INTEGER, " +
                    "dateUpdated INTEGER)";

            createMovieTable.execute(createMovieTableSQL);

        } catch (SQLException sqle) {
            System.err.println("Error creating 'movie' table. Error: " + sqle);
        }
    }


    public void addMovie(Movie movieToAdd) throws SQLException {

        // SQL Statement to INSERT record into 'movie' table
        String addMovie = "INSERT INTO movie (movieTitle, year, moviePlot, metascore, userRating, dateAdded, dateUpdated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // try-with-resources to apply movieToAdd object fields to expectant parameters in addMovie SQL statement
        try (Connection connection = DriverManager.getConnection(dbURI);
             PreparedStatement preparedStatement = connection.prepareStatement(addMovie)) {

            // set parameter 1
            preparedStatement.setString(1, movieToAdd.getTitle());

            // set parameter 2, entering null if year field is 0
            if (movieToAdd.getYear() != 0) {
                preparedStatement.setInt(2, movieToAdd.getYear());
            } else {
                preparedStatement.setNull(2, Types.INTEGER);
            }

            // set parameter 3
            preparedStatement.setString(3, movieToAdd.getPlot());

            // set parameter 4, entering null if metascore field is 0
            if (movieToAdd.getMetascore() != 0) {
                preparedStatement.setInt(4, movieToAdd.getMetascore());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }

            // set parameter 5, entering null if userRating field is 0
            if (movieToAdd.getUserRating() != 0) {
                preparedStatement.setDouble(5, movieToAdd.getUserRating());
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }

            // set parameters 6 and 7
            preparedStatement.setLong(6, movieToAdd.getDateAdded().getTime());
            preparedStatement.setLong(7, movieToAdd.getDateUpdated().getTime());

            // execute INSERT statement
            preparedStatement.executeUpdate();

            // after INSERT has been executed, retrieve generated key from db and add to object
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);
            movieToAdd.setId(id);


        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
            throw sqle;
        }

    }

}
