package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import controller.ViewSwitcher;
import gateway.AuthorTableGateway;
import gateway.BookTableGateway;
import gateway.DataSource;

public class Author {
	private int id;
	private String firstname;
	private String lastname;
	private LocalDate dateOfBirth;
	private String gender;
	private String website;
	
	public Author() {
	}
	
	public void save(Author author) throws SQLException {
		Connection conn = DataSource.getDs().getConnection();
		AuthorTableGateway gateway = new AuthorTableGateway(conn);
		ViewSwitcher.setAuthor(author);
		if(author.id == 0) {
			gateway.addAuthor(author);
			gateway.setAuthorId(author);
		}
		else
			gateway.updateAuthor(author);
		conn.close();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return firstname + " " + lastname;
	}
	
}
