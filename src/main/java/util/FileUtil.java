package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	/**
	 * Writes Text to a given File.
	 * @param text Text to write.
	 * @param f File to write to.
	 */
	public static void writeTextToFile(String text, File f) {
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try (FileWriter fw = new FileWriter(f)){
			fw.write(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * Deletes File.
	 * @param f File to delete.
	 */
	public static void deleteFile(File f) {
		if(f.exists()) f.delete();
	}
}
