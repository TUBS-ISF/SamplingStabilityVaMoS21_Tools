package de.tubs.isf.samplingstability.analysis;

import java.util.List;

import de.ovgu.featureide.fm.core.io.manager.IFeatureModelManager;

public interface IStabilityMetric {

	public double analyze(IFeatureModelManager fm1, List<List<String>> sample1List, IFeatureModelManager fm2,
			List<List<String>> sample2List);

}
