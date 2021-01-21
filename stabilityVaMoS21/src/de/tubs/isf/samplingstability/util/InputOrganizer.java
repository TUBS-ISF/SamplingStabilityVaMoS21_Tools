package de.tubs.isf.samplingstability.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

public class InputOrganizer {

	/**
	 * Key: time stamp of feature models; Value: path to feature model
	 */
	private Map<String, IFeatureModel> featureModelMap = new HashMap<String, IFeatureModel>();
	public Map<String, IFeatureModel> getFeatureModelMap() {
		return featureModelMap;
	}
	
	private Map<String, Sample> sampleMap = new HashMap<String, Sample>();
	public Map<String, Sample> getSampleMap() {
		return sampleMap;
	}
	
	private List<String> timestampList = new ArrayList<>();
	public List<String> getTimestampList() {
		return timestampList;
	}

	private FeatureModelReader fmReader = new FeatureModelReader();

	public InputOrganizer() {
		
	}
	
	public void organizeModels(String input, String extension){
		Path path = Paths.get(input); 
		for(File file : path.toFile().listFiles()){
			if(file.isDirectory()) {
				organizeModels(file.getAbsolutePath(), extension);
			}
			else {
				if(file.getName().endsWith(extension)) {
					IFeatureModel fm = fmReader.loadFile(Paths.get(file.getAbsolutePath())); 
					String timestamp = file.getParentFile().getName(); 
					featureModelMap.put(timestamp, fm); 
					timestampList.add(timestamp); 
					Collections.sort(timestampList);
				}
			}
		}
	}
	
	public void organizeSamples(String dir, String extension, String algo, String sampleNum) {
		Path path = Paths.get(dir);
		for(File file : path.toFile().listFiles()) {
			Sample sample = new Sample();
			String timestamp = file.getName();
			if(!timestamp.endsWith(".tar.gz")) {
				String algoPath = file.getAbsolutePath()+"\\"+algo; 
				File algoFile = new File(algoPath);
				File sampleFile = algoFile.listFiles()[Integer.parseInt(sampleNum)]; 

				for (File conf : sampleFile.listFiles()) {
					if (conf.getName().endsWith(extension)) {
						List<String> configuration = readContent(conf.toPath());
						sample.add(configuration);
					}
				}
				sampleMap.put(timestamp, sample);
			}
		}
	}
	
	

//	public void fillDirectoryMaps(Path modelRoot, Path sampleRoot) {
//		File systemDir = systemDirPath.toFile();
//		for (File subfile : systemDir.listFiles()) {
//			if (subfile.getName().equals("models")) {
//				mapTimestamps(subfile, featureModelMap);
//			} else if (subfile.getName().equals("samples")) {
//				mapTimestamps(subfile, sampleMap);
//			}
//		}
//
//	}

//	private void mapTimestamps(File directory, Map<String, Path> map) {
//		if ((this.system.equals("busybox") || this.system.equals("test")) && directory.getName().equals("models")) {
//			for (File yearDir : directory.listFiles()) {
//				for (File timestampDir : yearDir.listFiles()) {
//					if (timestampDir.isDirectory()) {
//						String timeStamp = timestampDir.getName();
//						Path timeStampPath = timestampDir.toPath();
//						map.put(timeStamp, timeStampPath);
//					}
//				}
//			}
//		} else {
//			for (File timestampDir : directory.listFiles()) {
//				if (timestampDir.isDirectory()) {
//					String timeStamp = timestampDir.getName();
//					Path timeStampPath = timestampDir.toPath();
//					map.put(timeStamp, timeStampPath);
//				}
//			}
//		}
//	}

//	public Sample getsample(String timeStamp, String algo, int sampNum) {
//		Sample sample = new Sample();
//		String sampleDirString = sampleMap.get(timeStamp).toString();
//		if (algo.equals("icpl")) {
//			sampleDirString += "/icpl";
//		} else if (algo.equals("chvatal")) {
//			sampleDirString += "/chvatal";
//		} else if (algo.equals("incling")) {
//			sampleDirString += "/incling";
//		} else if (algo.equals("yasa")) {
//			sampleDirString += "/YASA";
//		} else if (algo.equals("random")) {
//			sampleDirString += "/random"; 
//		}
//		File productsDirectory = new File(sampleDirString).listFiles()[sampNum];
//
//		for (File conf : productsDirectory.listFiles()) {
//			if (conf.getName().endsWith(".config")) {
//				List<String> configuration = readContent(conf.toPath());
//				sample.add(configuration);
//			}
//		}
//		return sample;
//	}

//	public IFeatureModel getFeatureModel(String timeSatmp) {
//		IFeatureModel fm = null;
//		File fmDir = featureModelMap.get(timeSatmp).toFile();
//		for (File fmFile : fmDir.listFiles()) {
//			if (fmFile.getName().endsWith(".xml")) {
//				fm = fmReader.loadFile(fmFile.toPath());
//			}
//		}
//		return fm;
//	}

	private List<String> readContent(Path confPath) {
		List<String> configuration = new ArrayList<>();
		try {
			configuration = Files.readAllLines(confPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configuration;
	}

	public List<String> getTimeStamps() {
		List<String> timeStamps = new ArrayList<String>();
		timeStamps.addAll(featureModelMap.keySet());
		Collections.sort(timeStamps);
		return timeStamps;
	}

	public IFeatureModel getFm(String timeStamp) {
		return this.featureModelMap.get(timeStamp); 
	}

	public Sample getSample(String timeStamp) {
		return this.sampleMap.get(timeStamp);
	}

}
