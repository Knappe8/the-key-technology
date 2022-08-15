package process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

import util.FileUtil;

public class WordpressPost {
	/**
	 * Defines how many entries the word count map will have.
	 */
	private static final int MAX_WORDS = 15;
	
	private String content;
	private Map<String, Integer> wordCountMap;
	private JSONArray wordCountJSON;
	private int postID;
	
	/**
	 * Constructor.
	 * Creates a WordpressPost-Object from given JSON-Object.
	 * @param wordpressPost
	 */
	public WordpressPost(JSONObject wordpressPost) {
		wordCountMap = new HashMap<>();
		wordCountJSON = new JSONArray();
		content = wordpressPost.getJSONObject("content").getString("rendered");
		postID = wordpressPost.getInt("id");
	}
	
	public int getID() {
		return postID;
	}

	public String getContent() {
		return content;
	}
	
	public JSONArray getWordCountInJSON() {
		return wordCountJSON;
	}
	
	/**
	 * Removes HTML-Tags from the content of a post.
	 * @return HTML-Tag free content of a post.
	 */
	public String getSanitizedContent() {
		return Jsoup.parse(getContent()).text();
	}
	
	/**
	 * Puts the content and the Word Count Map in a JSON-Object
	 * @return JSON-Object containing the content and a Word Count Map of a post.
	 */
	public JSONObject getPostAndMapInJSON() {
		JSONObject json = new JSONObject();
		json.put("content", getContent());
		json.put("wordCountMap", getWordCountInJSON());
		return json;
	}
	
	/**
	 * Creates a Word Count Map from the content of a post.
	 * Might produce wrong results in cases of quotation marks or similar punctuations.
	 */
	public void parseContentToWordCountMap() {
		//Create a temporary file with sanitized content. 
		File f = new File("tmp.txt");
		FileUtil.writeTextToFile(getSanitizedContent(), f);
		wordCountJSON.clear();
		
		FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		try {
			//Use the before created file to load the text.
			List<WordFrequency> wordList = frequencyAnalyzer.load(f);
			int i = 0;
			for(WordFrequency wf : wordList) {
				wordCountMap.put(wf.getWord(), wf.getFrequency());
				//Only store MAX_WORDS words.
				if(i<MAX_WORDS) {
					JSONObject json = new JSONObject();
					json.put("frequency",wf.getFrequency());
					json.put("word", wf.getWord());
					wordCountJSON.put(json);
					++i;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Delete the temporary file.
		FileUtil.deleteFile(f);
	}
	
}
