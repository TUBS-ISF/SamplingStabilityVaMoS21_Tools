package runCombiner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RunCombiner {
	
	private Path inputRoot = null; 
	private Path outputRoot = null;
	
	private CSVReader reader = new CSVReader();
	
	private CSVWriter csvWriterChvatal = new CSVWriter();
	private CSVWriter csvWriterIcpl = new CSVWriter();
	private CSVWriter csvWriterIncling = new CSVWriter();
	private CSVWriter csvWriterYasa = new CSVWriter();
	private CSVWriter csvWriterRandom = new CSVWriter();
	
	public static void main(String args[]) {
		RunCombiner combiner = new RunCombiner(args[0], args[1]);
		combiner.init();
		combiner.combine();
	}
	
	public RunCombiner(String inputPath, String outputPath) {
		inputRoot = Paths.get(inputPath);
		outputRoot = Paths.get(outputPath); 
	}
	
	private void init() {
		reader.read(inputRoot);
	}
	
	private void combine() {
		List<String> csvHeader = new ArrayList<String>();
		csvHeader.add("Version1");
		csvHeader.add("Version2");
		csvHeader.add("Hungarian");
		
		this.csvWriterChvatal.setOutputPath(Paths.get(outputRoot.toString()+"/chvatal"));
		this.csvWriterChvatal.setFileName("00_combined_chvatal.csv");
		this.csvWriterChvatal.setHeader(csvHeader);
		this.csvWriterChvatal.setSeparator(",");
		
		this.csvWriterIcpl.setOutputPath(Paths.get(outputRoot.toString()+"/icpl"));
		this.csvWriterIcpl.setFileName("00_combined_icpl.csv");
		this.csvWriterIcpl.setHeader(csvHeader);
		this.csvWriterIcpl.setSeparator(",");
		
		this.csvWriterIncling.setOutputPath(Paths.get(outputRoot.toString()+"/incling"));
		this.csvWriterIncling.setFileName("00_combined_incling.csv");
		this.csvWriterIncling.setHeader(csvHeader);
		this.csvWriterIncling.setSeparator(",");
		
		this.csvWriterYasa.setOutputPath(Paths.get(outputRoot.toString()+"/yasa"));
		this.csvWriterYasa.setFileName("00_combined_yasa.csv");
		this.csvWriterYasa.setHeader(csvHeader);
		this.csvWriterYasa.setSeparator(",");
		
		this.csvWriterRandom.setOutputPath(Paths.get(outputRoot.toString()+"/random"));
		this.csvWriterRandom.setFileName("00_combined_random.csv");
		this.csvWriterRandom.setHeader(csvHeader);
		this.csvWriterRandom.setSeparator(",");
		
		for(int index = 0; index < reader.getTimeStampsA().size()-1; index++) {
			
			String timeStampA = reader.getTimeStampsA().get(index);
			String timeStampB = reader.getTimeStampsB().get(index);
			
			double resultChvatal = reader.getResultsChvatal().get(timeStampA) / reader.getDividerChvatal();
			double resultIcpl = reader.getResultsIcpl().get(timeStampA) / reader.getDividerIcpl();
			double resultIncling = reader.getResultsIncling().get(timeStampA) / reader.getDividerIncling();
			double resultYasa = reader.getResultsYasa().get(timeStampA) / reader.getDividerYasa();
			double resultRandom = reader.getResultsRandom().get(timeStampA) / reader.getDividerRandom();
			
			List<String> csvLineChvatal = new ArrayList<String>(); 
			csvLineChvatal.add(timeStampA);
			csvLineChvatal.add(timeStampB);
			csvLineChvatal.add(Double.toString(resultChvatal));
			this.csvWriterChvatal.addLine(csvLineChvatal);
			
			List<String> csvLineIcpl = new ArrayList<String>(); 
			csvLineIcpl.add(timeStampA);
			csvLineIcpl.add(timeStampB);
			csvLineIcpl.add(Double.toString(resultIcpl));
			this.csvWriterIcpl.addLine(csvLineIcpl);
			
			List<String> csvLineIncling = new ArrayList<String>(); 
			csvLineIncling.add(timeStampA);
			csvLineIncling.add(timeStampB);
			csvLineIncling.add(Double.toString(resultIncling));
			this.csvWriterIncling.addLine(csvLineIncling);
			
			List<String> csvLineYasa = new ArrayList<String>(); 
			csvLineYasa.add(timeStampA);
			csvLineYasa.add(timeStampB);
			csvLineYasa.add(Double.toString(resultYasa));
			this.csvWriterYasa.addLine(csvLineYasa);
			
			List<String> csvLineRandom = new ArrayList<String>(); 
			csvLineRandom.add(timeStampA);
			csvLineRandom.add(timeStampB);
			csvLineRandom.add(Double.toString(resultRandom));
			this.csvWriterRandom.addLine(csvLineRandom);
		}
		
		this.csvWriterChvatal.flush();
		this.csvWriterIcpl.flush();
		this.csvWriterIncling.flush();
		this.csvWriterYasa.flush();
		this.csvWriterRandom.flush();
	}

}
