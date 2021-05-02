package movieDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieGUI extends JFrame {

    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JList<OmdbResponse> movieDetailsList;
    private JButton rateAndSaveMovieButton;
    private JButton showAllSavedMoviesButton;
    private JButton saveMovieWithoutRatingButton;
    private JLabel movieSearchResultsLabel;

    private DefaultListModel<OmdbResponse> movieDetailsListModel;

    private MovieController controller;

    MovieGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Open Movie Database Movie Finder");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(550, 400));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        movieDetailsListModel = new DefaultListModel<>();
        movieDetailsList.setModel(movieDetailsListModel);
        movieDetailsList.setCellRenderer(new MyListCellRenderer());
        movieDetailsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        eventListeners();

    }

    private void eventListeners() {

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get text entered in searchTextField and trim
                String searchTerm = searchTextField.getText();
                searchTerm = searchTerm.trim();
                // clear the listModel, to clean the GUI up.
                clearListData();

                List<OmdbResponse> omdbResponseList = new ArrayList<>();

                if (searchTerm.isBlank()) {
                    errorDialog("Search field cannot be blank");
                } else {
                    OmdbResponse response = controller.openMovieDatabaseQuery(searchTerm);

                    String movieTitle = response.Title;
                    if (movieTitle != null) {
                        omdbResponseList.add(response);
                        setListData(omdbResponseList);
                    } else {
                        clearListData();
                        setMovieSearchResultsLabelWithTimer("\"" + searchTerm + "\" not found. Please try again.");
                    }
                    searchTextField.setText("");
                }

            }
        });

        rateAndSaveMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get selected item from movieDetailsList JList
                OmdbResponse selectedMovie = movieDetailsList.getSelectedValue();
                 if (selectedMovie == null) {
                     errorDialog("Please select a movie");
                     return;
                 }

                String omdbTitle = selectedMovie.Title;
                boolean existsInDb = controller.searchDbByMovieTitle(omdbTitle);

                if (existsInDb) {
                    errorDialog("You have already saved \"" + omdbTitle + "\" to your Movie List. " +
                            "Please navigate to \"Show All Saved Movies\" to review or update rating.");
                    return;
                }

                // get selected fields from object to be added to new Movie object for INSERT into db
                try {
                    String title = selectedMovie.Title;
                    String year = selectedMovie.Year;
                    String plot = selectedMovie.Plot;
                    String scoreAsString = selectedMovie.Metascore;

                    // set score to 0 (which will be added to Movie object)
                    // MovieStore will set "0" score values as null in db
                    int score = 0;
                    if(!scoreAsString.equalsIgnoreCase("N/A")) {
                        score = Integer.parseInt(scoreAsString);
                    }

                    double userRating = 0.0; // set user rating to 0 as it has not been defined yet

                    Movie movieToRate = new Movie(title, year, plot, score, userRating);

                    Main.rateMovieGUI = new RateMovieGUI(controller, movieToRate);

                    // TODO how to make these execute after new RateMovieGUI screen is disposed.
                    clearListData();
                    setMovieSearchResultsLabelWithTimer(title + " has been added to your Movies List!");

                } catch (NumberFormatException nfe) {
                    System.err.println("Error: " + nfe);
                    throw nfe;
                }
            }
        });

        saveMovieWithoutRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get selected item from movieDetailsList JList
                OmdbResponse selectedMovie = movieDetailsList.getSelectedValue();
                if (selectedMovie == null) {
                    errorDialog("Please select a movie");
                    return;
                }

                String omdbTitle = selectedMovie.Title;
                boolean existsInDb = controller.searchDbByMovieTitle(omdbTitle);

                if (existsInDb) {
                    errorDialog("You have already saved \"" + omdbTitle + "\" to your Movie List. " +
                            "Please navigate to \"Show All Saved Movies\" to review or update rating.");
                    return;
                }

                // get selected fields from object to be added to new Movie object for INSERT into db
                try {
                    String title = selectedMovie.Title;
                    String year = selectedMovie.Year;
                    String plot = selectedMovie.Plot;
                    String scoreAsString = selectedMovie.Metascore;

                    // set score to 0 (which will be added to Movie object)
                    // MovieStore will set "0" year values as null in db
                    int score = 0;
                    if(!scoreAsString.equalsIgnoreCase("N/A")) {
                        score = Integer.parseInt(scoreAsString);
                    }

                    double userRating = 0.0; // set user rating to 0, database will commit 0's as null

                    Date date = new Date();

                    Movie movieToRate = new Movie(title, year, plot, score, userRating, date, date);

                    boolean added = controller.addMovieToDatabase(movieToRate);

                    if (added) {
                        clearListData();
                        setMovieSearchResultsLabelWithTimer(title + " has been added to your Movies List!");
                    } else {
                        errorDialog("Something went wrong. Please try again.");
                    }

                } catch (NumberFormatException nfe) {
                    System.err.println("Error: " + nfe);
                    throw nfe;
                }
            }
        });


    }

    private void setListData(List<OmdbResponse> omdbResponses) {
        movieDetailsListModel.clear();
        if (omdbResponses != null) {
            movieDetailsListModel.addAll(omdbResponses);
        }

    }

    private void clearListData() {
        movieDetailsListModel.clear();
    }

    private void setMovieSearchResultsLabelWithTimer(String label) {
        movieSearchResultsLabel.setText(label);
        Timer timer = new Timer(5000, event -> {
            movieSearchResultsLabel.setText("");
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void errorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(MovieGUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
