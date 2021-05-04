package movieDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


public class RateMovieGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel movieTitleAndYear;
    private JRadioButton oneStarRadioButton;
    private JRadioButton twoStarRadioButton;
    private JRadioButton threeStarRadioButton;
    private JRadioButton fourStarRadioButton;
    private JRadioButton fiveStarRadioButton;
    private JButton saveButton;
    private JButton cancelButton;


    private Movie movieToRate;

    private MovieController controller;


    RateMovieGUI(MovieController controller, Movie movieToRate) {
        this.controller = controller;
        this.movieToRate = movieToRate;

        setTitle("Rate and Save");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(550, 215));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // establish radio button group and set button options so that only one radio button can be selected at a time
        ButtonGroup starRatingButtonsGroup = new ButtonGroup();
        starRatingButtonsGroup.add(oneStarRadioButton);
        starRatingButtonsGroup.add(twoStarRadioButton);
        starRatingButtonsGroup.add(threeStarRadioButton);
        starRatingButtonsGroup.add(fourStarRadioButton);
        starRatingButtonsGroup.add(fiveStarRadioButton);

        // set movie title and year that is being reviewed on opening
        String title = movieToRate.getTitle();
        String year = movieToRate.getYear();

        // set movie Title and Year found in Movie object
        movieTitleAndYear.setText(title + " (" + year + ")");

        eventListeners();

    }

    private void eventListeners() {

        RadioButtonActionListener actionListener = new RadioButtonActionListener();
        oneStarRadioButton.addActionListener(actionListener);
        twoStarRadioButton.addActionListener(actionListener);
        threeStarRadioButton.addActionListener(actionListener);
        fourStarRadioButton.addActionListener(actionListener);
        fiveStarRadioButton.addActionListener(actionListener);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double userRating = movieToRate.getUserRating();
                if (userRating == 0) {
                    errorDialog("Please select a star rating");
                } else {
                    // if object has an ID, send to update method via controller to moviestore
                    // if object does NOT have an ID, send to add method via controller to moviestore
                    // set Date object with current timestamp
                    boolean idExists = true;
                    int id = movieToRate.getId();
                    if (id <= 0) { idExists = false; }

                    if (idExists) {
                        boolean updated = controller.updateMovieInDatabase(movieToRate);
                        if (updated) {
                            dispose();
                        } else {
                            errorDialog("Something went wrong. Please select \"Cancel\" and try again.");
                        }
                    } else {
                        // add to both dateAdded and dateUpdated
                        // NOTE: after initial add, date updated will be used to capture user updates to the db
                        Date date = new Date();
                        movieToRate.setDateAdded(date);
                        movieToRate.setDateUpdated(date);
                        boolean added = controller.addMovieToDatabase(movieToRate);

                        if (added) {
                            dispose();
                        } else {
                            errorDialog("Something went wrong. Please select \"Cancel\" and try again.");
                        }
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void errorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(RateMovieGUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // code solicited from example found at https://www.codejava.net/java-se/swing/jradiobutton-basic-tutorial-and-examples
    class RadioButtonActionListener implements ActionListener {
        double userRating;

        @Override
        public void actionPerformed(ActionEvent event) {
            JRadioButton button = (JRadioButton) event.getSource();

            if (button == oneStarRadioButton) {
                userRating = 1;
            } else if (button == twoStarRadioButton) {
                userRating = 2;
            } else if (button == threeStarRadioButton) {
                userRating = 3;
            } else if (button == fourStarRadioButton) {
                userRating = 4;
            } else if ( button == fiveStarRadioButton) {
                userRating = 5;
            }
            movieToRate.setUserRating(userRating);
        }



    }


}
