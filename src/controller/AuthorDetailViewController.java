package controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alert.InvalidParametersAlert;
import confirm.ConfirmType;
import exception.InvalidBookException;
import gateway.AuthorTableGateway;
import gateway.BookTableGateway;
import gateway.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Author;
import model.Book;

public class AuthorDetailViewController {

	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
    @FXML
    private TextField textFirst;

    @FXML
    private TextField textLast;

    @FXML
    private TextField textDate;

    @FXML
    private TextField textGender;

    @FXML
    private TextField textWebsite;

	private Author author;

    public AuthorDetailViewController(Author author) {
    	this.author = author;
    }
    @FXML
    public void initialize(){
    	try {
	    	this.textFirst.setText(this.author.getFirstname());
	    	this.textLast.setText(this.author.getLastname());
	    	this.textGender.setText(this.author.getGender());
	    	this.textDate.setText(this.author.getDateOfBirth().toString());
	    	this.textWebsite.setText(this.author.getWebsite());
    	} catch (NullPointerException e ) {
    		
    	}
    }
    @FXML
    void onDeleteClick(MouseEvent event) {
		ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Delete");
		if(userAnswer == ConfirmType.YES) {
			try {
				Connection conn = DataSource.getDs().getConnection();
				AuthorTableGateway gateway = new AuthorTableGateway(conn);
				gateway.deleteAuthor(this.author);
				logger.info("Delete successful.");
				conn.close();
			} catch (SQLException e) {
				logger.error("Delete Failed: " + e.getMessage());
			}
			ViewSwitcher.getInstance().switchView(4);
		}
    }

    @FXML
    void onSaveClick(MouseEvent event) {
		ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Save");
		if(userAnswer == ConfirmType.YES) {
			initiateSave();
			ViewSwitcher.getInstance().switchView(4);
		} else if(userAnswer == ConfirmType.NO) {
			ViewSwitcher.getInstance().switchView(4);
		}
    }
    
    private void initiateSave() {
		this.author.setFirstname(textFirst.getText());
		this.author.setLastname(textLast.getText());
		this.author.setDateOfBirth(LocalDate.parse(textDate.getText()));
		this.author.setGender(textGender.getText());
		this.author.setWebsite(textWebsite.getText());
		try {
			author.save(this.author);
			logger.info("Save Successful.");
		} catch (SQLException e) {
			logger.info("Save Unsuccessful.");
			e.printStackTrace();
		}
    }

}

