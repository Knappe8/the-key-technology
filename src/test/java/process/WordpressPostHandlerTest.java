package process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WordpressPostHandlerTest {

	private static WordpressPostHandler wpPostHandler;
	private static String JSON_WP_Post1 = "{\"id\":12, \"content\":{\"rendered\":\"<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?\"}}";
	private static String JSON_WP_Post2 = "{\"id\":13, \"content\":{\"rendered\":\"<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?\"}}";
	private static String JSON_WP_Post3 = "{\"id\":14, \"content\":{\"rendered\":\"<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?\"}}";
	private static String JSON_WP_Post4 = "{\"id\":13, \"content\":{\"rendered\":\"<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?\"}}";
	private static JSONArray jsonA1;
	private static JSONArray jsonA2;
	private static JSONArray jsonA3;
	
	@BeforeAll
	public static void setUp() {
		JSONObject json1 = new JSONObject(JSON_WP_Post1);
		JSONObject json2 = new JSONObject(JSON_WP_Post2);
		JSONObject json3 = new JSONObject(JSON_WP_Post3);
		JSONObject json4 = new JSONObject(JSON_WP_Post4);
		jsonA1 = new JSONArray();
		{
			jsonA1.put(json1);
			jsonA1.put(json2);
		}
		jsonA2 = new JSONArray();
		{
			jsonA2.put(json1);
			jsonA2.put(json2);
			jsonA2.put(json4);
		}
		jsonA3 = new JSONArray();
		{
			jsonA3.put(json1);
			jsonA3.put(json2);
			jsonA3.put(json3);
		}
	}
	@BeforeEach
	public  void reset() {
		wpPostHandler = new WordpressPostHandler();
	}
	
	@Test
	public void retrievePosts() {
		boolean newPost = wpPostHandler.retrieveWordpressPosts(jsonA1.toString());
		assertTrue(newPost);
		newPost = wpPostHandler.retrieveWordpressPosts(jsonA1.toString());
		assertFalse(newPost);
		newPost = wpPostHandler.retrieveWordpressPosts(jsonA2.toString());
		assertFalse(newPost);
	}
	@Test 
	public void retrieveNewPosts() {
		boolean newPost = wpPostHandler.retrieveWordpressPosts(jsonA1.toString());
		assertTrue(newPost);
		newPost = wpPostHandler.retrieveWordpressPosts(jsonA3.toString());
		assertTrue(newPost);
	}
	@Test
	public void getPostsAsJSON() {
		assertEquals(wpPostHandler.getPostsAsJSON(), "[]");
		wpPostHandler.retrieveWordpressPosts(jsonA2.toString());
		JSONArray json = new JSONArray(wpPostHandler.getPostsAsJSON());
		assertEquals(json.length(), 2);
		assertTrue( json.getJSONObject(0).has("content"));
	}
	
}
