package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import alert.InvalidParametersAlert;
import confirm.AddBookConfirm;
import confirm.ChangeBookViewConfirm;
import confirm.ConfirmType;
import confirm.DeleteBookConfirm;
import confirm.SaveBookConfirm;
import exception.BookDeletedException;
import exception.BookModifiedException;
import exception.InvalidBookException;
import gateway.AuthorTableGateway;
import gateway.BookAuditTrailGateway;
import gateway.BookTableGateway;
import gateway.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;

public class ViewSwitcher {
	private static ViewSwitcher instance = null;
	
	private static Logger logger = LogManager.getLogger(ViewSwitcher.class);
	
	private static Book book = new Book();
	
	private static AuthorBook authorBook = new AuthorBook();
	
	private static AuditTrailEntry audit = new AuditTrailEntry();
	
	private boolean bookSelected = false;
	
	private static Author author = new Author();
	
	private BookDetailViewController currentBookController;
	
	private ViewSwitcher() {
		menuBorderPane = null;

	}
	@FXML
	private BorderPane menuBorderPane;
	
	public void switchView(int viewType) {
		if(viewType == 1) {
			Connection conn = null;
			bookSelected = false;
			try {
				URL viewURL = this.getClass().getResource("/view/BookListView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				conn = DataSource.getDs().getConnection();
				logger.info("Connection opened. ");
				BookTableGateway gateway = new BookTableGateway(conn);
				ObservableList<Book> oListBook = FXCollections.observableArrayList(gateway.getBooks());
				loader.setController(new BookListViewController(oListBook));
				Parent contentPane = loader.load();
				menuBorderPane.setLeft(contentPane);
				menuBorderPane.setCenter(null);
			} catch(IOException e) {
				logger.error("IOException Encountered.");
			} catch (SQLException e) {
				logger.error("SQLException Encountered.");
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
		if(viewType == 2) {
			if(bookSelected) {
					try {
						currentBookController = (BookDetailViewController) menuBorderPane.getCenter().getUserData();
						if (currentBookController != null && currentBookController.getbookChanged()) {
							ConfirmType userAnswer = confirmDialog("Change");
							if(userAnswer == ConfirmType.YES) {
								try {
									currentBookController.initiateSave();
								} catch (InvalidBookException e) {
									InvalidParametersAlert.show(book);
									logger.error("Book parameters are invalid. Please enter valid parameters.");
									return;
								}
							}
							else if(userAnswer == ConfirmType.CANCEL) {
								return;
							}		
						}
					} catch (NullPointerException e) {
					}
			}
			try {
				URL viewURL = this.getClass().getResource("/view/BookDetailView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				currentBookController = new BookDetailViewController(book);
				loader.setController(currentBookController);
				Parent contentPane = loader.load();
				menuBorderPane.setCenter(contentPane);
				bookSelected = true;
			} catch(IOException e) {
				logger.error("IOException Encountered.");
			}
		}
		if(viewType == 3) {
			Connection conn = null;
			try {
				URL viewURL = this.getClass().getResource("/view/AuditTrailView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				conn = DataSource.getDs().getConnection();
				logger.info("Connection opened. ");
				BookTableGateway gateway = new BookTableGateway(conn);
				ObservableList<AuditTrailEntry> oListAudit = FXCollections.observableArrayList(gateway.getBookAudit(book));
				loader.setController(new AuditTrailController(oListAudit));
				Parent contentPane = loader.load();
				bookSelected = true;
				menuBorderPane.setCenter(contentPane);
			} catch(IOException e) {
				logger.error("IOException Encountered.");
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
		if (viewType == 4) {
			Connection conn = null;
			try {
				URL viewURL = this.getClass().getResource("/view/AuthorListView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				conn = DataSource.getDs().getConnection();
				logger.info("Connection opened. ");
				AuthorTableGateway gateway = new AuthorTableGateway(conn);
				ObservableList<Author> oListAuthor = FXCollections.observableArrayList(gateway.getAuthors());
				loader.setController(new AuthorListViewController(oListAuthor));
				Parent contentPane = loader.load();
				menuBorderPane.setLeft(contentPane);
				menuBorderPane.setCenter(null);
			} catch(IOException e) {
				logger.error("IOException Encountered.");
			} catch (SQLException e) {
				logger.error("SQLException Encountered.");
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
		if(viewType == 5) {
			try {
				URL viewURL = this.getClass().getResource("/view/AuthorDetailView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				loader.setController(new AuthorDetailViewController(author));
				Parent contentPane = loader.load();
				menuBorderPane.setCenter(contentPane);
				bookSelected = true;
			} catch(IOException e) {
				e.printStackTrace();
				logger.error("IOException Encountered.");
			}
		}
		if(viewType == 6) {
			try {
				URL viewURL = this.getClass().getResource("/view/AddAuthorBookView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				loader.setController(new AddAuthorBookViewController(book));
				Parent contentPane = loader.load();
				menuBorderPane.setBottom(contentPane);
				bookSelected = true;
			} catch(IOException e) {
				e.printStackTrace();
				logger.error("IOException Encountered.");
			}
		}
		if(viewType == 7) {
			try {
				URL viewURL = this.getClass().getResource("/view/AddAuthorBookView.fxml");
				FXMLLoader loader = new FXMLLoader(viewURL);
				loader.setController(new AddAuthorBookViewController(authorBook));
				Parent contentPane = loader.load();
				menuBorderPane.setBottom(contentPane);
				bookSelected = true;
			} catch(IOException e) {
				e.printStackTrace();
				logger.error("IOException Encountered.");
			}
		}
	}
	public void closeAddAuthor() {
		menuBorderPane.setBottom(null);
	}
	public void refreshAuthorBookList() {
		currentBookController.refreshList();
	}
	public ConfirmType confirmDialog(String confirmDialogType) {
		if(confirmDialogType.equals("Add")) {
			return AddBookConfirm.show(book);
		} else if(confirmDialogType.equals("Save")) {
			return SaveBookConfirm.show(book);
		} else if(confirmDialogType.equals("Change")) {
			return ChangeBookViewConfirm.show(book);
		} else { // User tries to delete
			return DeleteBookConfirm.show(book);
		}
	}
	
	public static ViewSwitcher getInstance() {
		if(instance == null) {
			instance = new ViewSwitcher();
		}
		return instance;
	}
	
	public void setMenuBorderPane(BorderPane rootBorderPane) {
		this.menuBorderPane = rootBorderPane;
	}

	public static Book getBook() {
		return book;
	}

	public static void setBook(Book book) {
		ViewSwitcher.book = book;
	}

	public static AuditTrailEntry getAudit() {
		return audit;
	}

	public static void setAudit(AuditTrailEntry audit) {
		ViewSwitcher.audit = audit;
	}

	public Author getAuthor() {
		return author;
	}

	public static void setAuthor(Author author) {
		ViewSwitcher.author = author;
	}
	public static AuthorBook getAuthorBook() {
		return authorBook;
	}
	public static void setAuthorBook(AuthorBook authorBook) {
		ViewSwitcher.authorBook = authorBook;
	}
}
