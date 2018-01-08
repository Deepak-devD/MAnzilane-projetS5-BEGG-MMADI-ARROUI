
public class MainClient {

	public static void main(String[] args) {
	  
      String host = "127.0.0.1";
      int port = 2345;
      
      ClientConnexion cl = new ClientConnexion(host, port);
      for(int i = 0; i < 1; i++){
         Thread t = new Thread(cl);
         t.start();
      }
   }
}