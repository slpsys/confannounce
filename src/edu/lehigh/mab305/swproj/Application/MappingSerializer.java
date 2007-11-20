package edu.lehigh.mab305.swproj.Application;

import java.util.HashMap;
import java.io.*;

public class MappingSerializer {
	
	public static HashMap<String, String> readMappingFile(InputStream in) {
		HashMap<String, String> map = null;
		if (in != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				String line = reader.readLine();
				map = new HashMap<String, String>();
				while (line != null) {
					if (line.contains(",")) {
						map.put(line.substring(0, line.indexOf(",")), line.substring(line.indexOf(",") + 1));
					}
					line = reader.readLine();
				}
			}
			catch (IOException ie) {
				
			}
		}
		return map;
	}
	
	public static void writeMappingFile(OutputStream out, HashMap<String, String> map) {
		if (out != null && map != null) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			try {
				for (String key: map.keySet()) {
					writer.write(key + "," + map.get(key) + "\n");
				}
				writer.flush();
			}
			catch (IOException ie) {
				
			}
		}
	}
}
