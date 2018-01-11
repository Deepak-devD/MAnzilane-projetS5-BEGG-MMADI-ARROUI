import java.sql.*;

public class Connect {
	public Connection connectToDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/NeoCampus";
			String user = "root";
			String pwd = "";

			Connection conn = DriverManager.getConnection(url, user, pwd);
			return conn;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void disconnect(Connection conn) throws SQLException{
		conn.close();
	}
}