package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alert.InvalidParametersAlert;
import confirm.ConfirmType;
import exception.InvalidBookException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import model.Author;
import model.Book;
import confirm.ExitConfirm;

public class MenuViewController implements Initializable {

	
	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
	@FXML
	private MenuItem menuQuit;
	
	@FXML
	private MenuItem menuBookList;
	
	@FXML
	private MenuItem menuAddBook;
	
	private boolean menuItemSelected;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
    @FXML
    void menuBookListClick(ActionEvent event) {
    	logger.info("Book List Selected. View Switched");
    	menuItemSelected = true;
    	ViewSwitcher.getInstance().switchView(1);
    }
    
    @FXML
    void menuAuthorListClick(ActionEvent event) {
    	logger.info("Author List Selected. View Switched");
    	menuItemSelected = true;
    	ViewSwitcher.getInstance().switchView(4);
    }


    @FXML
    void menuAddBookClick(ActionEvent event) {
    	logger.info("Add Book Selected. View Switched");
    	menuItemSelected = true;
    	ViewSwitcher.setBook(new Book());
    	ViewSwitcher.getInstance().switchView(1);
    	ViewSwitcher.getInstance().switchView(2);
    }
    @FXML
    void menuAddAuthorClick(ActionEvent event) {
    	logger.info("Add Author Selected. View Switched");
    	menuItemSelected = true;
    	ViewSwitcher.setAuthor(new Author());
    	ViewSwitcher.getInstance().switchView(4);
    	ViewSwitcher.getInstance().switchView(5);
    }
    @FXML
    void menuQuitClick(ActionEvent event) {
    	ConfirmType userAction;// = ChangeBookViewConfirm.show(ViewSwitcher.getBook());
    	userAction = ExitConfirm.show(ViewSwitcher.getBook());
    	if(userAction == ConfirmType.YES) {
	    	if(menuItemSelected)
	    		ViewSwitcher.getInstance().switchView(2);
	    	Platform.exit();
    	}

    }


}
