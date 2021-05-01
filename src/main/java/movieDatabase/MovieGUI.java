package movieDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MovieGUI extends JFrame {

    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JList<OmdbResponse> movieDetailsList;
    private JButton saveAndRateMovieButton;
    private JButton showAllSavedMoviesButton;
    private JButton saveMovieWithoutRatingButton;
    private JLabel movieSearchResultsLabel;

    private DefaultListModel<OmdbResponse> movieDetailsListModel;

    private MovieController controller;

    MovieGUI(MovieController controller) {
        this.controller = controller;

        setTitle("Open Movie Database Movie Finder");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(500, 400));
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

                List<OmdbResponse> omdbResponseList = new ArrayList<>();

                if (searchTerm.isBlank()) {
                    errorDialog("Search field cannot be blank");
                } else {
                    OmdbResponse response = controller.openMovieDatabaseQuery(searchTerm);
                    if (response != null) {
                        omdbResponseList.add(response);
                        setListData(omdbResponseList);

                    }
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

    private static class MyListCellRenderer extends JLabel implements ListCellRenderer<OmdbResponse> {

        @Override
        public Component getListCellRendererComponent(
                JList movieDetailsList,
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

            int jListWidth = movieDetailsList.getWidth();

            String labelText = "<html><body style='width: " + jListWidth + "px;>Movie: " + title + "<br/>" +
                    "Year: " + year + "<br/>" +
                    "Rated: " + rated + "<br/>" +
                    "Starring: " + actors + "<br/>" +
                    "Rating: " + metascore + "<br/>" +
                    "Plot:" + plot + "</body></html>";

            setText(labelText);

            return this;

        }
    }



    private void errorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(MovieGUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
