package cn.ebooboo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static List<String> loadFile(String fileName, String encoding) {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new RuntimeException("File not found : " + fileName);
		}
		
		List<String> results = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			// br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			if (line != null) {
				results.add(line);
			} else {
				return results;
			}
			
			while ((line=br.readLine()) != null) {
				results.add(line);
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					com.jfinal.kit.LogKit.error(e.getMessage(), e);
				}
			}
		}
	}
	
}
