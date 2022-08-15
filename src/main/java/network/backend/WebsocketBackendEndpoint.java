package network.backend;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
/**
 * 
 * Websocket Server that listens to new Connections from frontend Clients.
 *
 */
public class WebsocketBackendEndpoint extends WebSocketServer implements IBackendEndpoint{

	private static final int PORT = 8000;
	
	private BackendServer server;
	private Map<WebSocket, IConnection> connectionMap;
	/**
	 * Constructor
	 * Starts the Server.
	 * @param server
	 */
	public WebsocketBackendEndpoint(BackendServer server) {
		//Listens to Port 8000;
		super(new InetSocketAddress(PORT));
		
		connectionMap = new HashMap<>();
		this.server = server;
		start();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("Connection");
		connectionMap.put(conn, new WebsocketConnection(conn));		
		addConnectionToServer(connectionMap.get(conn));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		server.removeConnection(connectionMap.get(conn));
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		//Nothing to show
		
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		server.removeConnection(connectionMap.get(conn));
		
	}

	@Override
	public void onStart() {
		//Nothing to show
	}

	@Override
	public void addConnectionToServer(IConnection connection) {
		server.addConnection(connection);	
	}

}
