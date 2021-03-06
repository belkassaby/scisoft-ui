/*-
 * Copyright © 2011 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.diamond.scisoft.mappingexplorer.views;

import org.eclipse.january.DatasetException;
import org.eclipse.january.dataset.ILazyDataset;

/**
 * @author rsr31645
 */
public interface IMappingViewData {
	/**
	 * @return {@link ILazyDataset} - should essentially be a 2D or 3D dataset
	 */
	ILazyDataset getDataSet() throws DatasetException;

	/**
	 * @return the label to be attached to the first dimension
	 */
	String getDimension1Label();

	/**
	 * @return dimension values that essentially showup on the slider. The length should be the same as the first
	 *         dimension.
	 */
	double[] getDimension1Values();

}
