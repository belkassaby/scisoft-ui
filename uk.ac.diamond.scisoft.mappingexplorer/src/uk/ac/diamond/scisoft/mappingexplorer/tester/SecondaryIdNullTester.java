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
package uk.ac.diamond.scisoft.mappingexplorer.tester;

import org.eclipse.core.expressions.PropertyTester;

import uk.ac.diamond.scisoft.mappingexplorer.views.twod.IMappingDataControllingView;

/**
 * @author rsr31645
 * 
 */
public class SecondaryIdNullTester extends PropertyTester {

	/**
	 * 
	 */
	public SecondaryIdNullTester() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		if (receiver instanceof IMappingDataControllingView) {
			String secondaryId = ((IMappingDataControllingView) receiver)
					.getViewSite().getSecondaryId();
			if (secondaryId != null) {
				return false;
			}
			if (((IMappingDataControllingView) receiver).getMappingViewData() == null) {
				return false;
			}
		}
		return true;
	}

}
