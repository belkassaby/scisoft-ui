/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package uk.ac.diamond.scisoft.pydev.rcp.adapters;

import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.TreeAdaptable;
import org.eclipse.dawnsci.analysis.api.tree.TreeFile;
import org.eclipse.dawnsci.analysis.tree.TreeFactory;
import org.junit.Assert;
import org.junit.Test;
import org.python.pydev.shared_interactive_console.console.codegen.IScriptConsoleCodeGenerator;
import org.python.pydev.shared_interactive_console.console.codegen.PythonSnippetUtils;

import uk.ac.diamond.scisoft.analysis.rcp.adapters.HDF5Adaptable;

/**
 * This test verifies that the adapter factory properly adapts HDF5 objects into {@link IScriptConsoleCodeGenerator}s
 * 
 * @see HDF5AdapterFactory
 */
public class HDF5AdapterFactoryPluginTest {

	private TreeAdaptable createAdaptable() {
		TreeFile file = TreeFactory.createTreeFile(123456, "/my/filename/here");
		Node node = TreeFactory.createDataNode(234566);
		NodeLink hdf5NodeLink = TreeFactory.createNodeLink("name", null, node);
		HDF5Adaptable a = new HDF5Adaptable(file.getFilename(), "/path/name", hdf5NodeLink);
		return a;
	}

	private void checkGenerator(IScriptConsoleCodeGenerator generator) {
		Assert.assertNotNull(generator);
		Assert.assertEquals(true, generator.hasPyCode());
		Assert.assertEquals("dnp.io.load('/my/filename/here')['/path/name']", generator.getPyCode());
	}

	@Test
	public void testHDF5NodeLinkAdaptsTo() {
		IScriptConsoleCodeGenerator generator = PythonSnippetUtils
				.getScriptConsoleCodeGeneratorAdapter(createAdaptable());
		checkGenerator(generator);
	}

	@Test
	public void testHDF5NodeLinkFactoryDirect() {
		IScriptConsoleCodeGenerator generator = (IScriptConsoleCodeGenerator) new HDF5AdapterFactory().getAdapter(
				createAdaptable(), IScriptConsoleCodeGenerator.class);
		checkGenerator(generator);
	}

}
