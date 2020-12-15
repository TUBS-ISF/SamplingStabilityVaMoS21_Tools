package de.tubs.isf.samplingstability.analysis;

import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.manager.IFeatureModelManager;
import de.tubs.isf.samplingstability.util.Sample;

public class StabEvaluator {
	
	/** The feature model of the new samples. */
	private IFeatureModelManager fmNew;
	/** The feature model of the old samples. */
	private IFeatureModelManager fmOld;
	/** The samples of model after a change. */
	private Sample sampleNew = null;
	/** The samples of model before a change. */
	private Sample sampleOld = null;
	
	/**
	 * Creates a new stability evaluator.
	 * 
	 * @param fmOld     Old model.
	 * @param sampleOld Old sample.
	 * @param fmNew     New model.
	 * @param sampleNew New sample.
	 */
	public StabEvaluator(IFeatureModel fmOld, Sample sampleOld, IFeatureModel fmNew,
			Sample sampleNew) {
		this.fmOld = FeatureModelManager.getInstance(fmOld);
		this.sampleOld = sampleOld.omitNegatives();
		this.fmNew = FeatureModelManager.getInstance(fmNew);
		this.sampleNew = sampleNew.omitNegatives();
	}
	
	/**
	 * Transform the given configuration into a list a list of strings.
	 * 
	 * @param c Configuration that should be transformed.
	 * @return List containing all selected and deselected feature of a
	 *         configuration.
	 */
	private List<String> confToString(Configuration c) {
		List<String> list = new ArrayList<>();
		for (IFeature sf : c.getSelectedFeatures()) {
			list.add(sf.getName());
		}
		return list;
	}
	
	public double calculateSampleSim(String metric) {
		double result = -1;
		if(metric.equals("hungarian")) {
			Hungarian hg = new Hungarian(); 
			result = hg.analyze(fmOld, sampleOld, fmNew, sampleNew);
		}
		return result; 
	}

}
