package runCombiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {

	private List<String> timeStampsA = new ArrayList<>();
	private List<String> timeStampsB = new ArrayList<>();

	private Map<String, Double> resultsChvatal = new HashMap<String, Double>();
	private Map<String, Double> resultsIcpl = new HashMap<String, Double>();
	private Map<String, Double> resultsIncling = new HashMap<String, Double>();
	private Map<String, Double> resultsYasa = new HashMap<String, Double>();
	private Map<String, Double> resultsRandom = new HashMap<String, Double>();

	private int dividerChvatal = 1;
	private int dividerIcpl = 1;
	private int dividerIncling = 1;
	private int dividerYasa = 1;
	private int dividerRandom = 1;

	public CSVReader() {
	}

	public void read(Path inputDir) {
		for (File file : inputDir.toFile().listFiles()) {
			if (file.isDirectory()) {
				String algoName = file.getName();
				readAlog(file, algoName);
			}
		}
	}

	private void readAlog(File file, String algoName) {
		for (File csvFiles : file.listFiles()) {
			readCSV(csvFiles, algoName);
		}
		
		if (algoName.equals("chvatal")) {
			dividerChvatal = file.listFiles().length; 
		}
		if (algoName.equals("icpl")) {
			dividerIcpl = file.listFiles().length;
		}
		if (algoName.equals("incling")) {
			dividerIncling = file.listFiles().length;
		}
		if (algoName.equals("yasa")) {
			dividerYasa = file.listFiles().length;
		}
		if (algoName.equals("random")) {
			dividerRandom = file.listFiles().length;
		}
	}

	private List<String[]> readCSV(File csvFile, String algoName) {
		List<String[]> csvLines = new ArrayList<>();
		try {
			FileReader reader = new FileReader(csvFile);
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine(); 
			while ((line = br.readLine()) != null) {
//				if(!line.contains("version")) {
					handleLineContent(line, algoName);
//				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvLines;
	}

	private void handleLineContent(String line, String algoName) {
		String[] lineContent = null;
		lineContent = line.split(",");
		if (!timeStampsA.contains(lineContent[0])) {
			timeStampsA.add(lineContent[0]);
		}

		if (!timeStampsB.contains(lineContent[1])) {
			timeStampsB.add(lineContent[1]);
		}

		if (algoName.equals("chvatal")) {
			double prevResult = 0; 
			if(resultsChvatal.get(lineContent[0]) != null) {
				prevResult = resultsChvatal.get(lineContent[0]); 
			}
			double value = prevResult + Double.parseDouble(lineContent[2]);
			resultsChvatal.put(lineContent[0], value); 
		}
		if (algoName.equals("icpl")) {
			double prevResult = 0; 
			if(resultsIcpl.get(lineContent[0]) != null) {
				prevResult = resultsIcpl.get(lineContent[0]); 
			}
			double value = prevResult + Double.parseDouble(lineContent[2]); 
			resultsIcpl.put(lineContent[0], value);
		}
		if (algoName.equals("incling")) {
			double prevResult = 0; 
			if(resultsIncling.get(lineContent[0]) != null) {
				prevResult = resultsIncling.get(lineContent[0]); 
			}
			double value = prevResult + Double.parseDouble(lineContent[2]);
			resultsIncling.put(lineContent[0], value);
		}
		if (algoName.equals("yasa")) {
			double prevResult = 0; 
			if(resultsYasa.get(lineContent[0]) != null) {
				prevResult = resultsYasa.get(lineContent[0]); 
			}
			double value = prevResult + Double.parseDouble(lineContent[2]);
			resultsYasa.put(lineContent[0], value);
		}
		if (algoName.equals("random")) {
			double prevResult = 0; 
			if(resultsRandom.get(lineContent[0]) != null) {
				prevResult = resultsRandom.get(lineContent[0]); 
			}
			double value = prevResult + Double.parseDouble(lineContent[2]);
			resultsRandom.put(lineContent[0], value);
		}
	}

	/**
	 * @return the timeStampsA
	 */
	public List<String> getTimeStampsA() {
		return timeStampsA;
	}

	/**
	 * @return the timeStampsB
	 */
	public List<String> getTimeStampsB() {
		return timeStampsB;
	}

	/**
	 * @return the resultsChvatal
	 */
	public Map<String, Double> getResultsChvatal() {
		return resultsChvatal;
	}

	/**
	 * @return the resultsIcpl
	 */
	public Map<String, Double> getResultsIcpl() {
		return resultsIcpl;
	}

	/**
	 * @return the resultsIncling
	 */
	public Map<String, Double> getResultsIncling() {
		return resultsIncling;
	}

	/**
	 * @return the resultsYasa
	 */
	public Map<String, Double> getResultsYasa() {
		return resultsYasa;
	}

	/**
	 * @return the resultsRandom
	 */
	public Map<String, Double> getResultsRandom() {
		return resultsRandom;
	}

	/**
	 * @return the dividerChvatal
	 */
	public int getDividerChvatal() {
		return dividerChvatal;
	}

	/**
	 * @return the dividerIcpl
	 */
	public int getDividerIcpl() {
		return dividerIcpl;
	}

	/**
	 * @return the dividerIncling
	 */
	public int getDividerIncling() {
		return dividerIncling;
	}

	/**
	 * @return the dividerYasa
	 */
	public int getDividerYasa() {
		return dividerYasa;
	}

	/**
	 * @return the dividerRandom
	 */
	public int getDividerRandom() {
		return dividerRandom;
	}

}
