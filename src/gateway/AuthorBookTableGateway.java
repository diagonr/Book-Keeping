package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.AuthorBook;

public class AuthorBookTableGateway {
	
	private Connection conn;
	
	public AuthorBookTableGateway(Connection conn) {
		this.conn = conn;
	}
	public void addAuthorBook(AuthorBook authorBook) throws SQLException{
		PreparedStatement st = conn.prepareStatement("INSERT INTO author_book (author_id, book_id, royalty)"
				+ " VALUES (?, ?, ?)");
		st.setInt(1, authorBook.getAuthor().getId());
		st.setInt(2, authorBook.getBook().getId());
		st.setBigDecimal(3, authorBook.getBigDecimalRoyalty());
		st.executeUpdate();
		st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
		st.setInt(1, authorBook.getBook().getId());
		st.setString(2, "Author \"" + authorBook.getAuthor().getFirstname() + " " + authorBook.getAuthor().getLastname()
								+ "\" added to book \"" + authorBook.getBook().getTitle() + "\" with royalty "
								+ authorBook.getBigDecimalRoyalty());
		st.executeUpdate();
	}
	public void updateAuthorBook(AuthorBook authorBook) throws SQLException{
			PreparedStatement st = conn.prepareStatement("UPDATE author_book SET royalty = ? WHERE author_id = ? AND book_id = ?;");
			st.setBigDecimal(1, authorBook.getBigDecimalRoyalty());
			st.setInt(2, authorBook.getAuthor().getId());
			st.setInt(3, authorBook.getBook().getId());
			st.executeUpdate();
			st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
			st.setInt(1, authorBook.getBook().getId());
			st.setString(2, "Author \"" + authorBook.getAuthor().getFirstname() + " " + authorBook.getAuthor().getLastname()
									+ "\" updated with royalty " + authorBook.getBigDecimalRoyalty());
			st.executeUpdate();
	}
	
	public void deleteAuthorBook(AuthorBook authorBook) throws SQLException{
		PreparedStatement st = conn.prepareStatement("DELETE FROM author_book WHERE author_id = ? AND book_id = ?");
	    st.setInt(1, authorBook.getAuthor().getId());
	    st.setInt(2, authorBook.getBook().getId());
		st.executeUpdate();
		st = conn.prepareStatement("INSERT INTO book_audit_trail (book_id, entry_msg) VALUES (?, ?)");
		st.setInt(1, authorBook.getBook().getId());
		st.setString(2, "Author \"" + authorBook.getAuthor().getFirstname() + " " + authorBook.getAuthor().getLastname()
								+ "\" deleted from book \"" + authorBook.getBook().getTitle() + "\"");
		st.executeUpdate();
	}

}
