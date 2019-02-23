package gateway;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.ViewSwitcher;
import exception.BookDeletedException;
import exception.BookModifiedException;
import model.Author;
import model.Book;

public class AuthorTableGateway {
	private Connection conn;
	
	public AuthorTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public List<Author> getAuthors() throws SQLException{
		Author author = null;
		List<Author> listOfAuthors = new ArrayList<Author>();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM author;");
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			author = new Author();
			author.setId(rs.getInt("id"));
			author.setFirstname(rs.getString("first_name"));
			author.setLastname(rs.getString("last_name"));
			author.setDateOfBirth(rs.getDate("dob").toLocalDate());
			author.setGender(rs.getString("gender"));
			author.setWebsite(rs.getString("web_site"));
			listOfAuthors.add(author);
		}
		return listOfAuthors;
	}
	public void addAuthor(Author author) throws SQLException{
		PreparedStatement st = conn.prepareStatement("INSERT INTO author (first_name, last_name, dob, gender, "
									+ "web_site) VALUES (?, ?, ?, ?, ?)");
		st.setString(1, author.getFirstname());
		st.setString(2, author.getLastname());
		st.setDate(3, Date.valueOf(author.getDateOfBirth()));
		st.setString(4, author.getGender());
		st.setString(5, author.getWebsite());
		st.executeUpdate();
	}
	public void updateAuthor(Author author) throws SQLException{
			PreparedStatement st = conn.prepareStatement("UPDATE author SET first_name = ?, last_name = ?, dob = ?, gender = ?, web_site = ? WHERE id = ? ;");
			st.setString(1, author.getFirstname());
			st.setString(2, author.getLastname());
			st.setDate(3, Date.valueOf(author.getDateOfBirth()));
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			st.setInt(6, author.getId());
			st.executeUpdate();
	}
	
	public void deleteAuthor(Author author) throws SQLException{
		PreparedStatement st = conn.prepareStatement("DELETE FROM author_book WHERE author_id = ?");
		st.setInt(1, author.getId());
		st.executeUpdate();
		st = conn.prepareStatement("DELETE FROM author WHERE id = ?");
	    st.setInt(1, author.getId());
		st.executeUpdate();
		ViewSwitcher.setAuthor(null);
	}
	
	public void setAuthorId(Author author) throws SQLException{
		PreparedStatement st = conn.prepareStatement("SELECT LAST_INSERT_ID();");
		ResultSet rs = st.executeQuery();
		if(rs.next())
			author.setId(rs.getInt("LAST_INSERT_ID()"));
	}
}
