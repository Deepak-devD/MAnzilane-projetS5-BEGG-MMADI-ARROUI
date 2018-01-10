/*
 * initializes salts, usernames and passwords for users already in the database
 */
import java.sql.*;

public class InitDB {
	Statement state = null;
	Connection conn = null;

	public InitDB(){
		Connect connect = new Connect();
		this.conn = connect.connectToDB();
	}

	public void initUsernamesSaltAndHash(Connection conn){
		try {
			int id;
			String firstName, lastName, salt, userName, pwd, hashValue;
			state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT * FROM PERSON";
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()){
				id = rs.getInt("idPerson");
				firstName = rs.getString("firstName");
				lastName = rs.getString("lastName");
				salt = SaltGenTest.saltValueGen();
				userName = UsernameAndPassGenTest.userNameGen(lastName, firstName, id);
				pwd = UsernameAndPassGenTest.passWordGen(lastName, firstName, id);
				hashValue = SaltGenTest.hashValueGen(pwd, salt);
				rs.updateString("userName", userName);
				rs.updateString("salt", salt);
				rs.updateString("hashValue", hashValue);
				rs.updateRow();
			}
			rs.close();
			state.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InitDB init = new InitDB();
		init.initUsernamesSaltAndHash(init.conn);
	}
}