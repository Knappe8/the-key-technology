package network.backend;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * The BackendServer manages the Backendpoint where new Clients connect to as well as the connections to these clients.
 * When new posts are found he broadcasts those posts to all connected Clients.
 *
 */
public class BackendServer {

	private List<IConnection> clients;
	private IBackendEndpoint endPoint;
	private String lastBroadcastMessage = "[]";
	
	/**
	 * Default Constructor
	 */
	public BackendServer() {
		clients = new ArrayList<>();
		endPoint = new WebsocketBackendEndpoint(this);
	}
	
	/**
	 * Broadcasts a message to all connected Clients.
	 * @param message Message to broadcast.
	 */
	public synchronized void broadCastMessage(String message) {
		for(IConnection connection : clients) {
			connection.sendMessage(message);
		}
		lastBroadcastMessage = message;
	}
	
	/**
	 * Adds new Client to the list of connected clients.
	 * Also sends the list of the most recent found posts.
	 */
	public synchronized void addConnection(IConnection connection) {
		if(clients.contains(connection)) return;
		clients.add(connection);
		connection.sendMessage(lastBroadcastMessage);
	}
	
	/**
	 * Removes a Client from the list of connected clients.
	 * @param connection Client to remove.
	 */
	public synchronized void removeConnection(IConnection connection) {
		clients.remove(connection);
	}
}
