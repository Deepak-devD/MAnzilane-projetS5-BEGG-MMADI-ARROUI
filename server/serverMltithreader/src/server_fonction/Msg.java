package server_fonction;

public class Msg {
	// balise nous permetant d'identifier la demande (type de msg
	private String msg;
	private int indice = 1;
	public Msg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	//pour l'instant on se contente de ça pour l'exemple 
	/**
	 * @return type de demande que le client à envoyer
	 */
	public TypeOfRequest getTypeOfmsg() {
		String c = "";
		c += msg.charAt(0);
		switch (c) {
		case "#":
			return TypeOfRequest.AUTHENTIFICATION;
		case "$":
			return TypeOfRequest.CREATE_TICKET;
		case "@":
			return TypeOfRequest.ANSWAR;
		case "*":
			return TypeOfRequest.CLOSE_CONN;
		}
		return null;
	}
	
	/**
	 * @param indice : get the pointer where we can get the passWord
	 * @return idUser
	 */
	public String get(int indice, char balise) {
		String id = "";
		while (msg.charAt(indice) != balise) {
			id += msg.charAt(indice++);
		}
		return id;
	}

	/**
	 * @return
	 */
	public String getIdGroup() {
		String idGroup = "";
		while (msg.charAt(indice) != '/') {
			idGroup += msg.charAt(indice++);
		}
		indice++;
		return idGroup;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		String title = "";
		while (msg.charAt(indice) != '#') {
			title += msg.charAt(indice++);
		}
		indice++;
		return title;
	}

	/**
	 * @return
	 */
	public String getIdTicket() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public String getContenue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public String getLogin() {
		String id = "";
		while (msg.charAt(indice) != '/') {
			id += msg.charAt(indice++);
		}
		indice++;
		return id;
	}

	/**
	 * @return
	 */
	public String getPwd() {
		String pwd = "";
		while (msg.charAt(indice) != '#') {
			pwd += msg.charAt(indice++);
		}
		return pwd;
	}
	
	// si c'est le moment de la demande de connxion on va devoir extraire le mot de
	// passe
	// et l'identifiant une fois justifier il va falloire les mettre à jour
}
