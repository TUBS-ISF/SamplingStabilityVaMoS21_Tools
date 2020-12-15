package de.tubs.isf.samp.main;

import java.util.List;

import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;





public class SimpleSampler {

	private String modelDirectory = "";
	private String sampleDirectory = ""; 
	private int randomSeat = 0;
	private String sampleNumber = "0"; 
	
	private List<LiteralSet> sampleChv;
	private List<LiteralSet> sampleIcpl;
	private List<LiteralSet> sampleInc;
	private List<LiteralSet> sampleRan;
	private List<LiteralSet> sampleYasa;
	
	
	public static void main(String[] args) {
		System.out.println("Hello Sampler");
		SimpleSampler sampler = new SimpleSampler(args[0], args[1], Integer.parseInt(args[2]), args[3]); 
	}
	
	public SimpleSampler(String modelDirectory, String sampleDirectory, int randomSeat, String sampleNumber) {
		this.modelDirectory = modelDirectory; 
		this.sampleDirectory = sampleDirectory;
		this.randomSeat = randomSeat; 
		this.sampleNumber = sampleNumber; 
	}
	
	private void runSampling() {
		
	}

}
