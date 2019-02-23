package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;

public class BookListViewController {
	
	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
	@FXML
	ListView<Book> bookList;
	
	ObservableList<Book> books = FXCollections.observableArrayList();
	
	public BookListViewController(ObservableList<Book> books) {
		this.books = books;
	}
	@FXML
	public void initialize() {
		bookList.getItems().addAll(books);
		logger.info("Books printed: " + books);
	}
    @FXML
    void itemClick(MouseEvent event) {
    	if (!bookList.getSelectionModel().isEmpty() && event.getClickCount() == 2) {
	    	logger.info("Book " + bookList.getSelectionModel().getSelectedItem() + " Selected. Details Shown.");
	    	ViewSwitcher.setBook(bookList.getSelectionModel().getSelectedItem());
    		bookList.getSelectionModel().clearSelection();
    		ViewSwitcher.getInstance().switchView(2);
    	} 
    }
    
    
}
