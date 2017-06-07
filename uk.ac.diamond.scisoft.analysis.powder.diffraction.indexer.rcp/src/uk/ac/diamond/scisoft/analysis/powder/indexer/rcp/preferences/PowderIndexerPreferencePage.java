package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.dawnsci.common.widgets.file.SelectorWidget;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerParam;
import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerPowderParams;
import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerService;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.IPowderIndexer;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.PowderIndexerFactory;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;

/**
 * Loaders in all powder indexers currently configured. Then the subsequent parameters of these powder indexers.
 * 	
 * @author Dean P. Ottewell
 */
public class PowderIndexerPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public static final String ID = "uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences";
	
	private static final Logger logger = LoggerFactory.getLogger(PowderIndexerPreferencePage.class);

	Map<String,IPowderIndexerPowderParams> indexer= new HashMap<String, IPowderIndexerPowderParams>();
	
	
	Map<String,Number> currentParams = new HashMap<String, Number>(); //TODO: in this scenario all the preference page params are loaded...
	
	private CCombo indexerCombo;
	private Group specificIndexerParams;
	
	private String indexerDirectory = System.getProperty("user.home");

	private SelectorWidget directorySelector;

	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public boolean performOk() {
		return storePreferences();
	}

	@Override
	protected void performDefaults() {
		getPreferenceStore().setDefault(PowderIndexerConstants.indexer, PowderIndexerConstants.INDEXERS.iterator().next());
		
		//TODO: iterate over all intial parameters
		for(String id : PowderIndexerConstants.INDEXERS){
			IPowderIndexer indexer = PowderIndexerFactory.createIndexer(id);
			for (Entry<String, IPowderIndexerParam> param : indexer.getInitialParamaters().entrySet()) {
				getPreferenceStore().setValue(param.getKey(), param.getValue().getValue().doubleValue());
			}
		}
	}
	

	private boolean storePreferences() {
		
		//Should set the current indexer to have the subsequent indexerdirectory
		String directory = directorySelector.getText();
		String selectedIndexer = indexerCombo.getText();
		getPreferenceStore().setValue(selectedIndexer, directory );
		
		
		
		Iterator<Entry<String, Number>> iterator = currentParams.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Number> param = iterator.next();
			//TOOD: check the type before castin
			getPreferenceStore().setValue(param.getKey(), param.getValue().doubleValue());
			currentParams.remove(param.getKey());
		}
		
		return true;
	}
	

	@Override
	protected Control createContents(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));
		GridData gdc = new GridData(SWT.FILL, SWT.FILL, true, true);
		comp.setLayoutData(gdc);

		Group indexerGroup = new Group(comp, SWT.NONE);
		indexerGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		indexerGroup.setLayout(new GridLayout(2, false));
		indexerGroup.setText("Powder Indexing Controls");
		
		specificIndexerParams = new Group(comp, SWT.NONE);
		specificIndexerParams.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		specificIndexerParams.setLayout(new GridLayout(2, false));
		specificIndexerParams.setText("Indexer Configuration Controls");
		
		Label algoLabel = new Label(indexerGroup, SWT.NONE);
		algoLabel.setText("Powder Indexer");
		indexerCombo = new CCombo(indexerGroup, SWT.BORDER);
		indexerCombo.setEditable(false);
		indexerCombo.setListVisible(true);
		indexerCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		indexerCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				//Dispose of those nasty brats
				for (Control childControl : specificIndexerParams.getChildren()){
					childControl.dispose();
				}
				
				/*
				 * Load Indexer Parameters
				 * 
				 * */

				loadPowderIndexer(specificIndexerParams);
				//specificFinderSetting.redraw();
				//specificFinderSetting.pack();
				comp.update();
				comp.setSize(comp.getSize());
				//comp.pack();
				comp.redraw();
			}
		});
		
		getPreferenceStore().getString(PowderIndexerConstants.indexer);
		
		//Needed to initialise this primitively because load in services	
		Iterator<String> itr = PowderIndexerConstants.INDEXERS.iterator();
		while(itr.hasNext()){
			indexerCombo.add(itr.next());
		}
		indexerCombo.select(0);

		
		
		IPowderIndexerService powderIndexerServ = (IPowderIndexerService)Activator.getService(IPowderIndexerService.class);
		powderIndexerServ.getIndexerName(indexerCombo.getText());
		
		
		//TODO: setup for changing the preference indexer location
		Label indexerLocationLb = new Label(indexerGroup, SWT.NONE);
		indexerLocationLb.setText("Indexer Location:");
		
		final Composite locationComp = new Composite(indexerGroup, SWT.NONE);
		locationComp.setLayout(new GridLayout(2, false));
		locationComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		boolean isFolderSelector = true;
		boolean hasResourceButton = true;
		String[] files = new String[] {"All Files"};
		String[] extensions = null;
	    
	    directorySelector = new SelectorWidget(locationComp, isFolderSelector, hasResourceButton, files, extensions){
			
			@Override
			public void pathChanged(String path, TypedEvent event) {
					Button button = PowderIndexerPreferencePage.this.getApplyButton();
//					
//					if (button.getSelection()) {
//						boolean canOK = true;
//						if (isFolderSelector) canOK = this.checkDirectory(path, false);
//						button.setEnabled(canOK);
//						indexerDirectory = path;
//						directorySelector.setText(indexerDirectory);
//					}
				
			}
		};
		directorySelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		directorySelector.setText(indexerDirectory);		
		
		
		specificIndexerParams.layout();
    	
		return comp;
	}
	
	private void loadPowderIndexer(Group specificFinderSetting){
		String currPowderIndexerID = indexerCombo.getText();
		getPreferenceStore().setValue(PowderIndexerConstants.indexer, currPowderIndexerID);
		//TODO: store params as constnats?
		IPowderIndexerService powderIndexerServ = (IPowderIndexerService)Activator.getService(IPowderIndexerService.class);
		Map<String, IPowderIndexerParam> powderIndexerParams = powderIndexerServ.getIndexerParameters(currPowderIndexerID);
				
		for (Entry<String, IPowderIndexerParam> powderIndexerParam : powderIndexerParams.entrySet()){
			IPowderIndexerParam param = powderIndexerParam.getValue();
			//String curVal = getPreferenceStore().getString(powderIndexerParam.getKey());
			Number val = param.getValue();////TODO: set in prefs Double.parseDouble(curVal);
			val = (int) val.doubleValue();
			param.setValue(val);
			genParam(specificFinderSetting, powderIndexerParam.getKey(),param);
		}
	}		
	
	private void genParam(Group paramSetting, String displayname, final IPowderIndexerParam param){
		String value = param.getValue().toString();
		
		Label paramLab = new Label(paramSetting, SWT.NONE);
		paramLab.setText(displayname);//param.getName());
		final Text valTxt = new Text(paramSetting, SWT.BORDER);
		valTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		valTxt.setText(value);
		
		//TODO: validate vals
		valTxt.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {				
				Number val = Double.parseDouble(valTxt.getText());
				val = val.doubleValue();
				currentParams.put(param.getName(), val);
			}
		});
		
		valTxt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getPreferenceStore().setValue(param.getName(), Double.parseDouble(valTxt.getText()));
				//getPreferenceStore().setValue(	FittingConstants.SMOOTHING, smooth);
			}
		});
	}

	
}
