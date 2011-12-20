/*
 * Copyright © 2011 Diamond Light Source Ltd.
 * Contact :  ScientificSoftware@diamond.ac.uk
 * 
 * This is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.diamond.sda.navigator.decorator;

import gda.analysis.io.ScanFileHolderException;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.io.DataHolder;
import uk.ac.diamond.scisoft.analysis.io.HDF5Loader;
import uk.ac.diamond.scisoft.analysis.io.IExtendedMetadata;
import uk.ac.diamond.scisoft.analysis.io.IMetaData;
import uk.ac.diamond.scisoft.analysis.io.LoaderFactory;

public class LightweightScanCommandDecorator extends LabelProvider implements ILightweightLabelDecorator {

	private static final Object SRS_EXT = "dat"; //$NON-NLS-1$
	private static final Object H5_EXT = "h5"; //$NON-NLS-1$
	private static final Object HDF5_EXT = "hdf5"; //$NON-NLS-1$
	private static final Object NXS_EXT = "nxs"; //$NON-NLS-1$
	private IExtendedMetadata metaData;
	private String decorator = "";
	private String fileName;
	private static final Logger logger = LoggerFactory.getLogger(LightweightScanCommandDecorator.class);

	public LightweightScanCommandDecorator() {
		super();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		decorator = "";
		if (element instanceof IFile) {
			IFile modelFile = (IFile) element;
			if (SRS_EXT.equals(modelFile.getFileExtension())) {
				IFile ifile = (IFile) element;
				// IPath path = ifile.getLocation();
				// File file = path.toFile();
				srsMetaDataLoader(ifile);

				try {
					//Collection<String> list = metaData.getMetaNames();
					decorator = metaData.getScanCommand();
					if(decorator==null){
						decorator="Scan Command: N/A";
						decoration.addSuffix(decorator);
						//decoration.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
					}else{
						if (decorator.length() > 100) // restrict to 100 characters
							decorator = decorator.substring(0, 100) + "...";
						decorator = "* " + decorator;
						decoration.addSuffix(decorator);
						//decoration.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
					}
				}catch (Exception e) {
					logger.error("Could not read metadata: ", e);
				}

			}
			if (NXS_EXT.equals(modelFile.getFileExtension())) {
				IFile ifile = (IFile) element;

				try {
					System.out.println("");
					String[][] listTitlesAndScanCmd = getHDF5TitleAndScanCmd(ifile);
					for (int i = 0; i < listTitlesAndScanCmd[0].length; i++) {
						decorator = listTitlesAndScanCmd[0][i] + listTitlesAndScanCmd[1][i];
						decoration.addSuffix(decorator);
						//decoration.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
					}
				} catch (ScanFileHolderException e) {
					logger.error("Could not read hdf5 file: ", e);
				}catch (Exception e){
					logger.error("Could not read hdf5metadata: ", e);
				}
			}
		}		
	}
	
	private void srsMetaDataLoader(IFile file) {
		fileName = file.getLocation().toString();
		
		try {
			IMetaData metaDataTest=LoaderFactory.getMetaData(fileName, null);
			if(metaDataTest instanceof IExtendedMetadata)
				metaData = (IExtendedMetadata)LoaderFactory.getMetaData(fileName, null);
			else{
				decorator=" Scan Command: N/A";
				logger.warn("Cannot decorate SRS decorator");
			}
		} catch (Exception ne) {
			logger.error("Cannot open dat file", ne);
		}
	}

	private String[][] getHDF5TitleAndScanCmd(IFile file) throws Exception {
		fileName = file.getLocation().toString();
		String hdf5scanCommand = "";
		String hdf5Title = "";

		//HDF5File data = new HDF5Loader(fileName).loadTree();
		DataHolder dataHolder= new HDF5Loader(fileName).loadFile();

		List<String> list = getAllRootEntries(dataHolder.getNames());
		Object[] entries = list.toArray();
		String[] scanCmd = new String[entries.length];
		scanCmd=initStringArray(scanCmd);
		String[] titles = new String[entries.length];
		titles=initStringArray(titles);
		
		String[][] listScanCmdAndTitles = new String[2][entries.length];
		for (int i = 0; i < entries.length; i++) {
			// scan command
			if (dataHolder.contains("/" + entries[i].toString() + "/scan_command")) {
				//hdf5scanCommand = data.findNodeLink("/" + entries[i].toString() + "/scan_command").toString();
				hdf5scanCommand = dataHolder.getDataset("/" + entries[i].toString() + "/scan_command").toString();
				scanCmd[i] = "\nScanCmd" + (i+1) + ": " + hdf5scanCommand;// display of the string on a new line
			}
			// title
			if (dataHolder.contains("/" + entries[i].toString() + "/title")) {
				System.out.println(dataHolder.getDataset("/" + entries[i].toString() + "/title").toString());
				//hdf5Title = data.findNodeLink("/" + entries[i].toString() + "/title").toString();
				hdf5Title = dataHolder.getDataset("/" + entries[i].toString() + "/title").toString();
				titles[i] = "\nTitle" + (i+1) + ": " + hdf5Title;// display of the string on a new line
			}
			if (titles[i].length() > 100) // restrict to 100 characters
				titles[i] = titles[i].substring(0, 100) + "...";
			if (scanCmd[i].length() > 100) // restrict to 100 characters
				scanCmd[i] = scanCmd[i].substring(0, 100) + "...";
			
			listScanCmdAndTitles[0][i] = titles[i];
			listScanCmdAndTitles[1][i] = scanCmd[i];
		}
		return listScanCmdAndTitles;

	}
	
	public String[] initStringArray(String[] array){
		for (int i = 0; i < array.length; i++) {
			array[i]="";
		}
		return array;
	}

	public List<String> getAllRootEntries(String[] oldFullPaths) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < oldFullPaths.length; i++) {
			String[] tmp = oldFullPaths[i].split("/");
			if (!list.contains(tmp[1]))
				list.add(tmp[1]);
		}
		return list;
	}
}