/*-
 * Copyright 2012 Diamond Light Source Ltd.
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

package uk.ac.diamond.sda.navigator.views;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import org.dawb.common.ui.util.EclipseUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ExplorerOSHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final FileView fileView = (FileView)EclipseUtils.getActivePage().getActivePart();
		
		File fileObject;
		
		if (fileView.getSelectedFile().isDirectory()) {
			fileObject = fileView.getSelectedFile();
		} else {
			fileObject = fileView.getSelectedFile().getParentFile();
		}
		
		final File finalFile = fileObject;
		Job openfile = new Job("open file") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				try {
					//Desktop.getDesktop().open() should work for all OSs
					// but in testing the odd windows system didnt open explorer
					// when given a path deep into the C drive
					//This is also why this is in a job
					if (System.getProperty("os.name").toLowerCase().contains("win")) {
						new ProcessBuilder("explorer.exe",finalFile.getAbsolutePath()).start();
						return Status.OK_STATUS;
					}
					
					Desktop desktop = Desktop.getDesktop();
					desktop.open(finalFile);
				} catch (IOException e) {
					// do nothing
				}
				return Status.OK_STATUS;
			}
		};
		openfile.schedule();
		return Boolean.TRUE;
	}


}
