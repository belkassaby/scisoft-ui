/*-
 * Copyright (c) 2016 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package uk.ac.diamond.scisoft.arpes.calibration.functions;

import org.eclipse.dawnsci.analysis.api.fitting.functions.IFunction;
import org.eclipse.dawnsci.analysis.api.message.DataMessageComponent;
import org.eclipse.dawnsci.analysis.dataset.impl.Image;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.fitting.functions.FermiGauss;
import uk.ac.diamond.scisoft.arpes.calibration.utils.ARPESCalibrationConstants;

public class PrepareFermiGaussianFunction {
	
	private static final Logger logger = LoggerFactory.getLogger(PrepareFermiGaussianFunction.class);
	
	private static final Integer FIT_DIRECTION = 1;
	private FermiGauss fg;

	public PrepareFermiGaussianFunction(DataMessageComponent calibrationData) {
		Integer fitDim = FIT_DIRECTION;
		// get the required datasets
		Dataset dataDS = DatasetFactory.createFromObject(calibrationData.getList(ARPESCalibrationConstants.REGION_DATANAME));
		int[] shape = dataDS.getShape();
		
		Dataset xAxisDS = DatasetFactory.createFromObject(calibrationData.getList(ARPESCalibrationConstants.ENERGY_AXIS));
		if (xAxisDS == null)
			xAxisDS = DatasetFactory.createRange(DoubleDataset.class, shape[fitDim], 0, -1);
			logger.warn("Could not find a energy axis, creating one");
		
		Double temperatureValue = 10.0;
		try {
			temperatureValue = (Double) calibrationData.getUserObject(ARPESCalibrationConstants.TEMPERATURE_PATH);
		} catch (Exception e) {
			logger.warn("Could not find a temperature value, using 10K as an estimate");
		}
		
		fg = new FermiGauss();
		
		// Mu
		Dataset smoothed = Image.gaussianBlurFilter(dataDS, 5);
		smoothed = smoothed.mean(0, true);
		double min = (double) smoothed.min(true);
		double height = (float)dataDS.max(true) - (float)dataDS.min(true);
		int crossing = Maths.abs(Maths.subtract(smoothed, (min+(height/2.0)))).minPos()[0];

		min = (double) smoothed.getSlice(new int[] {crossing+5}, new int[] {smoothed.getShape()[0]}, new int[] {1}).mean(true);
		double max = (double) smoothed.getSlice(new int[] {0}, new int[] {crossing-5}, new int[] {1}).mean(true);
		height = max - min;
		
		fg.getParameter(0).setValue(xAxisDS.getDouble(crossing));
		fg.getParameter(0).setLowerLimit((Double)xAxisDS.min(true));
		fg.getParameter(0).setUpperLimit((Double)xAxisDS.max(true));
		
		// Temperature
		fg.getParameter(1).setValue(temperatureValue);
		fg.getParameter(1).setLowerLimit(0.0);
		fg.getParameter(1).setUpperLimit(300.0);
		fg.getParameter(1).setFixed(true);
		
		// BG Slope
		fg.getParameter(2).setValue(0.0);
		fg.getParameter(2).setLowerLimit(-10000.0);
		fg.getParameter(2).setUpperLimit(10000.0);
		
		// Step Height
		fg.getParameter(3).setValue(height);
		fg.getParameter(3).setLowerLimit(0.0);
		fg.getParameter(3).setUpperLimit(height*2);
		
		// Constant
		fg.getParameter(4).setValue(min);
		fg.getParameter(4).setLowerLimit(0.0);
		fg.getParameter(4).setUpperLimit((Float)dataDS.min(true)*2);
		
		// FWHM
		fg.getParameter(5).setValue(0.001);
		fg.getParameter(5).setLowerLimit(0.001);
		fg.getParameter(5).setUpperLimit(0.1);
		
		fg.setName(fg.getName());

	}

	public IFunction getPreparedFunction() {
		return fg;
	}
}
