/*-
 * Copyright (c) 2016 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package uk.ac.diamond.scisoft.pydev.rcp.handlers;

public class PydevDebugPlottingAddLineCommandHandler extends PydevDebugPlottingCommandHandler {

	@Override
	protected String getCommand() {
		return "addline";
	}

}
