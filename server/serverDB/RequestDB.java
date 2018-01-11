import java.sql.*;
import java.util.*;

public class RequestDB implements DBRequestInterface {
	Statement state = null;
	Connection conn = null;

	public RequestDB(){
		Connect connect = new Connect();
		this.conn = connect.connectToDB();
	}

	public ArrayList<String> getGroups(String userName){
		String idPersonString = userName.replaceAll("[^0-9]", "");
		int idPerson = Integer.parseInt(idPersonString);
		ArrayList<String> groupList = new ArrayList<String>();
		try {
			String nameGrp = "";
			state = conn.createStatement();
			String sql = "SELECT nameGrp FROM MEMBER_OF WHERE idPerson = " + idPerson + " UNION SELECT nameGrp FROM TICKET WHERE idPerson = " + idPerson + ";";
			ResultSet rs = state.executeQuery(sql);
			rs.beforeFirst();
			while (rs.next()){
				nameGrp = rs.getString("nameGrp");
				groupList.add(nameGrp);
			}
			for (int i = 0; i < groupList.size(); i++){
				String groupCur = groupList.get(i);
				groupCur += "/" + countUnreadForGroup(idPerson, groupCur) + "";
				groupList.set(i, groupCur);
			}
			rs.close();
			state.close();
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Error returning groups");
			return null;
		}
		return groupList;
	}

	private int countUnreadForGroup(int idPerson, String groupName){
		try {
			state = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM MESSAGE M, RECEIVE R, TICKET T, GROUPE G WHERE R.idPerson = " + idPerson + " AND G.nameGrp = \"" + groupName + "\" AND M.idMsg = R.idMsg AND T.idTicket = M.idTicket AND G.nameGrp = T.nameGrp AND R.status = 'RECU';";
			ResultSet rs = state.executeQuery(sql);
			rs.next();
			return ((Number) rs.getObject(1)).intValue();
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Unable to count unread messages");
			return -1;
		}
	}

	public ArrayList<String> getGroupTickets(int idPerson, String groupName){
		ArrayList<String> ticketList = new ArrayList<String>();
		try {
			int ticketID;
			String ticketTitle = "", ticketInfo = ""; 
			state = conn.createStatement();
			String sql = "SELECT T.idTicket, T.title FROM PERSON P, TICKET T, GROUPE G WHERE P.idPerson = " + idPerson +" AND G.nameGrp = \"" + groupName + "\" AND G.nameGrp = T.nameGrp;";
			ResultSet rs = state.executeQuery(sql);
			rs.beforeFirst();
			while (rs.next()){
				ticketID = rs.getInt("T.idTicket");
				ticketTitle = rs.getString("T.title");
				ticketInfo = ticketID + "/" + ticketTitle;
				ticketList.add(ticketInfo);
			}
			for (int i = 0; i < ticketList.size(); i++){
				String ticketArr[] = ticketList.get(i).split("/");
				String ticketCur = ticketList.get(i);
				ticketCur += "/" + countUnreadForTicket(idPerson, Integer.parseInt(ticketArr[0])) + "";
				ticketList.set(i, ticketCur);
			}
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Error returning groups");
			return null;
		}
		return ticketList;
	}

	private int countUnreadForTicket(int idPerson, int idTicket){
		try {
			state = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM MESSAGE M, RECEIVE R, TICKET T, GROUPE G WHERE R.idPerson = " + idPerson + " AND T.idTicket = " + idTicket +" AND M.idMsg = R.idMsg AND T.idTicket = M.idTicket AND G.nameGrp = T.nameGrp AND R.status = 'RECU';";
			ResultSet rs = state.executeQuery(sql);
			rs.next();
			return ((Number) rs.getObject(1)).intValue();
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Unable to count unread messages");
			return -1;
		}
	}

	// public ArrayList<String> getTicketMessages(int idTicket){
	// 	ArrayList<String> ticketList = new ArrayList<String>();
	// 	try {
	// 		int ticketID;
	// 		String ticketTitle = "", ticketInfo = ""; 
	// 		state = conn.createStatement();
	// 		String sql = "SELECT T.idTicket, T.title FROM PERSON P, TICKET T, GROUPE G WHERE P.idPerson = " + idPerson +" AND G.nameGrp = \"" + groupName + "\" AND G.nameGrp = T.nameGrp;";
	// 		ResultSet rs = state.executeQuery(sql);
	// 		rs.beforeFirst();
	// 		while (rs.next()){
	// 			ticketID = rs.getInt("T.idTicket");
	// 			ticketTitle = rs.getString("T.title");
	// 			ticketInfo = ticketID + "/" + ticketTitle;
	// 			ticketList.add(ticketInfo);
	// 		}
	// 		for (int i = 0; i < ticketList.size(); i++){
	// 			String ticketArr[] = ticketList.get(i).split("/");
	// 			String ticketCur = ticketList.get(i);
	// 			ticketCur += "/" + countUnreadForTicket(idPerson, Integer.parseInt(ticketArr[0])) + "";
	// 			ticketList.set(i, ticketCur);
	// 		}
	// 	} catch (Exception e){
	// 		e.printStackTrace();
	// 		System.err.println("Error returning groups");
	// 		return null;
	// 	}
	// 	return ticketList;
	// }

	public static void main(String[] args) {
		RequestDB rq = new RequestDB();
		ArrayList<String> mylist = rq.getGroups(1008);
		System.out.println(mylist);
		mylist = rq.getGroupTickets(1008, "INFORMATIQUE");
		System.out.println(mylist);
	}
}