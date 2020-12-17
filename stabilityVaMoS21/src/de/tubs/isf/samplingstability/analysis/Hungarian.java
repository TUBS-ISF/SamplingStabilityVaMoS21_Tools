package de.tubs.isf.samplingstability.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.ovgu.featureide.fm.core.io.manager.IFeatureModelManager;

public class Hungarian extends MSOC {

	@Override
	public double analyze(IFeatureModelManager fm1, List<List<String>> sample1List, IFeatureModelManager fm2,
			List<List<String>> sample2List) {
		this.sampleMap_old = generateSampleMap(SampleListToSet(sample1List));
		this.sampleMap_new = generateSampleMap(SampleListToSet(sample2List));

		combinedFeatureSet = buildCombinedFS(fm1, fm2);

		Set<Integer> keySet1 = new HashSet<>();
		keySet1.addAll(sampleMap_old.keySet());

		Set<Integer> keySet2 = new HashSet<>();
		keySet2.addAll(sampleMap_new.keySet());
		double[][] costArray = new double[keySet1.size()][keySet2.size()];
		for (int key1 : keySet1) {
			for (int key2 : keySet2) {
				double confSim = calcConfSim(sampleMap_old.get(key1), sampleMap_new.get(key2));
				costArray[key1 - 1][key2 - 1] = confSim;
			}
		}
//		printTwoDimArray("Base Array:", costArray);
		int[][] result = HungarianAlgorithm.hgAlgorithm(costArray, "max");
//		System.out.println("Result");
		for (int[] row : result) {
			if((row[0] == -1)) {
				ConfigurationPair pair = new ConfigurationPair(-1, row[1], 0);
				pairList.add(pair);
			}
			else if((row[1] == -1)) {
				ConfigurationPair pair = new ConfigurationPair(row[0], -1, 0);
				pairList.add(pair);
			}
			else {
				ConfigurationPair pair = new ConfigurationPair(row[0], row[1], costArray[row[0]][row[1]]);
				pairList.add(pair);
//				System.out.println(Arrays.toString(row));
			}
		}
		
		Set<Integer> copyKSet1 = new HashSet<Integer>();
		Set<Integer> copyKSet2 = new HashSet<Integer>();
		Set<Integer> copyPSet1 = new HashSet<Integer>();
		Set<Integer> copyPSet2 = new HashSet<Integer>();
		
		copyKSet1.addAll(keySet1); 
		copyKSet2.addAll(keySet2); 
		for(ConfigurationPair cp : pairList) {
			copyPSet1.add(cp.getKey1()); 
			copyPSet2.add(cp.getKey2()); 
		}
		
		copyKSet1.removeAll(copyPSet1); 
		copyKSet2.removeAll(copyPSet2); 
		
		for(Integer i :  copyKSet1) {
			ConfigurationPair pair = new ConfigurationPair(i,-1,0); 
			pairList.add(pair); 
		}
		for(Integer j :  copyKSet2) {
			ConfigurationPair pair = new ConfigurationPair(-1,j,0); 
			pairList.add(pair); 
		}
		
//		DoubleSummaryStatistics stats = Arrays.stream(hungarianArray).flatMapToDouble(Arrays::stream)
//				.summaryStatistics();
//		double[][] normalizedHungarianArray = hungarianArray;
//
//		for (int row = 0; row < hungarianArray.length; row++) {
//			for (int col = 0; col < hungarianArray[row].length; col++) {
//				normalizedHungarianArray[row][col] = stats.getMax() - hungarianArray[row][col];
//			}
//		}
//
//		printTwoDimArray("Normalized Hungarian Array:", normalizedHungarianArray);
//		
//		double[] colMins = new double[normalizedHungarianArray[0].length];
//		for (int i = 0; i < colMins.length; i++) {
//			colMins[i] = stats.getMax();
//		}
//		for (int row = 0; row < normalizedHungarianArray.length; row++) {
//			for (int col = 0; col < normalizedHungarianArray[row].length; col++) {
//				if (colMins[col] > normalizedHungarianArray[row][col]) {
//					colMins[col] = normalizedHungarianArray[row][col];
//				}
//			}
//		}
//		System.out.println("Col Mins: " + Arrays.toString(colMins));
//
//		for (int row = 0; row < normalizedHungarianArray.length; row++) {
//			for (int col = 0; col < normalizedHungarianArray[row].length; col++) {
//				normalizedHungarianArray[row][col] = normalizedHungarianArray[row][col] - colMins[col];
//			}
//		}
//		printTwoDimArray("Column reduced by ColMin:", normalizedHungarianArray);
//
//
//		double[] rowMins = new double[normalizedHungarianArray.length];
//		for (int i = 0; i < rowMins.length; i++) {
//			rowMins[i] = stats.getMax();
//		}
//		for (int row = 0; row < normalizedHungarianArray.length; row++) {
//			for (int col = 0; col < normalizedHungarianArray[row].length; col++) {
//				if (rowMins[row] > normalizedHungarianArray[row][col]) {
//					rowMins[row] = normalizedHungarianArray[row][col];
//				}
//			}
//		}
//		System.out.println("Row Mins:" + Arrays.toString(rowMins));
//
//		for (int row = 0; row < normalizedHungarianArray.length; row++) {
//			for (int col = 0; col < normalizedHungarianArray[row].length; col++) {
//				normalizedHungarianArray[row][col] = normalizedHungarianArray[row][col] - rowMins[row];
//			}
//		}
//		printTwoDimArray("Rows reduced by ColMin:", normalizedHungarianArray);
		return simAgregation(pairList); 
	}

	private void printTwoDimArray(String msg, double[][] twoDimArray) {
		System.out.println(msg);
		for (double[] row : twoDimArray) {
			System.out.println(Arrays.toString(row));
		}
	}
}
