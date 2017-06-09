package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;

/**
 * Checkbox for each thing to search on
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchConfigWidget {
	
	private final Logger logger = LoggerFactory.getLogger(CellSearchConfigWidget.class);
	
	private CellSearchManager controller;
	Button saveConfig; 
	Button runSearch;
	
	public CellSearchConfigWidget(CellSearchManager controller) {
		this.controller = controller;
	}

	public void createControl(final Composite parent) {
		
		Group searcher = new Group(parent, SWT.FILL);
		searcher.setText(" Cell Search Configuration ");
		searcher.setLayout(new GridLayout(1, false));
		searcher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		searcher.setBackground(parent.getBackground()); 
		
		Group controls = new Group(searcher, SWT.FILL);
		controls.setLayout(new GridLayout(2, false));
		controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		controls.setBackground(parent.getBackground()); 
		
		saveConfig = new Button(controls, SWT.PUSH);
		saveConfig.setText("Save");
		saveConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		saveConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Save config / apply 
			}	
		});
		saveConfig.setEnabled(true); //Disabled until configured
		
		runSearch = new Button(controls, SWT.PUSH);
		runSearch.setText("Run");
		runSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		runSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO:close  +controller.searchWidget.handlesearc
			}	
		});
		runSearch.setEnabled(false); //Disabled until configured
		
		//Configurable parameters

		final Composite configurable = new Composite(parent, SWT.NONE);
		configurable.setLayout(new GridLayout(3, false));
		configurable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		Label cellLengthLb= new Label(configurable, SWT.NONE);
		cellLengthLb.setText("Cell Lengths");
		
		final Text aTxt = new Text(configurable, SWT.BORDER);
		aTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aTxt.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {				
				//TODO: Update manager with refcode search param
			}
		});
				
		Button checkbox = new Button(configurable, SWT.CHECK);
		checkbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
			    Button btn = (Button) event.getSource();
			    if(btn.getSelection()) {
			    	//TODO:Update manager
			    }
			}
		});
		checkbox.setSelection(false);
		
	}
	
}
