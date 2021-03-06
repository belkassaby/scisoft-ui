/*-
 * Copyright (c) 2016 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package uk.ac.diamond.scisoft.pydev.rcp.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.dawnsci.analysis.api.tree.TreeAdaptable;
import org.python.pydev.shared_interactive_console.console.codegen.IScriptConsoleCodeGenerator;
import org.python.pydev.shared_interactive_console.console.codegen.PythonSnippetUtils;

@SuppressWarnings("rawtypes")
public class HDF5AdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IScriptConsoleCodeGenerator.class) {
			if (adaptableObject instanceof TreeAdaptable) {
				final TreeAdaptable adaptee = (TreeAdaptable) adaptableObject;
				final String filename = adaptee.getFile();

				if (filename == null || filename.isEmpty())
					return null;

				return new IScriptConsoleCodeGenerator() {
	
					@Override
					public String getPyCode() {
						return "dnp.io.load(" + PythonSnippetUtils.getSingleQuotedString(filename) + ")["
								+ PythonSnippetUtils.getSingleQuotedString(adaptee.getNode()) + "]";
					}
	
					@Override
					public boolean hasPyCode() {
						return true;
					}
				};
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IScriptConsoleCodeGenerator.class };
	}

}
