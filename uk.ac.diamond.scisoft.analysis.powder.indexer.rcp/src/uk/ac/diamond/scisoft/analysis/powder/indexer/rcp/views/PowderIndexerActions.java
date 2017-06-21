package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.january.dataset.IDataset;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.IPowderCellListener;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.PowderIndexerManager;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences.PowderIndexerPreferencePage;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.wizards.PeakImportWizard;



//import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.*; TODO: remove the tmp need to steal this widget
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;



/**
 * @author Dean P. Ottewell
 */
public class PowderIndexerActions {
	
	private static final Logger logger = LoggerFactory.getLogger(PowderIndexerActions.class);

	private PowderIndexerManager manager;
	
	private List<CellParameter> cells;
	
	public PowderIndexerActions(PowderIndexerManager manager){
		this.manager = manager;
	}
	
	public void createActions(IToolBarManager toolbar) {
		
		final Action export = new Action("Import peak(s)", IAction.AS_PUSH_BUTTON) {
			public void run() {
				try {
					PeakImportWizard wiz = new PeakImportWizard(manager);
					wiz.setNeedsProgressMonitor(true);
					final WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wiz);
					wd.create();
					wd.open();
					
				} catch (Exception e) {
					logger.error("Cannot import "+PeakImportWizard.ID, e);
				}
			}
		};
		export.setImageDescriptor(Activator.getImageDescriptor("icons/mask-import-wiz.png"));
		toolbar.add(export);
		
		final Action preferences = new Action("Preferences...") {
			public void run() {
//				/if (!controller.isToolPageActive()) return;
				PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), PowderIndexerPreferencePage.ID, null, null);
				if (pref != null) pref.open();
			}
		};
		preferences.setImageDescriptor(Activator.getImageDescriptor("icons/Configure.png"));
		toolbar.add(preferences);
		
		final Action searchDB = new Action("DB Search", SWT.CHECK) {
			public void run() {
				if (this.isChecked())
					this.setChecked(false);
				else 
					this.setChecked(true);
					sendPowderIndexingEvent();
					//TODO: would like to indent this action. Need to chagne the action setting
			}
		};
		searchDB.setImageDescriptor(Activator.getImageDescriptor("icons/cellSearchDB.png"));
		toolbar.add(searchDB);
		
		
		
		
		IPowderCellListener listener = new IPowderCellListener() {
			
			@Override
			public void theChosenIndexer(String indexerID) {
			}
			
			@Override
			public void isPowderSearching() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void finishedSearching() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void peakDataChanged(IDataset nXData, IDataset nYData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void cellChanged(List<CellParameter> changedCells) {
				cells = changedCells;
			}

			@Override
			public void cellsChanged(List<Crystal> crystals) {
				// TODO Auto-generated method stub
				
			}
			
		
		};
		manager.addCellListener(listener);

		
	}
	
		
	private void sendPowderIndexingEvent(){
		//TODO:Spawn plug in view
		//Interesting does nothing but that was cool know still needs to be displayed
		// new uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.views.CellSearchRoutineView();
		
		//TODO: if context does notwork dpende of the genric context inside ccdc...
		BundleContext ctx = FrameworkUtil.getBundle(PowderIndexerActions.class).getBundleContext();
		ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
	    
		EventAdmin eventAdmin = ctx.getService(ref);
	    //The object in this case being a list of cell parametr
		Map<String,Object> properties = new HashMap<String, Object>();
		
		/*
		 * TMP cells pass
		 * */
		
		CellParameter cellInteract = cells.get(0);
		//Crystal cell =new Crystal(new Lattice.LatticeBuilder(1).build(), CrystalSystem.CUBIC); 
		//Crystal cell = cellInteract.getCrystalSystem();
		
		
		//TODO: where to put this trigger line?
		//TODO: after lattice cast
	    properties.put("INDEXERRESULTS", cellInteract);
	    
	    //Going to be triggered on a button and would like to know its arrived. However, should check before beforeing this action the 
	    //view is active... If thats the case maybe based to have it async...
	    Event event = new Event("powderanalysis/syncEvent", properties);
	    eventAdmin.sendEvent(event);
	}
}






