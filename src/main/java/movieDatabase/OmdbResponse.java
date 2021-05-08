package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages API Response objects.
 * It contains a single constructor based on the data we want from the API call */

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
