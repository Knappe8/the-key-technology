package process;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * Handler that manages the posts of the Wordpress site and recognizes if new posts are added.
 *
 */
public class WordpressPostHandler {


	private Map<Integer, WordpressPost> postList;
	public WordpressPostHandler() {
		postList = new HashMap<>();
	}
	/**
	 * Retrieves Wordpress Posts from given JSON-Text.
	 * Determines if new posts were added to the list.
	 * @param postsInJsonFormat Wordpress Posts in JSON-Format.
	 * @return true if new posts were added.
	 */
	public boolean retrieveWordpressPosts(String postsInJsonFormat) {
		//Posts are in JSONArray-Form.
		JSONArray postListJSON = new JSONArray(postsInJsonFormat);
		boolean newValues = false;
		for(int i = 0; i<postListJSON.length(); ++i) {
			//Get single Post in JSON-Format.
			JSONObject postJSON = postListJSON.getJSONObject(i);
			//Check if the Post-ID already exists in the stored Post-List.
			if(!postList.containsKey(postJSON.get("id"))) {
				//Convert JSON-Post to Java WordpressPost.
				WordpressPost wpPost = new WordpressPost(postJSON);
				wpPost.parseContentToWordCountMap();
				//Add Post to Post-List.
				postList.put(wpPost.getID(), wpPost);
				newValues = true;
			}
		}
		
		return newValues;
	}
	
	/**
	 * Gathers information from all Wordpress-Posts and stores them in a JSON-Format.
	 * @return Information about all Wordpress-Posts in JSON-Format.
	 */
	public String getPostsAsJSON() {
		JSONArray postListJSON = new JSONArray();
		for(Integer postID : postList.keySet()) {
			postListJSON.put(postList.get(postID).getPostAndMapInJSON());
		}
		return postListJSON.toString();
	}
}
