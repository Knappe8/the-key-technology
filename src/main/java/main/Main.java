package main;

import org.json.JSONArray;

import network.backend.BackendServer;
import network.wordpress.WPPostRetriever;
import process.WordpressPostHandler;

public class Main {

	/**
	 * URL to foreign wordpress host.
	 */
	private static final String FOREIGN_SERVER = "https://www.internate.org";
	
	public static void main(String[] args) {
		final BackendServer server = new BackendServer();
		final WordpressPostHandler  wpPostHandler = new WordpressPostHandler();
		final WPPostRetriever wpPosts = new WPPostRetriever(FOREIGN_SERVER);

		//Thread polls for wordpress posts every 5 seconds.
		//Broadcasts if new posts where found.
		new Thread() {
			public void run() {
				while(true) {
					
					if(wpPostHandler.retrieveWordpressPosts(wpPosts.getPosts())) {
						server.broadCastMessage(wpPostHandler.getPostsAsJSON());
					}
					try {
						sleep(5_000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}

}
