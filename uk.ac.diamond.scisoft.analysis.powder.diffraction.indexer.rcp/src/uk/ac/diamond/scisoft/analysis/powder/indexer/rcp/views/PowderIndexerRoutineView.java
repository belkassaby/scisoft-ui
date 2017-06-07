package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.PowderIndexerManager;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.widget.PowderIndexerSetupWidget;

/**
 * @author Dean P. Ottewell
 *
 */
public class PowderIndexerRoutineView extends ViewPart { 
	public static final String ID = "uk.ac.diamond.scisoft.analysis.powder.diffraction.indexer.rcp.views.PowderIndexingRoutine";
	
	private CellParameterDelegate cells;
	
	private PowderIndexerSetupWidget widget;
	
	private PowderIndexerActions actions;
	
	private PowderIndexerManager controller; 
	
	@Override
	public void createPartControl(final Composite parent) {
		//Initialise controller
		controller = new PowderIndexerManager();

		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		cells = new CellParameterDelegate(controller);
		cells.createControl(content);
		
		widget = new PowderIndexerSetupWidget(controller); 
		widget.createControl(content);
		controller.setMainWidget(widget); //TODO: do want this back propagation
		
		actions = new PowderIndexerActions(controller);
		actions.createActions(this.getViewSite().getActionBars().getToolBarManager());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
}