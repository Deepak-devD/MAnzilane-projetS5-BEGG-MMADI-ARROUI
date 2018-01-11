import java.util.*;

public interface DBRequestInterface {
	public ArrayList<String> getGroups(int idPerson);
	public ArrayList<String> getGroupTickets(int idPerson, String groupName);
	// public String[] getTicketMessages(int idTicket);
}