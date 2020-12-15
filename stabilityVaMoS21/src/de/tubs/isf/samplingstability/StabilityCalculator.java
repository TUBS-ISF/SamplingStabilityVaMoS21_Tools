package de.tubs.isf.samplingstability;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.tubs.isf.samplingstability.analysis.StabEvaluator;
import de.tubs.isf.samplingstability.util.CSVWriter;
import de.tubs.isf.samplingstability.util.InputOrganizer;
import de.tubs.isf.samplingstability.util.Sample;

public class StabilityCalculator {

	private String modelRoot = "";
	private String sampleRoot = "";
	private String csvRoot = "";
	private String algo = "";
	private String sampNum = "";

	private final CSVWriter csvWriter = new CSVWriter();
	private final InputOrganizer organizer = new InputOrganizer();

	public static void main(String args[]) {
		System.out.println("hello sampling stability");
		/*
		 * args[0] == model root args[1] == sample root args[2] == csv root args[3] ==
		 * algo args[4] == sample number
		 */
		StabilityCalculator stabCalc = new StabilityCalculator(args[0], args[1], args[2], args[3], args[4]);

	}

	public StabilityCalculator(String modelRoot, String sampleRoot, String csvRoot, String algo, String sampNum) {
		this.modelRoot = modelRoot;
		this.sampleRoot = sampleRoot;
		this.csvRoot = csvRoot;
		this.algo = algo;
		this.sampNum = sampNum;

		init();
		analyze();
	}

	private void init() {
		organizer.organizeModels(modelRoot, ".xml");
		organizer.organizeSamples(sampleRoot, ".config", algo, sampNum);

		// init csvWriter
		csvWriter.setOutputPath(Paths.get(csvRoot));
		csvWriter.setFileName(algo + sampNum + ".csv");
		// set csv Header
		List<String> csvHeader = new ArrayList<String>();
		csvHeader.add("Version1");
		csvHeader.add("Version2");
		csvHeader.add("Hungarian");
		csvWriter.setHeader(csvHeader);

		csvWriter.setSeparator(",");
	}

	private void analyze() {
		List<String> timestamps = organizer.getTimestampList(); 
		for (int i = 0; i <= timestamps.size() - 2; i++) {
			String timestampA = timestamps.get(i);
			String timestampB = timestamps.get(i+1);
			// tuple 1
			IFeatureModel fmA = organizer.getFm(timestampA); 
			Sample sampleA = organizer.getSample(timestampA);

			// tuple 2
			IFeatureModel fmB = organizer.getFm(timestampB); 
			Sample sampleB = organizer.getSample(timestampB);

			StabEvaluator evaluator = new StabEvaluator(fmA, sampleA, fmB, sampleB);
			double hgResult = evaluator.calculateSampleSim("hungarian");
			
			// heart beat output
			System.out.println("Calculating " + timestamps.get(i) + " VS. " + timestamps.get(i+1));
			System.out.println("Result hg: " + hgResult);
			System.out.println("");
			
			// store results in writer
			List<String> csvLine = new ArrayList<String>(); 
			csvLine.add(timestamps.get(i));
			csvLine.add(timestamps.get(i+1));
			csvLine.add(Double.toString(hgResult));
			csvWriter.addLine(csvLine);
		}
		// write results to file
		csvWriter.flush();
	}
}
