package gateway;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSource {
	
	private static MysqlDataSource ds = new MysqlDataSource();

	public static MysqlDataSource getDs() {
		return ds;
	}

	public static void setDs(MysqlDataSource ds) {
		DataSource.ds = ds;
	}
	
}
