package de.tubs.isf.simpleSampling.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileOrganiser {

	Map<String, Path> inputPathMap = new HashMap<>(); 
	
	public FileOrganiser() {
		
	}
	
	public void setInputPaths(String input, String extension){
		Path path = Paths.get(input); 
		for(File file : path.toFile().listFiles()){
			if(file.isDirectory()) {
				setInputPaths(file.getAbsolutePath(), extension);
			}
			else {
				if(file.getName().endsWith(extension)) {
					String timestamp = file.getParentFile().getName(); 
					inputPathMap.put(timestamp, Paths.get(file.getAbsolutePath())); 
				}
			}
		}
	}
	
	public Path buildOutputPath(String timeStamp, String algo, int number) {
		String outStr = timeStamp + "/" + algo + "/" + number;
		return Paths.get(outStr); 
	}
	
	public Map<String, Path> getInputPaths() {
		return this.inputPathMap; 
	}
}
