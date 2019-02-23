package gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.AuditTrailEntry;

public class BookAuditTrailGateway {
	private Connection conn;
	
	public BookAuditTrailGateway(Connection conn) {
		this.conn = conn;
	}
	
	public List<AuditTrailEntry> getAudits() throws SQLException{
		List<AuditTrailEntry> listOfAudits = new ArrayList<AuditTrailEntry>();
		
		PreparedStatement st = conn.prepareStatement("SELECT * FROM book_audit_trail");
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			listOfAudits.add(new AuditTrailEntry(
					rs.getInt("book_id"),
					rs.getTimestamp("date_added"),
					rs.getString("entry_msg")));
		}
		return listOfAudits;
	}
}
