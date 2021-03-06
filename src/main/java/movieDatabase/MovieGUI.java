package movieDatabase;

/** Created by Joel Swearingen May 2021
 * This file manages primary GUI and all of it's buttons and displays */

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
    private JTextField optionalYearTextField;
    private JButton closeButton;
    private JLabel funFactLabel;

    private DefaultListModel<OmdbResponse> movieDetailsListModel;

    private MovieController controller;

    MovieGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Open Movie Database Movie Finder");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(700, 385));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        movieDetailsListModel = new DefaultListModel<>();
        movieDetailsList.setModel(movieDetailsListModel);
        movieDetailsList.setCellRenderer(new MyListCellRenderer());
        movieDetailsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        funFactLabel.setText("" +
                "<html><body style='width: 300px;'>" +
                "The world's earliest surviving motion-picture film is <i>Roundhay Garden Scene</i> (1888)" +
                "</body></html>");

        eventListeners();

    }

    private void eventListeners() {

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get text entered in searchTextField and optionalYearTextField and trim each
                String searchTerm = searchTextField.getText();
                searchTerm = searchTerm.trim();

                String searchYear = optionalYearTextField.getText();
                searchYear = searchYear.trim();

                // clear the listModel, to clean the GUI up.
                clearListData();



                if (searchTerm.isBlank()) {
                    errorDialog("Search field cannot be blank");
                } else {
                    // movie search in the background
                    // Disable search button and change text while thread
                    searchButton.setEnabled(false);
                    searchButton.setText("Searching...");

                    MovieSearch search = new MovieSearch(searchTerm, searchYear);
                    search.execute();

//                    OmdbResponse response = controller.openMovieDatabaseQuery(searchTerm, searchYear);
//                    updateMovie(response);
                }
            }
        });

        rateAndSaveMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get selected item from movieDetailsList JList
                OmdbResponse selectedMovie = movieDetailsList.getSelectedValue();
                if (selectedMovie == null) {
                     errorDialog("Please select a movie to rate");
                     return;
                }

                // get title from omdbResponse object and validate movie title does not already exist in db
                String omdbTitle = selectedMovie.Title;
                String omdbYear = selectedMovie.Year;
                boolean existsInDb = controller.searchDbByMovieTitle(omdbTitle, omdbYear);
                // if the movie title is found in db, display error to the user and interrupt actionListener
                if (existsInDb) {
                    errorDialog("You have already saved \"" + omdbTitle + "\" to your Movie List.  " +
                            "Please navigate to \"Show All Saved Movies\" to review list and update ratings.");
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

                // get title from omdbResponse object and validate movie title does not already exist in db
                String omdbTitle = selectedMovie.Title;
                String omdbYear = selectedMovie.Year;
                boolean existsInDb = controller.searchDbByMovieTitle(omdbTitle, omdbYear);
                // if the movie title is found in db, display error to the user and interrupt actionListener
                if (existsInDb) {
                    errorDialog("You have already saved \"" + omdbTitle + "\" to your Movie List.  " +
                            "Please navigate to \"Show All Saved Movies\" to review list and update ratings.");
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

        showAllSavedMoviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.movieListGUI = new MovieListGUI(controller);
                showAllSavedMoviesButton.setEnabled(false);  // prevent the user from opening multiple instances of the movie list
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.quitProgram();
            }
        });

    }

    public void enableShowAllMoviesButton() {
        showAllSavedMoviesButton.setEnabled(true); // re-enable show all button if listGUI is closed

    }


    // once API call is completed, use this method to finish updating main GUI and re-enable buttons, etc.
    public void updateMovie(OmdbResponse response) {
        List<OmdbResponse> omdbResponseList = new ArrayList<>();

        String movieTitle = response.Title;
        if (movieTitle != null) {
            omdbResponseList.add(response);
            setListData(omdbResponseList);
        } else {
            setMovieSearchResultsLabelWithTimer("Movie not found. Please try again.");
        }
        searchTextField.setText("");
        optionalYearTextField.setText("");
        // enable search button
        searchButton.setEnabled(true);
        searchButton.setText("Search");

    }


    private void setListData(List<OmdbResponse> omdbResponses) {
        movieDetailsListModel.clear();
        if (omdbResponses != null) {
            movieDetailsListModel.addAll(omdbResponses);
            movieDetailsList.setSelectedIndex(0);
        }
    }


    public void clearListData() { movieDetailsListModel.clear(); }


    // set a timer on the display label field so that inaccurate text isn't hanging around
    public void setMovieSearchResultsLabelWithTimer(String label) {
        movieSearchResultsLabel.setText(label);
        Timer timer = new Timer(5000, event -> {
            movieSearchResultsLabel.setText("");
        });
        timer.setRepeats(false);
        timer.start();
    }


    // word wrap in JOptionPane code found here: https://stackoverflow.com/questions/7861724/is-there-a-word-wrap-property-for-jlabel/7861833#7861833
    private void errorDialog(String errorMessage) {
        String html = "<html><body style='width: %1spx'>%1s";
        JOptionPane.showMessageDialog(
                MovieGUI.this,
                String.format(html, 200, errorMessage),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }


}
