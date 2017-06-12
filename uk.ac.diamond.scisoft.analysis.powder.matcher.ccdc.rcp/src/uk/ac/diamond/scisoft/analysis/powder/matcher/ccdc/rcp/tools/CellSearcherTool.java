package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.tools;

import org.dawb.common.ui.widgets.ActionBarWrapper;
import org.eclipse.dawnsci.plotting.api.tool.AbstractToolPage;
import org.eclipse.dawnsci.plotting.api.tool.IToolPage.ToolPageRole;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPageSite;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.views.CellSearchActions;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.widgets.CellSearchWidget;

public class CellSearcherTool extends AbstractToolPage  {

	private CellSearchManager manager; 
	
	private CellSearchWidget widget;
	
	private CellSearchActions actions;

	// Page Components
	private Composite composite;
	
	@Override
	public ToolPageRole getToolPageRole() {
		return ToolPageRole.ROLE_1D;
	}


	@Override
	public Control getControl() {
		return this.composite;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		this.composite.setFocus();
	}

	@Override
	public void createControl(Composite parent) {
		//TODO:Centerered blanket text arguing Unfortuantely Cell Searcher Service is not configured.
		//Have some log... can narrow down where in the service is erroring? maybe even grey out until the ccdc is avalaible to generate as a tool optiton...
		//Based on exceptions thrown in CCDCservice. iff problems exist
		
		this.composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite .setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		manager = new CellSearchManager(); 

		final IPageSite site = getSite();
		IActionBars actionbars = site != null ? site.getActionBars() : generateActionBar(parent);
		
		actions = new CellSearchActions(manager);
		actions.createActions(actionbars.getToolBarManager());
		
		widget = new CellSearchWidget(manager);
		widget.createControl(composite);
		
		super.createControl(parent);
	}
	
	private ActionBarWrapper generateActionBar(Composite parent) {
		ActionBarWrapper actionBarWrapper = null;
		parent = new Composite(parent, SWT.RIGHT);
		parent.setLayout(new GridLayout(1, false));
		actionBarWrapper = ActionBarWrapper.createActionBars(parent, null);
		actionBarWrapper.update(true);
		return actionBarWrapper;
	}
}
