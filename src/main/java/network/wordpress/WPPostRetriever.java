package network.wordpress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class WPPostRetriever {

	/**
	 * Wordpress Posts API endpoint without host.
	 */
	private static final String WP_API_POSTS_ENDPOINT = "/wp-json/wp/v2/posts";
	/**
	 * Host name
	 */
	private String website;

	/**
	 * Constructor. Sets the host name.
	 * 
	 * @param website host name of foreign wordpress site.
	 */
	public WPPostRetriever(String website) {
		this.website = website;
	}

	/**
	 * Returns a list of all posts given by the API endpoint in json format.
	 * 
	 * @return list of all posts given by the API endpoint in json format.
	 * @throws IllegalStateException thrown when there is no connection to the
	 *                               server possible or the API doesn't respond with
	 *                               content.
	 */
	public String getPosts() throws IllegalStateException {
		Optional<HttpURLConnection> oConn = connectToUrl();
		// Prüfen ob Verbindung möglich war.
		if (!oConn.isPresent())
			throw new IllegalStateException("Verbindung zu " + website + " nicht möglich.");
		Optional<String> content = getContentFromConnection(oConn.get());
		// Prüfen ob API content zurückgegeben hat.
		if (!content.isPresent())
			throw new IllegalStateException(website + " liefert keinen Content zurück.");
		return sanitizeString(content.get());
	}

	/**
	 * Connects to the API-Endpoint. Returns the connection.
	 * 
	 * @return Connection to the API-Endpoint
	 */
	private Optional<HttpURLConnection> connectToUrl() {
		HttpURLConnection conn = null;
		try {

			URL url = new URL(website + WP_API_POSTS_ENDPOINT);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

		} catch (Exception e) {
			e.printStackTrace();
		}
		Optional<HttpURLConnection> oConn = Optional.ofNullable(conn);
		return oConn;
	}

	/**
	 * Returns the content the API-Endpoint returned as string.
	 * 
	 * @param conn Connection to the API-Endpoint.
	 * @return Content the API-Endpoint returned.
	 */
	private Optional<String> getContentFromConnection(HttpURLConnection conn) {
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null;) {
				result.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Optional<String> oResult = Optional.ofNullable(result.toString());
		return oResult;
	}

	/**
	 * Removes useless texts from the start of a given text.
	 * Does not guarantee a the text is valid json!
	 * @param toSanitize texts that needs to be sanitized. 
	 * @return sanitized text. Does not guarantee valid json!
	 */
	private String sanitizeString(String toSanitize) {
		return toSanitize.substring(toSanitize.indexOf("[{\"id\""));
	}
}
