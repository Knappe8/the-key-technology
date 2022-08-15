package network.backend;

import org.java_websocket.WebSocket;

/**
 * 
 * Handles a Clients connection to the Server.
 *
 */
public class WebsocketConnection implements IConnection{

	private WebSocket connection;
	/**
	 * Constructor
	 * Sets the connection to the new Client.
	 * @param connection Connection to the new Client.
	 */
	public WebsocketConnection(WebSocket connection) {
		this.connection = connection;
	}
	@Override
	public void sendMessage(String message) {
		if(connection.isOpen())
			connection.send(message);	
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub
		
	}

}
