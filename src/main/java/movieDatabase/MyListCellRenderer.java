package movieDatabase;

import javax.swing.*;
import java.awt.*;

public class MyListCellRenderer extends JLabel implements ListCellRenderer<OmdbResponse> {

    // constructor to set
    public MyListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends OmdbResponse> movieDetailsList,
            OmdbResponse value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        String title = value.Title;
        String year = value.Year;
        String rated = value.Rated;
        String actors = value.Actors;
        String metascore = value.Metascore;
        String plot = value.Plot;

        // metascore may be returned as N/A in which case we don't want to display %
        // create variable include or exclude % dependent on value that is returned
        String hasMetascore = " out of 100";

        if (metascore.equals("N/A")) {
            hasMetascore = "";
        }


        // add wrap functionality
        int jListWidth = movieDetailsList.getWidth();

        /*
        when running debugger it says this value is 516px, but when it gets to "return this" in the JList
        it's resets (? not sure what it's doing) the width to 670px and the wrap allow the text to go further than scroll.
        As a result you can only see the full text by resizing the app.
        */

        String labelText = "<html><body style='width: " + jListWidth + "px;'>MOVIE: " + title + "<br/>" +
                "YEAR: " + year + "<br/>" +
                "RATED: " + rated + "<br/>" +
                "STARRING: " + actors + "<br/>" +
                "METACRITIC SCORE: " + metascore + hasMetascore + "<br/>" +
                "PLOT: " + plot + "</body></html>";


            // without wrap functionality
//            String labelText = "<html>MOVIE: " + title + "<br/>" +
//                    "YEAR: " + year + "<br/>" +
//                    "RATED: " + rated + "<br/>" +
//                    "STARRING: " + actors + "<br/>" +
//                    "RATING: " + metascore + hasMetascore + "<br/>" +
//                    "PLOT: " + plot;

        setText(labelText);

        if (isSelected) {
            setBackground(movieDetailsList.getSelectionBackground());
            setForeground(movieDetailsList.getSelectionForeground());
        } else {
            setBackground(movieDetailsList.getBackground());
            setForeground(movieDetailsList.getForeground());
        }

        return this;

    }
}