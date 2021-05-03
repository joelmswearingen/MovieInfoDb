package movieDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MovieListGUI extends JFrame {
    private JPanel mainPanel;
    private JTable movieListTable;
    private JButton refreshListButton;
    private JButton updateMovieRatingButton;
    private JButton deleteMovieButton;
    private JLabel averageRatingLabel;
    private JLabel topFiveMoviesLabel;

    private DefaultTableModel defaultTableModel;
    private DefaultTableColumnModel defaultTableColumnModel;
    private TableColumn tableColumn;

    private MovieController controller;

    MovieListGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Review and Update");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // TODO: upon launch, populate movie list from db
        // table setup
        defaultTableModel = new DefaultTableModel();
        defaultTableColumnModel = new DefaultTableColumnModel();
        movieListTable.setModel(defaultTableModel);

        // column headers for the table
        defaultTableModel.addColumn("ID");
        defaultTableModel.addColumn("Movie Title");
        defaultTableModel.addColumn("Release Year");
        defaultTableModel.addColumn("Metacritic Score");
        defaultTableModel.addColumn("My Rating");

//        defaultTableColumnModel.getColumn(0).setPreferredWidth(20);
//        defaultTableColumnModel.getColumn(1).setPreferredWidth(100);
//        defaultTableColumnModel.getColumn(2).setPreferredWidth(30);
//        defaultTableColumnModel.getColumn(3).setPreferredWidth(20);
//        defaultTableColumnModel.getColumn(4).setPreferredWidth(20);
        
        List<Movie> allMovies = controller.getAllMoviesFromDatabase();

        for (Movie movie : allMovies) {
            String id = String.valueOf(movie.getId());
            String title = movie.getTitle();
            String year = movie.getYear();
            String metascore = String.valueOf(movie.getMetascore());
            String userRating = String.valueOf(movie.getUserRating());

            String[] m = {id, title, year, metascore, userRating};

            defaultTableModel.addRow(m);

        }


    }

}
