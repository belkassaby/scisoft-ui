package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.dawnsci.common.widgets.spinner.FloatSpinner;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerParam;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.PowderIndexerManager;

public class PowderIndexerTabLayout {
	
		private PowderIndexerManager manager ;
	
		public PowderIndexerTabLayout(PowderIndexerManager manager) {
			this.manager = manager;
		}
		
		public void createControl(final Composite parent) {
			
			final TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	 		
			//Iterate over the preferencess ?
//			Map<String, IPowderIndexerParam> params;
//			
//			for (Entry<String, IPowderIndexerParam> powderParam : params.entrySet()){
//			
//			
//				TabItem indexerTab = new TabItem(tabFolder, SWT.NONE);
//				indexerTab.setText();
//				
//		
//				indexerTab.setControl(control);
//				
//			}
			
			//Ntreor
			TabItem ntreorTba = new TabItem(tabFolder, SWT.NONE);
			
			ntreorTba.setText("Ntreor");
			ntreorTba.setControl(new Ntreor(tabFolder, manager));

			
			
			//Dicvol
			TabItem dicvolTba = new TabItem(tabFolder, SWT.NONE);
			
			dicvolTba.setText("Dicvol");
			dicvolTba.setControl(new Ntreor(tabFolder, manager));
			
		}

}

class Ntreor extends Composite {
	private PowderIndexerManager manager;
	
	Ntreor(Composite parent, PowderIndexerManager manager) {		
		super(parent, SWT.BORDER);
		this.manager = manager;
		
		Composite searchConfig = new Composite(parent, SWT.NONE);
		searchConfig.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		searchConfig.setLayout(new GridLayout(2, false));
	
		
		Label wavelengthLb = new Label(searchConfig, SWT.NONE);
		wavelengthLb.setText("Wavelength" + "/ \u212B");
		wavelengthLb.setToolTipText("As shown by the second vertical line");
		
		FloatSpinner wavelengthVal = new FloatSpinner(searchConfig, SWT.BORDER);
		wavelengthVal.setWidth(8);
		wavelengthVal.setPrecision(6);
		wavelengthVal.setMinimum(0);
		wavelengthVal.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		wavelengthVal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manager.setWavelength(wavelengthVal.getDouble());
			}
		});
		
		
	}

}
