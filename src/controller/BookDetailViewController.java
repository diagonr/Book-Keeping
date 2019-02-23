package controller;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alert.DeletedBookAlert;
import alert.InvalidParametersAlert;
import alert.ModifiedBookAlert;
import confirm.ConfirmType;
import exception.BookDeletedException;
import exception.BookModifiedException;
import exception.InvalidBookException;
import gateway.AuthorBookTableGateway;
import gateway.BookTableGateway;
import gateway.DataSource;
import gateway.PublisherTableGateway;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.AuthorBook;
import model.Book;
import model.Publisher;

public class BookDetailViewController {
	
	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
    @FXML
    private TextField textTitle;

    @FXML
    private TextArea textSummary;

    @FXML
    private TextField textYear;

    @FXML
    private TextField textISBN;

    @FXML
    private TextField textDate;
    
    @FXML
    private TextField textLastModified;
    
    @FXML
    private ComboBox<Publisher> comboBoxPublisher;

    @FXML
    private Button auditButton;

    @FXML
    private Button addAuthorButton;

    @FXML
    private Button editRoyaltyButton;

    @FXML
    private Button deleteAuthorButton;
    
    @FXML
    private ListView<AuthorBook> authorList;
    
    private Book book;
    
    public BookDetailViewController(Book book) {
    	this.book = book;
    }

    @FXML
    void itemClick(MouseEvent event) {

    }
    @FXML
    void onAddAuthorClick(MouseEvent event) {
    	ViewSwitcher.getInstance().switchView(6);
    }

    @FXML
    void onEditRoyaltyClick(MouseEvent event) {
    	try {
    	ViewSwitcher.setAuthorBook(authorList.getSelectionModel().getSelectedItem());
    	ViewSwitcher.getInstance().switchView(7);
    	} catch (NullPointerException e) {
    		
    	}
    }
    @FXML
    void onDeleteAuthorClick(MouseEvent event) {
    	if(authorList.getSelectionModel().getSelectedItem() != null) {
			ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Delete");
			if(userAnswer == ConfirmType.YES) {
				try {
					Connection conn = DataSource.getDs().getConnection();
					AuthorBookTableGateway gateway = new AuthorBookTableGateway(conn);
					gateway.deleteAuthorBook(authorList.getSelectionModel().getSelectedItem());
					conn.close();
					ViewSwitcher.getInstance().refreshAuthorBookList();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("SQL insert failed. Possibly duplicate entry?");
				}
			}
    	}
    }
    public void refreshList() {
    	authorList.getItems().clear();
    	try {
			authorList.getItems().addAll(book.getAuthors(book));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    void onAuditClick(MouseEvent event) {
    	if(getbookChanged()) {
	    	ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Change");
	    	if(userAnswer == ConfirmType.YES) {
	    		try {
	    			initiateSave();
	    			ViewSwitcher.getInstance().switchView(3);
	    		} catch (InvalidBookException e) {
	    			InvalidParametersAlert.show(book);
	    		}
	    	} else if(userAnswer == ConfirmType.NO) {
	    		ViewSwitcher.getInstance().switchView(3);
	    	} else {
	    		event.consume();
	    	}
	    } else {
	    	ViewSwitcher.getInstance().switchView(3);
	    }
    }
	@FXML
    void onSaveClick(MouseEvent event) {
		ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Save");
		if(userAnswer == ConfirmType.YES) {
			try {
				initiateSave();
				ViewSwitcher.getInstance().switchView(1);
			} catch (InvalidBookException e) {
				InvalidParametersAlert.show(book);
			}
		} else if(userAnswer == ConfirmType.NO) {
			ViewSwitcher.getInstance().switchView(1);
		}
    }
	@FXML
	void onDeleteClick(MouseEvent event) {
		ConfirmType userAnswer = ViewSwitcher.getInstance().confirmDialog("Delete");
		if(userAnswer == ConfirmType.YES) {
			try {
				Connection conn = DataSource.getDs().getConnection();
				BookTableGateway gateway = new BookTableGateway(conn);
				gateway.deleteBook(this.book);
				logger.info("Delete successful.");
				conn.close();
			} catch (SQLException e) {
				logger.error("Delete Failed: " + e.getMessage());
			}
			ViewSwitcher.getInstance().switchView(1);
		}
	}
    @FXML
    public void initialize(){
    	textTitle.setText(book.getTitle());
    	textSummary.setText(book.getSummary());
    	textYear.setText(String.valueOf(book.getYearPublished()));
    	textISBN.setText(book.getIsbn());
    	textDate.setText(String.valueOf(book.getDateAdded()));
    	textLastModified.setText(String.valueOf(book.getLastModified()));
    	textDate.setDisable(true);
    	textLastModified.setDisable(true);
    	try {
        	authorList.getItems().addAll(book.getAuthors(book));
			Connection conn = DataSource.getDs().getConnection();
			logger.info("Connection opened.");
			PublisherTableGateway gateway = new PublisherTableGateway(conn);
			comboBoxPublisher.getItems().addAll(gateway.getPublishers());
			if(book.getPublisher() == null)
				comboBoxPublisher.getSelectionModel().selectFirst();
			else
				comboBoxPublisher.getSelectionModel().select(book.getPublisher());
			conn.close();
			logger.info("Connection Closed.");
		} catch (SQLException e) {
			logger.error("SQLException Encountered.");
			e.printStackTrace();
		}
    }
	public void initiateSave() throws InvalidBookException {
		try {
			this.book.setTitle(textTitle.getText());
			this.book.setSummary(textSummary.getText());
			this.book.setYearPublished(Integer.parseInt(textYear.getText()));
			this.book.setIsbn(textISBN.getText());
			this.book.setPublisher(comboBoxPublisher.getValue());
			book.save(this.book);
			logger.info("Save Successful.");
		} catch (NumberFormatException e){
			logger.error("Book parameters are invalid. Please enter valid parameters.");
		}catch (InvalidBookException e) {
			logger.error("Book parameters are invalid. Please enter valid parameters.");
			throw e;
		} catch (SQLException e) {
			logger.error("Save failed: " + e.getMessage());
		} catch (BookModifiedException e) {
			// TODO Display save failure error via JavaFX Alert
			ModifiedBookAlert.show(book);
			logger.error("Book was modified. Please update book.");
		} catch (BookDeletedException e) {
			// TODO Display deleted book error via JavaFX Alert
			DeletedBookAlert.show(book);
			logger.error("Book was deleted.");
		}
	}
    public boolean getbookChanged() {
		return !textTitle.getText().equals(book.getTitle())
				|| !textSummary.getText().equals(book.getSummary())
				|| !textYear.getText().equals(String.valueOf(book.getYearPublished()))
				|| !textISBN.getText().equals(book.getIsbn())
				|| !comboBoxPublisher.getValue().equals(book.getPublisher());
    }
    
    public void disableAudit() {
    	auditButton.setDisable(true);
    }
    public void enableAudit() {
    	auditButton.setDisable(false);
    }
	public void disableAuthorEdits() {
		this.addAuthorButton.setDisable(true);
		this.editRoyaltyButton.setDisable(true);
		this.deleteAuthorButton.setDisable(true);
		this.auditButton.setDisable(true);
	}
}
