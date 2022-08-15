package network.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BackendServerTest {

	private static BackendServer server;
	private static ConnectionMock connection1;
	private static ConnectionMock connection2;
	private static ConnectionMock connection3;
	private static int numMessagesSent = 0;
	
	@BeforeAll
	public static void setUp() {
		connection1 = new ConnectionMock();
		connection2 = new ConnectionMock();
		connection3 = new ConnectionMock();
	}
	
	public List<IConnection> getClientsList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = BackendServer.class.getDeclaredField("clients");
		f.setAccessible(true);
		return (List<IConnection>) f.get(server);
	}
	@BeforeEach
	public void reset() {
		server = new BackendServer();
		numMessagesSent = 0;
	}
	
	@Test
	public void addConnectionsToServer() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		server.addConnection(connection1);
		server.addConnection(connection2);
		List<IConnection> clientList = getClientsList();
		assertEquals(clientList.size(), 2);
		server.addConnection(connection1);
		assertEquals(clientList.size(), 2);
	}
	@Test
	public void removeConnectionsFromServer() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		server.addConnection(connection1);
		server.addConnection(connection2);
		server.removeConnection(connection1);
		List<IConnection> clientList = getClientsList();
		assertEquals(clientList.size(), 1);
		server.removeConnection(connection3);
		assertEquals(clientList.size(), 1);
	}
	
	@Test
	public void sendMessageAtConnection() {
		server.broadCastMessage("miau");
		server.addConnection(connection1);
		assertEquals(connection1.lastMessage, "miau");
	}
	@Test
	public void broadcastMessage() {
		server.addConnection(connection1);
		server.addConnection(connection2);
		assertEquals(numMessagesSent, 2);
		server.broadCastMessage("hello");
		assertEquals(connection1.lastMessage, "hello");
		assertEquals(connection2.lastMessage, "hello");
		assertNotEquals(connection3.lastMessage, "hello");
		assertEquals(numMessagesSent, 4);
		server.removeConnection(connection1);
		server.broadCastMessage("hallo");
		assertEquals(numMessagesSent, 5);
	}
	
	public static class ConnectionMock implements IConnection {
		public String lastMessage;
		@Override
		public void sendMessage(String message) {
			this.lastMessage = message;
			++numMessagesSent;
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
}
