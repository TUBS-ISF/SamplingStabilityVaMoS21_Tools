package de.tubs.isf.simpleSampling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.PairWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.RandomConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.SPLCAToolConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.twise.TWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.tubs.isf.simpleSampling.util.FeatureModelReader;
import de.tubs.isf.simpleSampling.util.FileOrganiser;

public class SimpleSampler {
	
	private String INPUTDIR = ""; 
	private String OUTPUTDIR = ""; 
	private final String MODELEXT = ".xml";
	private final String SAMPLEEXT = ".config";
	private FeatureModelReader fmReader = new FeatureModelReader();
	private int SAMPLENUMBER = 0;
	
	private FileOrganiser organizer;  

	private List<LiteralSet> sampleChv;
	private List<LiteralSet> sampleIcpl;
	private List<LiteralSet> sampleInc;
	private List<LiteralSet> sampleRan;
	private List<LiteralSet> sampleYasa;
	private Random random = new Random(0);
	
	
	public static void main(String args[]) {
		System.out.println("hello sampling");
		System.out.println("#######################");
		/**
		 * args[0] == root directory of input feature models
		 * args[1] == root directory for output samples
		 * args[2] == identifier to express how many samples where created already
		 * args[3] == random seed 
		 */
		SimpleSampler sampler = new SimpleSampler(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3])); 
	}
	
	public SimpleSampler(String inputdir, String outputdir, int sampleNumber, int randomseed) {
		this.INPUTDIR = inputdir; 
		this.OUTPUTDIR = outputdir;
		this.SAMPLENUMBER = sampleNumber;
		this.random = new Random(randomseed);
		
		 organizer = new FileOrganiser();
		 organizer.setInputPaths(INPUTDIR, MODELEXT);
		
		runSampling(organizer.getInputPaths()); 
	}
	
	private void runSampling(Map<String, Path> map){
		List<String> timestamps = new ArrayList<>(organizer.getInputPaths().keySet());
		Collections.sort(timestamps);
		for(String timestamp : timestamps) {
			Path fmPath = organizer.getInputPaths().get(timestamp); 
			System.out.println(fmPath.toString());
			sampling(fmPath, timestamp); 
		}
	}
	
	private void sampling(Path fmPath, String timeStamp) {
		IFeatureModel fm = fmReader.loadFile(fmPath);  
		CNF modelCNF = new FeatureModelFormula(fm).getCNF();
		SPLCAToolConfigurationGenerator cg;
		PairWiseConfigurationGenerator pcg;
		RandomConfigurationGenerator rcg; 
		TWiseConfigurationGenerator twcg; 
		CNF randomCNF = modelCNF.randomize(random); 
		
		
		System.out.println("Create Sample ICPL");
		cg = new SPLCAToolConfigurationGenerator(randomCNF.randomize(new Random(12)), "ICPL", 2, 1000);
		sampleIcpl = LongRunningWrapper.runMethod(cg);
		
		System.out.println("Create Sample Chvatal");
		cg = new SPLCAToolConfigurationGenerator(randomCNF, "Chvatal", 2, 1000);
		sampleChv = LongRunningWrapper.runMethod(cg);
		
		System.out.println("Create Sample IncLing");
		pcg = new PairWiseConfigurationGenerator(randomCNF, 1000);
		sampleInc = LongRunningWrapper.runMethod(pcg);
		
		System.out.println("Create Sample Yasa");
		twcg = new TWiseConfigurationGenerator(randomCNF, 2);
		sampleYasa = LongRunningWrapper.runMethod(twcg);
		
		System.out.println("Create Sample Random");
		rcg = new RandomConfigurationGenerator(randomCNF, 15); 
		sampleRan = LongRunningWrapper.runMethod(rcg);
		
		Path outIcpl = organizer.buildOutputPath(timeStamp, "icpl", SAMPLENUMBER);
		Path outChvatal = organizer.buildOutputPath(timeStamp, "chvatal", SAMPLENUMBER);;
		Path outIncling = organizer.buildOutputPath(timeStamp, "incling", SAMPLENUMBER);;
		Path outYasa = organizer.buildOutputPath(timeStamp, "yasa", SAMPLENUMBER);;
		Path outRandom = organizer.buildOutputPath(timeStamp, "random", SAMPLENUMBER);;
		
		writeSample(fm, randomCNF, sampleIcpl, outIcpl.toString());
		writeSample(fm, randomCNF, sampleChv, outChvatal.toString());
		writeSample(fm, randomCNF, sampleInc, outIncling.toString());
		writeSample(fm, randomCNF, sampleYasa, outYasa.toString());
		writeSample(fm, randomCNF, sampleRan, outRandom.toString());
	}
	
	private void writeSample(IFeatureModel fm, CNF modelCNF, List<LiteralSet> sample, String algoPath) {
		List<Configuration> configs = new ArrayList<>(); 
		FeatureModelFormula fmForm = new FeatureModelFormula(fm);
		String outPutString = OUTPUTDIR+"/"+algoPath; 
		
		for (LiteralSet literalSet : sample) {
			Configuration configuration = new Configuration(fmForm);

			for (final int selection : literalSet.getLiterals()) {
				final String name = modelCNF.getVariables().getName(selection);
				configuration.setManual(name, selection > 0 ? Selection.SELECTED : Selection.UNSELECTED);
			}
			configs.add(configuration);
		}
		
		int counter = 0;
		for (Configuration conf : configs) {
			String fileName = "";
			if (counter >= 0 && counter < 10) {
				fileName = "000" + counter + SAMPLEEXT;
			} else if (counter >= 10 && counter < 100) {
				fileName = "00" + counter + SAMPLEEXT;
			} else if (counter >= 100 && counter < 1000) {
				fileName = "0" + counter + SAMPLEEXT;
			} else {
				fileName = counter + SAMPLEEXT;
			}
			System.out.println(outPutString);
			System.out.println(fileName);

			StringBuilder configBuilder = new StringBuilder();
			for (String feature : conf.getSelectedFeatureNames()) {
				System.out.println(feature);
				if (configBuilder.length() != 0) {
					configBuilder.append("\n");
				}
				configBuilder.append(feature);
			}
			try {
				File configurationDir = new File(outPutString + "/");
				configurationDir.mkdirs();
				File configurationFile = new File(configurationDir + "/" + fileName);
				if (!configurationFile.exists()) {
					configurationFile.createNewFile();
				}
				
				FileWriter fw = new FileWriter(configurationFile);
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(configBuilder.toString());
				bw.flush();
				bw.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			counter++;
		}
	}
}
