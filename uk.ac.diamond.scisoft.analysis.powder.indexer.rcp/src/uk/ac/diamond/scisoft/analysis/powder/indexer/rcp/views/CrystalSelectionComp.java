package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Dean P. Ottewell
 *
 */
public class CrystalSelectionComp extends Composite {

	
	//TODO: put in preferences
	private Map<String, Boolean> shouldSearchSystems = new HashMap<String, Boolean>();
	
	public CrystalSelectionComp (Composite parent, int style, Map<String, Boolean>  intialSearchSystems) {
		super(parent, style);

		this.shouldSearchSystems = intialSearchSystems;
		createContent();
	}

	private void createContent() {
		
		this.setLayout(new GridLayout(1, false));

		Composite crystalSystems = new Composite(this, SWT.NONE);
		crystalSystems.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		crystalSystems.setLayout(new GridLayout(shouldSearchSystems.size()/2,false));
		
		
		for (Entry<String, Boolean>  param : shouldSearchSystems.entrySet()){
	    	Composite comp = new Composite(crystalSystems, SWT.NONE);
	    	comp.setLayout(new GridLayout(2, false));
	    	
	    	Label systemLbl = new Label(comp, SWT.NONE);
	    	systemLbl.setText(param.getKey());
			
			controlUseGenerator(comp, systemLbl ,param.getValue());
		}
				
	}
	
	public Map<String, Boolean> getSearchLimtiation() {
		return shouldSearchSystems;
	}
	
	//TODO: bean was being set up for cystal selections
	private Composite controlUseGenerator(Composite comp, Label label, boolean isOn){
		Button shouldSearch = new Button(comp, SWT.CHECK);
		shouldSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		shouldSearch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				  Button btn = (Button) e.getSource();
				  label.setEnabled(btn.getSelection());
				  //label.off();
				  //data.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GR));
//				  data.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
//				  data.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
//				  data.redraw();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
//		label.setEnabled(isOn);
//		if(isOn) {
//			label.on();
//		} else {
//			label.off();
//		}
		
		shouldSearch.setSelection(isOn);
		return comp;
	}

}
