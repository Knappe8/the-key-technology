package process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WordpressPostTest {

	private static String JSON_WP_Post;
	private static WordpressPost wpPost;
	@BeforeAll
	public static void setUp() {
		JSON_WP_Post = "{\"id\":12, \"content\":{\"rendered\":\"<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?\"}}";
		wpPost = new WordpressPost(new JSONObject(JSON_WP_Post));
	}
	public Map<String, Integer> getWordCountMap() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = WordpressPost.class.getDeclaredField("wordCountMap");
		f.setAccessible(true);
		return  (Map<String, Integer>) f.get(wpPost);
	}
	
	@Test
	public void createInvalidPost() {
		assertThrows(NullPointerException.class, () -> new WordpressPost(null)) ;
	}
	
	@Test
	public void getMethods() {
		assertEquals(wpPost.getID(), 12);
		assertEquals(wpPost.getContent(), "<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?");
	}
	@Test 
	public void sanitizeText() {
		assertEquals(wpPost.getSanitizedContent(), "Dies ist mein Testtext. Dies ist wichtig um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?");
	}
	@Test
	public void parseContent() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		wpPost.parseContentToWordCountMap();
		File f = new File("tmp.txt");
		Map<String, Integer> wordCountMap = getWordCountMap();
		assertEquals(wordCountMap.get("dies"), 3);
		assertEquals(wordCountMap.get("testtext"), 2);
		assertEquals(wordCountMap.get("abrafaxe"), null);
	}
	@Test
	public void getPostAndMapInJSON() {
		wpPost.parseContentToWordCountMap();
		JSONObject json = wpPost.getPostAndMapInJSON();
		assertNotNull(json);
		assertTrue(json.has("content"));
		assertTrue(json.has("wordCountMap"));
		assertEquals(json.getString("content"), "<b>Dies</b> ist mein Testtext. <h1>Dies ist wichtig</h1> um dies zu testen. Außerdem braucht man noch zusätzliche Worte. Nun nun nun! Wohin geht die Reise in diesem TestText?");
		JSONArray wordCountMap = json.getJSONArray("wordCountMap");
		assertEquals(wordCountMap.length(), 15);
	}
	
}
