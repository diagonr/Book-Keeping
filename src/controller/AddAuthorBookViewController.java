package controller;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import confirm.ConfirmType;
import gateway.AuthorBookTableGateway;
import gateway.AuthorTableGateway;
import gateway.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Author;
import model.AuthorBook;
import model.Book;

public class AddAuthorBookViewController  {
	
	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
    @FXML
    private ComboBox<Author> comboBoxAuthor;

    @FXML
    private TextField royaltyTextField;

	private Book book;

	private AuthorBook authorBook;
    
    public AddAuthorBookViewController(Book book) {
    	authorBook = new AuthorBook();
    	this.book = book;
    	authorBook.setBook(book);
    }
    
    public AddAuthorBookViewController(AuthorBook authorBook) {
    	this.authorBook = authorBook;
    	this.book = this.authorBook.getBook();
    }
	@FXML
	public void initialize() {
		Connection conn = null;
		try {
			conn = DataSource.getDs().getConnection();
			logger.info("Connection opened. ");
			AuthorTableGateway gateway = new AuthorTableGateway(conn);
			ObservableList<Author> oListAuthor = FXCollections.observableArrayList(gateway.getAuthors());
			comboBoxAuthor.getItems().addAll(oListAuthor);
			if(authorBook.getAuthor() != null) {
				comboBoxAuthor.getSelectionModel().select(authorBook.getAuthor());
				royaltyTextField.setText("." + String.valueOf(authorBook.getRoyalty()));
			}
			else {
				comboBoxAuthor.getSelectionModel().selectFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				logger.info("Connection Closed.");
			} catch(SQLException e) {
				logger.error("SQLException Encountered.");
			}
		}
		
	}
    @FXML
    void onCancelClick(MouseEvent event) {
	    ViewSwitcher.getInstance().closeAddAuthor();
    }
    @FXML
    void onSaveClick(MouseEvent event) {
    	Author selectedAuthor = comboBoxAuthor.getSelectionModel().getSelectedItem();
    	try {
    	this.authorBook.setRoyalty(Double.valueOf(this.royaltyTextField.getText()));
    	} catch (NumberFormatException e){
    		logger.error("Invalid numeric entry entered.");
    	}
    	if(selectedAuthor != null) {
    		authorBook.setAuthor(selectedAuthor);
			Connection conn;
			try {
				conn = DataSource.getDs().getConnection();
				AuthorBookTableGateway gateway = new AuthorBookTableGateway(conn);
				gateway.addAuthorBook(authorBook);
				conn.close();
				ViewSwitcher.getInstance().refreshAuthorBookList();
			    ViewSwitcher.getInstance().closeAddAuthor();
			} catch (SQLException e) {
				try {
					conn = DataSource.getDs().getConnection();
					AuthorBookTableGateway gateway = new AuthorBookTableGateway(conn);
					gateway.updateAuthorBook(authorBook);
					conn.close();
					ViewSwitcher.getInstance().refreshAuthorBookList();
				    ViewSwitcher.getInstance().closeAddAuthor();
				} catch (SQLException e2){
					logger.error("SQL insert failed. Possibly duplicate entry?");
				}
			}
    	}
    	else {
    		logger.error("No authors within Database.");
    	    ViewSwitcher.getInstance().closeAddAuthor();
    	}
    }
}

