package movieDatabase;

public class Movie {

    private int id;
    private String title;
    private int year;
    private String plot;
    private int metascore;
    private double userRating;

    public Movie(int id, String title, int year, String plot, int metascore, double userRating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.metascore = metascore;
        this.userRating = userRating;
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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
