package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.ViewSwitcher;
import exception.BookDeletedException;
import exception.BookModifiedException;
import exception.InvalidBookException;
import gateway.BookTableGateway;
import gateway.DataSource;

public class Book {
	private static Logger logger = LogManager.getLogger(ViewSwitcher.class);
	private int id;
	private String title;
	private String summary;
	private int yearPublished;
	private String isbn;
	private LocalDateTime dateAdded;
	private LocalDateTime lastModified;
	private Publisher publisher;
	
	public Book() {	
	}
	
	public Book(int id, String title, String summary, int yearPublished, String isbn, LocalDateTime dateAdded
						, LocalDateTime lastModified) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.yearPublished = yearPublished;
		this.isbn = isbn;
		this.dateAdded = dateAdded;
		this.lastModified = lastModified;
	}
	public List<AuthorBook> getAuthors(Book book) throws SQLException{
		Connection conn = DataSource.getDs().getConnection();
		BookTableGateway gateway = new BookTableGateway(conn);
		ViewSwitcher.setBook(book);
		List<AuthorBook> listOfAuthorBooks = new ArrayList<AuthorBook>();
		listOfAuthorBooks = gateway.getAuthorsForBook(book);
		conn.close();
		return listOfAuthorBooks;
	}
	public void save(Book book) throws InvalidBookException, SQLException, BookModifiedException, BookDeletedException{
		if(validTitle(this.title) && validSummary(this.summary)
				&& validYearPublished(this.yearPublished) && validIsbn(this.isbn)) {
			Connection conn = DataSource.getDs().getConnection();
			BookTableGateway gateway = new BookTableGateway(conn);
			ViewSwitcher.setBook(book);
			if(book.id == 0) {
				gateway.addBook(book);
				gateway.setBookId(book);
			}
			else
				gateway.updateBook(book);
			conn.close();
		} else {
			throw new InvalidBookException();
		}
	}
	private boolean validTitle(String title) {
		return title != null && title.length() > 0 && title.length() < 256;
	}
	private boolean validSummary(String summary) {
		return summary != null && summary.length() < 65537;
	}
	private boolean validYearPublished(int yearPublished) {
		return yearPublished <= Calendar.getInstance().get(Calendar.YEAR);
	}
	private boolean validIsbn(String isbn) {
		return isbn != null && isbn.length() > 0 && isbn.length() < 14;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getYearPublished() {
		return yearPublished;
	}
	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public LocalDateTime getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return getTitle();
	}
	
}
