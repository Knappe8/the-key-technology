package network.backend;

/**
 * 
 * Interface for Socket Endpoint.
 * Allows different Types of Sockets to be used.
 *
 */
public interface IBackendEndpoint {

	/**
	 * Adds a new Client to the current Servers connectionList.
	 * @param connection new Client.
	 */
	void addConnectionToServer(IConnection connection);
	
}
