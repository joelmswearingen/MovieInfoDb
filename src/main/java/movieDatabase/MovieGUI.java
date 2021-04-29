package movieDatabase;

import javax.swing.*;
import java.awt.*;

public class MovieGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JList movieDetailsList;
    private JButton saveAndRateMovieButton;
    private JButton showAllSavedMoviesButton;
    private JButton saveMovieWithoutRatingButton;

    private MovieController controller;

    MovieGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Open Movie Database Movie Finder");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(500, 400));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }

}
