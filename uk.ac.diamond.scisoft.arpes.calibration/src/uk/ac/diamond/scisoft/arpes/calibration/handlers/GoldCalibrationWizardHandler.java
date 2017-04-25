/*-
 * Copyright (c) 2016 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package uk.ac.diamond.scisoft.arpes.calibration.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dawnsci.datavis.api.IDataFilePackage;
import org.dawnsci.datavis.api.IDataPackage;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import uk.ac.diamond.scisoft.arpes.calibration.wizards.GoldCalibrationWizard;

public class GoldCalibrationWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		ISelection selection = HandlerUtil.getActiveSite(event).getWorkbenchWindow().getSelectionService().getSelection("org.dawnsci.datavis.view.parts.LoadedFilePart");
		IDataPackage suitableData = getSuitableData(selection);
		
		GoldCalibrationWizard wiz = new GoldCalibrationWizard();
		wiz.init(PlatformUI.getWorkbench(), suitableData);
		wiz.setNeedsProgressMonitor(true);
		final WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),wiz);
		
		wd.setPageSize(new Point(1200, 600));
		wd.create();
		wd.open();
		
		return null;
	
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		Object variable = HandlerUtil.getVariable(evaluationContext, "activeSite");
		if (variable != null && variable instanceof IWorkbenchSite) {
			ISelection selection = ((IWorkbenchSite)variable).getWorkbenchWindow().getSelectionService().getSelection("org.dawnsci.datavis.view.parts.LoadedFilePart");
			IDataPackage suitableData = getSuitableData(selection);
			
			setBaseEnabled(suitableData != null);
		}
	}
	
	private IDataPackage getSuitableData(ISelection selection){

		if (selection instanceof StructuredSelection) {
			List<IDataFilePackage> list = Arrays.stream(((StructuredSelection)selection).toArray())
			.filter(IDataFilePackage.class::isInstance)
			.map(IDataFilePackage.class::cast).collect(Collectors.toList());
			
			if (list.size() == 1) {
				IDataFilePackage pack = list.get(0);
				IDataPackage[] data = pack.getDataPackages();
				Optional<IDataPackage> ff = Arrays.stream(data).filter(d -> d.isSelected() && d.getLazyDataset().getRank() == 3).findFirst();
				
				if (!ff.isPresent()) return null;
				
				return ff.get();
			}	
		}	
		return null;
	}
	
// TODO should add back in better classification of Arpes files	
//	@Override
//	public boolean isEnabled() {
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		if (window != null) {
//			ISelection selection = HandlerUtil.getActiveSite(event).getWorkbenchWindow().getSelectionService().getSelection("org.dawnsci.datavis.view.parts.LoadedFilePart");
//			IDataPackage suitableData = getSuitableData(selection);
//
//			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
//			Object selected = selection.getFirstElement();
//			String path = "";
//			if (selected instanceof IFile) {
//				IFile ifile = (IFile) selected;
//				path = ifile.getLocation().toOSString();
//			} else if (selected instanceof File) {
//				File file = (File) selected;
//				path = file.getPath();
//			}
//			return ARPESFilePreview.isArpesFile(path, null);
//		}
//
//		return false;
//	}

	@Override
	public boolean isHandled() {
		return true;
	}
}
