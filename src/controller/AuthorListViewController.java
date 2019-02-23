package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Author;
import model.Book;

public class AuthorListViewController {

	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
    @FXML
    private ListView<Author> authorList;
    
	ObservableList<Author> authors = FXCollections.observableArrayList();

	public AuthorListViewController(ObservableList<Author> authors) {
		this.authors = authors;
	}
	@FXML
	public void initialize() {
		authorList.getItems().addAll(authors);
		logger.info("Authors printed: " + authors);
	}
    @FXML
    void itemClick(MouseEvent event) {
    	if (!authorList.getSelectionModel().isEmpty() && event.getClickCount() == 2) {
	    	logger.info("Book " + authorList.getSelectionModel().getSelectedItem() + " Selected. Details Shown.");
	    	ViewSwitcher.setAuthor(authorList.getSelectionModel().getSelectedItem());
    		authorList.getSelectionModel().clearSelection();
    		ViewSwitcher.getInstance().switchView(5);
    	} 
    }

}

