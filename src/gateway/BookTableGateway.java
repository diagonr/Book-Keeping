package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.ViewSwitcher;
import exception.BookDeletedException;
import exception.BookModifiedException;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;
import model.Publisher;

public class BookTableGateway {
	private Connection conn;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
	}
	public List<Book> getBooks() throws SQLException{
		Book book = null;
		List<Book> listOfBooks = new ArrayList<Book>();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM book INNER JOIN publisher ON book.publisher_id = publisher.publisher_id");
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			book = new Book();
			book.setId(rs.getInt("id"));
			book.setTitle(rs.getString("title"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(rs.getInt("year_published"));
			book.setIsbn(rs.getString("isbn"));
			book.setPublisher(new Publisher(rs.getInt("publisher_id"), rs.getString("publisher_name")));
			book.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
			book.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
			listOfBooks.add(book);
		}
		return listOfBooks;
	}

	public void updateBook(Book book) throws SQLException, BookModifiedException, BookDeletedException{
		if(checkBookModified(book)) {
			auditTrailProcess(book);
			PreparedStatement st = conn.prepareStatement("UPDATE book SET title = ?, summary = ?, year_published = ?, publisher_id = ?, isbn = ? WHERE id = ? ;");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getPublisherId());
			st.setString(5, book.getIsbn());
			st.setInt(6, book.getId());
			st.executeUpdate();
		}
		else {
			throw new BookModifiedException();
		}
	}
	private void auditTrailProcess(Book book) throws SQLException {
		PreparedStatement st = conn.prepareStatement("SELECT * FROM book  "
				+ "INNER JOIN publisher ON book.publisher_id = publisher.publisher_id WHERE id = ?;");
		st.setInt(1, book.getId());
		ResultSet rs = st.executeQuery();
		rs.next();
		Book bookCheck = new Book();
		bookCheck.setId(rs.getInt("id"));
		bookCheck.setTitle(rs.getString("title"));
		bookCheck.setSummary(rs.getString("summary"));
		bookCheck.setYearPublished(rs.getInt("year_published"));
		bookCheck.setIsbn(rs.getString("isbn"));
		bookCheck.setPublisher(new Publisher(rs.getInt("publisher_id"), rs.getString("publisher_name")));
		bookCheck.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
		bookCheck.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
		if(!bookCheck.getTitle().equals(book.getTitle())) {
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, "title changed from " + bookCheck.getTitle() + " to " + book.getTitle());
			st.executeUpdate();
		}
		if(!bookCheck.getSummary().equals(book.getSummary())) {
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, "summary changed from " + bookCheck.getSummary() + " to " + book.getSummary());
			st.executeUpdate();
		}
		if(bookCheck.getYearPublished() != book.getYearPublished()) {
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, "year_published changed from " + bookCheck.getYearPublished() + " to " + book.getYearPublished());
			st.executeUpdate();
		}
		if(!bookCheck.getPublisher().equals(book.getPublisher())) {
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, "publisher changed from (" + bookCheck.getPublisher().getPublisherId() + ")" + 
													bookCheck.getPublisher().getPublisherName() + " to (" +
													book.getPublisher().getPublisherId() + ")"
												+ book.getPublisher().getPublisherName());
			st.executeUpdate();
		}
		if(!bookCheck.getIsbn().equals(book.getIsbn())) {
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, "isbn changed from " + bookCheck.getIsbn() + " to " + book.getIsbn());
			st.executeUpdate();
		}
	}
	
	public List<AuthorBook> getAuthorsForBook(Book book) throws SQLException{
		List<AuthorBook> listOfAuthorBooks = new ArrayList<AuthorBook>();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM author_book "
				+ "INNER JOIN author ON author_book.author_id = author.id "
				+ "INNER JOIN book on author_book.book_id = book.id WHERE book_id = ?;");
		st.setInt(1, book.getId());
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			AuthorBook authorBook= new AuthorBook();
			Author author = new Author();
			author.setId(rs.getInt("author_id"));
			author.setFirstname(rs.getString("first_name"));
			author.setLastname(rs.getString("last_name"));
			author.setDateOfBirth(rs.getDate("dob").toLocalDate());
			author.setGender(rs.getString("gender"));
			author.setWebsite(rs.getString("web_site"));
			authorBook.setAuthor(author);
			authorBook.setBook(book);
			authorBook.setRoyalty(rs.getBigDecimal("royalty"));
			authorBook.setNewRecord(false);
			listOfAuthorBooks.add(authorBook);
		}
		return listOfAuthorBooks;
	}
	
	public void addBook(Book book) throws SQLException{
		PreparedStatement st = conn.prepareStatement("INSERT INTO book (title, summary, year_published, publisher_id, "
									+ "isbn) VALUES (?, ?, ?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
		st.setString(1, book.getTitle());
		st.setString(2, book.getSummary());
		st.setInt(3, book.getYearPublished());
		st.setInt(4, book.getPublisher().getPublisherId());
		st.setString(5, book.getIsbn());
		st.executeUpdate();
		ResultSet rs = st.getGeneratedKeys();
		if(rs.next()) {
			book.setId(rs.getInt(1));
		}
		st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
		st.setInt(1, book.getId());
		st.setString(2, "Book Added");
		st.executeUpdate();
	}
	public void setBookId(Book book) throws SQLException{
		PreparedStatement st = conn.prepareStatement("SELECT LAST_INSERT_ID();");
		ResultSet rs = st.executeQuery();
		if(rs.next())
			book.setId(rs.getInt("LAST_INSERT_ID()"));
	}
	public void deleteBook(Book book) throws SQLException{
		PreparedStatement st = conn.prepareStatement("DELETE FROM author_book WHERE book_id = ?");
		st.setInt(1, book.getId());
		st.executeUpdate();
		st = conn.prepareStatement("DELETE FROM book WHERE id = ?");
	    st.setInt(1, book.getId());
		st.executeUpdate();
		ViewSwitcher.setBook(null);
	}
	public List<AuditTrailEntry> getBookAudit(Book book) throws SQLException{
		List<AuditTrailEntry> listOfAudits = new ArrayList<AuditTrailEntry>();
		
		PreparedStatement st = conn.prepareStatement("SELECT * FROM book_audit_trail WHERE book_id = ? ORDER BY date_added ASC");
		st.setInt(1, book.getId());
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			listOfAudits.add(new AuditTrailEntry(
					rs.getInt("book_id"),
					rs.getTimestamp("date_added"),
					rs.getString("entry_msg")));
		}
		return listOfAudits;
	}
	public boolean checkBookModified(Book book) throws SQLException, BookDeletedException{
		PreparedStatement st = conn.prepareStatement("SELECT last_modified FROM book WHERE id = ?");
		st.setInt(1, book.getId());
		ResultSet rs = st.executeQuery();
		if(rs.next())
			return book.getLastModified().equals(rs.getTimestamp("last_modified").toLocalDateTime());
		throw new BookDeletedException();
	}
}
