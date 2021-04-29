package movieDatabase;

import javax.swing.*;
import java.awt.*;

public class OmdbResponse {

    // class fields corresponding to JSON Key/value pairs
    public String Plot;
    public String Title;
    public String Rated;
    public String Actors;
    public String Year;
    public String Metascore;


    public OmdbResponse(String plot, String title, String rated, String actors, String year, String metascore) {
        Plot = plot;
        Title = title;
        Rated = rated;
        Actors = actors;
        Year = year;
        Metascore = metascore;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    @Override
    public String toString() {
        return "Movie: " + Title + "\n" +
                "Year: " + Year + "\n" +
                "Rated: " + Rated + "\n" +
                "Starring: " + Actors + "\n" +
                "Rating: " + Metascore + "\n" +
                "Plot:" + Plot;
    }




}
