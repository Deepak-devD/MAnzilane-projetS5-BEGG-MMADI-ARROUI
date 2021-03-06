package server_fonction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import main_server.Server;

public class ClientsServerSocketInit {
	//contiendera la liste des client leur identifiant et leur flux de sortie
	private Map<String, PrintWriter> listeDeClient = new HashMap<>(); 
	private int nbClient = 0;
	// On initialise des valeurs par défaut
	private int port = 2345;
	private String host = "127.0.0.1";
	private ServerSocket server = null;
	private boolean isRunning = true;
	private Server main_server = null;
	public ClientsServerSocketInit(Server main_server) {
		this.main_server = main_server;
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClientsServerSocketInit(String host, int port, Server main_server) {
		this.main_server = main_server;
		this.host = host;
		this.port = port;
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// On lance notre serveur

	public void open() {
		// Toujours dans un thread à part vu qu'il est dans une boucle infinie
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (isRunning == true) {
					try {
						// On attend une connexion d'un client
						Socket client = server.accept();
						ClientServerCommunication newClient = 
								new ClientServerCommunication(client);
						//on utilise cette fonction pour pouvoir utiliser les fonction de maniere
						//globale dans la gestion des flux client server
						indiquerServer(newClient);
						// Une fois reçue, on la traite dans un thread séparé
						System.out.println("Connexion cliente reçue.");
						Thread t = new Thread(newClient);
						t.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
					server = null;
				}
			}
		});
		t.start();
	}
	
	
	
	/**
	 * @param idClient
	 * @param out
	 * @return la position d'ajoue du client
	 */
	synchronized public int addClient(String idClient,PrintWriter out) {
		nbClient++;
		listeDeClient.put(idClient, out);
		return listeDeClient.size();
	}
	
	/**
	 * supprime un client deconnecter
	 * @param idClient
	 */
	synchronized public void dellClient(String idClient) {
		if (listeDeClient.containsKey(idClient)) {
			listeDeClient.remove(idClient);
			nbClient--;
		}
	}
	
	/**
	 * permet de faire connaitre le server au differrent thread qui gere les cleint 
	 * @param client
	 */
	private void indiquerServer(ClientServerCommunication client) {
		client.setServer(this);
	}
	
	public String create_Ticket(Msg msg) {
		String idGroupe = msg.getIdGroup();
		String idTicket = createIdTicket();
		String titleOfTicket = msg.getTitle();
		System.out.println("idGroupe :"+idGroupe+"\ntitre :"+titleOfTicket);
		if (main_server.pushTicket(idGroupe, idTicket, titleOfTicket)) {	
			return "ok";
		}
		return "ko";
	}
	
	
	private String createIdTicket() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		isRunning = false;
	}

	public String answar_back(Msg msg) {
		String idTicket = msg.getIdTicket();
		String contenue = msg.getContenue();
		if (main_server.updateTicket(idTicket, contenue)) {
			return "ok";
		}
		return "ko";
	}

	/**
	 * @param id
	 * @param pwd
	 * @return
	 */
	public boolean login(String id, String pwd) {
		return !main_server.userIsExist(id, pwd);
	}

	/**
	 * @param idClient
	 */
	public void updateInfos(String idClient) { //on lui envoie juste la liste des groupes
		//on lui signifie que c'est bon il est connecter 
		//avec des indice dessus lui signifiant le nombre de notfication qu'il y a dans chaque
		PrintWriter writer = listeDeClient.get(idClient);
		String toSend = "USER CONNECTED";
		writer.write(toSend);
		toSend = main_server.getListOfGroup(idClient); 
		writer.write(toSend);
		writer.flush();// à voir je ne connait pas trop son utilisation 
	}
	
	
	/**
	 * avec un thread à part on traite l'envoie des notifications au différent client
	 * une fois un ticket creé ou un msg repondu 
	 * 
	 * @param notification : la notification à envoyer
	 * @param Client : nobre de client recevant cette notification 
	 */
	public void sendNotification(String notification, PrintWriter ...client) {
		Thread envoyerNotification = new Thread(new Runnable() {
			
			public void run() {
				for (int i = 0; i < client.length; i++) {
					client[i].write(notification);
				}
			}
		});
		envoyerNotification.start();
	}
}
