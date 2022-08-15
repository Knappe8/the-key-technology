package network.process;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import network.wordpress.WPPostRetriever;

public class WPPostRetrieverTest {

	private static WPPostRetriever wpPostRetriever;
	private static WPPostRetriever wpPostRetrieverFaulty;
	@BeforeAll
	public static void createWPPostRetriever() {
		wpPostRetriever = new WPPostRetriever("https://www.internate.org");
		wpPostRetrieverFaulty = new WPPostRetriever("fjghdk");
	}
	@Test
	public void retrievePosts() {
		assertNotEquals(wpPostRetriever.getPosts().length(), 0);
	}
	@Test
	public void testFaultyConnection() {
		assertThrows(IllegalStateException.class, ()->wpPostRetrieverFaulty.getPosts());
	}
	
	//TODO test SanitizeString
}
