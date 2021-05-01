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

    // constructor
    public OmdbResponse(String plot, String title, String rated, String actors, String year, String metascore) {
        Plot = plot;
        Title = title;
        Rated = rated;
        Actors = actors;
        Year = year;
        Metascore = metascore;
    }


    // converts object to human readable string

//    @Override
//    public String toString() {
//        return "Movie: " + Title + "\n" +
//                "Year: " + Year + "\n" +
//                "Rated: " + Rated + "\n" +
//                "Starring: " + Actors + "\n" +
//                "Rating: " + Metascore + "\n" +
//                "Plot:" + Plot;
//    }



}
