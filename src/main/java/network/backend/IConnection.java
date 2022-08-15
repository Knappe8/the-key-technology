package network.backend;

/**
 * 
 * Interface to handle a Clients connection and allow the usage of different Sockettypes.
 *
 */
public interface IConnection {

	/**
	 * Sends a message to the Client.
	 * @param message Message to send.
	 */
	public void sendMessage(String message);
	/**
	 * Sends a message to the Client, when the Server is shut down.
	 */
	public void disconnect();
	/**
	 * Handles a disconnecting Client.
	 */
	public void disconnected();
}
