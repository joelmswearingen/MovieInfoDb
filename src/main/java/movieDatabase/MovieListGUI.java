package movieDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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

        // table setup
        defaultTableModel = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        movieListTable.setModel(defaultTableModel);
        movieListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieListTable.setRowSelectionAllowed(true);
        movieListTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        movieListTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        movieListTable.setShowGrid(false);
        movieListTable.setShowHorizontalLines(true);
        movieListTable.setGridColor(Color.GRAY);

        // set column headers for the table in list and loop through list to set column headers
        String[] columns = {"Id", "Movie Title", "Release Year", "Metacritic Score", "My Rating"};
        for ( String column : columns ) {
            defaultTableModel.addColumn(column);
        }

        // set column px width for each table column in list and loop through list to set each width
        TableColumnModel columnModel = movieListTable.getColumnModel();
        int[] columnWidths = {56, 330, 120, 140, 120}; // total is 766, which is width of JTable
        for ( int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // upon launch, populate movie list from db and get user stats
        getTableData();
        setUserStats();

        // call event listeners for button clicks
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

                    // parse string to int as needed
                    int id = Integer.parseInt(idAsString);
                    int metascore = Integer.parseInt(metascoreAsString);
                    double userRating = Double.parseDouble(userRatingAsString);

                    // create date variable to update dateUpdated
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
                        setUserStats();
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
        double avgRating = controller.getAverageMovieRating();  // if movie table is empty, query will return 0.0 as avgRating
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
