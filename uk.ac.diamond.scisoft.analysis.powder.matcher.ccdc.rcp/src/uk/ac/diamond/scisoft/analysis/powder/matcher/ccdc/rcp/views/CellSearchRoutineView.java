package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.widgets.CellSearchWidget;

/**
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchRoutineView extends ViewPart {
	
	public static final String ID = "uk.ac.diamond.scisoft.diffraction.powder.matcher.ccdc.rcp.views.CellSearchRoutineView";

	private CellSearchManager manager; 
	
	private CellSearchWidget widget;
	
	private CellSearchActions actions;
	
	public CellSearchRoutineView() {
		this.manager = new CellSearchManager();
	}
	
	@Override
	public void createPartControl(final Composite parent) {
		manager = new CellSearchManager(); 
		
		//TODO:Centerered blanket text arguing Unfortuantely Cell Searcher Service is not configured.
		//Have some log... can narrow down where in the service is erroring?
		//Based on exceptions thrown in CCDCservice. iff problems exist 
		
		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		widget = new CellSearchWidget(manager);
		widget.createControl(content);
		//manager.setMainWidget(widget); //TODO: do want this back propagation
		
		//ActionBar setup	
		actions = new CellSearchActions(manager);
		actions.createActions(this.getViewSite().getActionBars().getToolBarManager());
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
}