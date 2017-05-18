/*-
 * Copyright 2017 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.diamond.scisoft.analysis.rcp.hdf5;

import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class HDF5TreeDialog extends Dialog {

	private Tree tree;
	private String filename;
	
	protected HDF5TreeDialog(Shell parentShell, Tree tree, String filename) {
		super(parentShell);
		this.tree = tree;
		this.filename = filename;
	}
	
	@Override
	public Control createDialogArea(Composite parent)  {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		HDF5TableTree tt = new HDF5TableTree(container, null, null, null);
		tt.setFilename(filename);
		tt.setInput(tree.getNodeLink());

		return container;
	}

	@Override
	protected Point getInitialSize() {
		Rectangle bounds = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell().getBounds();
		return new Point((int)(bounds.width*0.8),(int)(bounds.height*0.8));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("NeXus/HDF5 Tree");
	}

}
