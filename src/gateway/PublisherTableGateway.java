package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Publisher;

public class PublisherTableGateway {
	private Connection conn;
	
	public PublisherTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public List<Publisher> getPublishers() throws SQLException{
		List<Publisher> listOfPublishers = new ArrayList<Publisher>();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM publisher");
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			listOfPublishers.add(new Publisher(rs.getInt("publisher_id"), rs.getString("publisher_name")));
		}
		return listOfPublishers;
	}
	
}
