package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages Movie objects. It contains various constructors for scenario specific movie object instances,
 * getters and setter since fields are private, and a toString() method to allow for human-readable printed as needed. */

import java.util.Date;

public class Movie {

    private int id;
    private String title;
    private String year;
    private String plot;
    private int metascore;
    private double userRating;
    private Date dateAdded;
    private Date dateUpdated;

    // constructor with all data fields in db
    public Movie(int id, String title, String year, String plot, int metascore, double userRating, Date dateAdded, Date dateUpdated) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.metascore = metascore;
        this.userRating = userRating;
        this.dateAdded = dateAdded;
        this.dateUpdated = dateUpdated;
    }

    // constructor for when a Movie object is passed to the MovieListGUI
    public Movie(int id, String title, String year,
                 int metascore, double userRating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.metascore = metascore;
        this.userRating = userRating;
    }

    // constructor for when a Movie object is passed from the MovieListGUI
    public Movie(int id, String title, String year,
                 int metascore, double userRating, Date dateUpdated) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.metascore = metascore;
        this.userRating = userRating;
        this.dateUpdated = dateUpdated;
    }

    // constructor for when a Movie objects is passed to the MovieStore without an updated userRating
    public Movie(String title, String year, String plot,
                 int metascore, double userRating, Date dateAdded, Date dateUpdated) {
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.metascore = metascore;
        this.userRating = userRating;
        this.dateAdded = dateAdded;
        this.dateUpdated = dateUpdated;
    }

    // constructor for when a Movie object is passed to the RateMovieGUI to have user Rating applied
    public Movie(String title, String year, String plot,
                 int metascore, double userRating) {
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.metascore = metascore;
        this.userRating = userRating;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getMetascore() {
        return metascore;
    }

    public void setMetascore(int metascore) {
        this.metascore = metascore;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    // toString human-readable object instance
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", plot='" + plot + '\'' +
                ", metascore=" + metascore +
                ", userRating=" + userRating +
                '}';
    }
}
