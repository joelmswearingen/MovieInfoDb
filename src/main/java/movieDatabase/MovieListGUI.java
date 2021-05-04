package movieDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieListGUI extends JFrame {
    private JPanel mainPanel;
    private JTable movieListTable;
    private JButton refreshListButton;
    private JButton updateMovieRatingButton;
    private JButton deleteMovieButton;
    private JLabel averageRatingLabel;
    private JButton closeButton;

    private DefaultTableModel defaultTableModel;

    private MovieController controller;

    MovieListGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Review and Update");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800, 450));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // upon launch, populate movie list from db
        // table setup
        defaultTableModel = new DefaultTableModel();
        movieListTable.setModel(defaultTableModel);
        movieListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieListTable.setRowSelectionAllowed(true);

        // set column headers for the table in a List and loop through list to set column headers
        String[] columns = {"Id", "Movie Title", "Release Year", "Metacritic Score", "My Rating"};
        for ( String column : columns ) {
            defaultTableModel.addColumn(column);
        }

        getTableData();
        setUserStats();
        eventListeners();

    }

    private void eventListeners() {
        refreshListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTableData();
                setUserStats();
            }
        });

        updateMovieRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ensure a line is selected
                int column = movieListTable.getSelectedColumn();
                if (column == -1) {
                    errorDialog("Please select a movie to update rating");
                    return;
                }

                try {
                    int columns = movieListTable.getColumnCount();
                    int row = movieListTable.getSelectedRow(); // gives you row index
                    ArrayList<String> movieRecord = new ArrayList<>();
                    for (int i = 0; i < columns; i++) {
                        String movieElement = movieListTable.getModel().getValueAt(row, i).toString();
                        movieRecord.add(movieElement);
                    }

                    String idAsString = movieRecord.get(0);
                    String title = movieRecord.get(1);
                    String year = movieRecord.get(2);
                    String metascoreAsString = movieRecord.get(3);
                    String userRatingAsString = movieRecord.get(4);

                    int id = Integer.parseInt(idAsString);
                    int metascore = Integer.parseInt(metascoreAsString);
                    double userRating = Double.parseDouble(userRatingAsString);

                    Date date = new Date();

                    Movie updateMovieRating = new Movie(id, title, year, metascore, userRating, date);

                    Main.rateMovieGUI = new RateMovieGUI(controller, updateMovieRating);


                } catch (NumberFormatException nfe) {
                    System.out.println("Error: " + nfe);
                }
            }
        });


        deleteMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ensure a line is selected
                int column = movieListTable.getSelectedColumn();
                if (column == -1) {
                    errorDialog("Please select a movie to remove");
                    return;
                }

                try {

                    int row = movieListTable.getSelectedRow();
                    String movieIdAsString = movieListTable.getModel().getValueAt(row, 0).toString();

                    int movieId = Integer.parseInt(movieIdAsString);

                    boolean deleted = controller.deleteMovieFromDatabase(movieId);

                    if (deleted) {
                        getTableData();
                        JOptionPane.showMessageDialog(MovieListGUI.this, "Movie has been deleted");
                    } else {
                        errorDialog("Movie was not deleted. Please try again.");
                    }

                } catch (NumberFormatException nfe) {
                    System.out.println("Error: " + nfe);
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void getTableData() {
        // query db to populate movie table. send results to setTableData method to populate table
        List<Movie> allMovies = controller.getAllMoviesFromDatabase();
        setTableData(allMovies);
    }

    private void setTableData(List<Movie> movies) {
        defaultTableModel.setRowCount(0);
        for (Movie movie : movies) {
            String id = String.valueOf(movie.getId());
            String title = movie.getTitle();
            String year = movie.getYear();
            String metascore = String.valueOf(movie.getMetascore());
            String userRating = String.valueOf(movie.getUserRating());

            String[] record = {id, title, year, metascore, userRating};

            defaultTableModel.addRow(record);

        }
    }

    private void setUserStats() {
        double avgRating = controller.getAverageMovieRating();
        averageRatingLabel.setText("My Average Rating is " + avgRating + " stars");

    }


    // word wrap in JOptionPane code found here: https://stackoverflow.com/questions/7861724/is-there-a-word-wrap-property-for-jlabel/7861833#7861833
    private void errorDialog(String errorMessage) {
        String html = "<html><body style='width: %1spx'>%1s";
        JOptionPane.showMessageDialog(
                MovieListGUI.this,
                String.format(html, 200, errorMessage),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
