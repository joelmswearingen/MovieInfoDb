package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages interactions with the database.
 * This file consumes information passed to it from the MovieController */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

// Create a movie table if it does not already exist
public class MovieStore {

    // declare final variable to use for dbURI
    private final String dbURI;

    // method accepts database URI passed from Main method
    MovieStore(String databaseURI) {
        // set variable
        this.dbURI = databaseURI;

        // create movie table, if it does not already exists
        try (Connection connection = DriverManager.getConnection(dbURI);
             Statement statement = connection.createStatement()) {

            String createMovieTableSQL = "CREATE TABLE IF NOT EXISTS movie " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "movieTitle TEXT, " +
                    "year TEXT, " +
                    "moviePlot TEXT, " +
                    "metascore INTEGER, " +
                    "userRating REAL CHECK (userRating >= 1 AND userRating <= 5)," +
                    "dateAdded INTEGER, " +
                    "dateUpdated INTEGER)";

            statement.execute(createMovieTableSQL);

        } catch (SQLException sqle) {
            System.err.println("Error creating 'movie' table. Error: " + sqle);
        }
    }

    // add a movie to the movie table
    public void addMovie(Movie movieToAdd) throws SQLException {

        // SQL Statement to INSERT record into 'movie' table
        String addMovieSQL = "INSERT INTO movie (movieTitle, year, moviePlot, metascore, userRating, dateAdded, dateUpdated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // try-with-resources to apply movieToAdd object fields to expectant parameters in addMovie SQL statement
        try (Connection connection = DriverManager.getConnection(dbURI);
             PreparedStatement preparedStatement = connection.prepareStatement(addMovieSQL)) {

            // set parameter 1, 2, and 3
            preparedStatement.setString(1, movieToAdd.getTitle());
            preparedStatement.setString(2, movieToAdd.getYear());
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

    // update the user rating for a movie stored in the movie table
    public void updateMovieUserRating(Movie movieToUpdate) throws SQLException {

        String updateUserRatingSQL = "UPDATE movie SET userRating = ?, dateUpdated = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(dbURI);
             PreparedStatement preparedStatement = connection.prepareStatement(updateUserRatingSQL)) {

            preparedStatement.setDouble(1, movieToUpdate.getUserRating());
            preparedStatement.setLong(2, movieToUpdate.getDateUpdated().getTime());
            preparedStatement.setInt(3, movieToUpdate.getId());

            // execute UPDATE statement
            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
            throw sqle;
        }

    }


    // search for a movie stored in the movie table
    public boolean searchByTitle(String omdbMovieTitle, String omdbMovieYear) {

        String searchByMovieTitleSQL;

        // This method chooses which query to run depending on whether one or two search fields were submitted with content.
        // The GUI manages at least the movie title being submitted.
        if (omdbMovieYear.isBlank()) {
            searchByMovieTitleSQL = "SELECT * FROM movie WHERE movieTitle = ?";
        } else {
            searchByMovieTitleSQL = "SELECT * FROM movie WHERE movieTitle = ? AND year = ?";
        }

        try (Connection connection = DriverManager.getConnection(dbURI);
             PreparedStatement preparedStatement = connection.prepareStatement(searchByMovieTitleSQL)) {

            preparedStatement.setString(1, omdbMovieTitle);
            preparedStatement.setString(2, omdbMovieYear);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
            return false;
        }

    }

    // queries the db to get a list of all movies stored in the movie table.
    public List<Movie> getAllMovies() {

        try (Connection connection = DriverManager.getConnection(dbURI);
             Statement statement = connection.createStatement()) {

            List<Movie> allMovies = new ArrayList<>();

            String selectAllMoviesSQL = "SELECT * FROM movie ORDER BY movieTitle";

            ResultSet resultSet = statement.executeQuery(selectAllMoviesSQL);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("movieTitle");
                String year = resultSet.getString("year");
                String plot = resultSet.getString("moviePlot");
                int metascore = resultSet.getInt("metascore");
                double userRating = resultSet.getDouble("userRating");
                long dateAdded = resultSet.getLong("dateAdded");
                long dateUpdated = resultSet.getLong("dateUpdated");

                Date dateAdd = new Date(dateAdded);
                Date dateUpdt = new Date(dateUpdated);

                Movie movie = new Movie(id, title, year, plot, metascore, userRating, dateAdd, dateUpdt);

                allMovies.add(movie);
            }
            return allMovies;

        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
            return null;
        }
    }

    // deletes a movie from the movie table
    public void deleteByMovieId(int movieId) throws SQLException {

        String deleteMovieSQL = "DELETE FROM movie WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(dbURI);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteMovieSQL)) {

            preparedStatement.setInt(1, movieId);

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
        }
    }

    // calculates the users average movie rating, excluding null values (aka, movies that were not been rated when saved)
    public double getAverageRating() {

        try (Connection connection = DriverManager.getConnection(dbURI);
             Statement statement = connection.createStatement()) {

            String getAverageRatingSQL = "SELECT ROUND(AVG(userRating), 2) AS \"My Average Rating\" FROM movie";

            ResultSet resultSet = statement.executeQuery(getAverageRatingSQL);
            double avgRating = 0.0;
            if (resultSet.next()) {
                avgRating = resultSet.getDouble("My Average Rating");
            }
            return avgRating;

        } catch (SQLException sqle) {
            System.err.println("Error: " + sqle);
            return 0.0;
        }
    }


}
