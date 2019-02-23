package model;

import java.math.BigDecimal;

public class AuthorBook {

	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	
	public AuthorBook() {
		this.newRecord = true;
	}
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getRoyalty() {
		return royalty;
	}
	public void setRoyalty(BigDecimal royalty) {
		this.royalty = royalty.multiply(BigDecimal.valueOf(100000)).intValue();
	}
	public void setRoyalty(double royalty) {
		this.royalty = (int)(royalty * 100000);
	}
	public BigDecimal getBigDecimalRoyalty() {
		return BigDecimal.valueOf(royalty).movePointLeft(5);
	}
	public boolean isNewRecord() {
		return newRecord;
	}
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	@Override
	public String toString() {
		return author + " - " + royalty/1000 + "%";
	}
	
	
}
