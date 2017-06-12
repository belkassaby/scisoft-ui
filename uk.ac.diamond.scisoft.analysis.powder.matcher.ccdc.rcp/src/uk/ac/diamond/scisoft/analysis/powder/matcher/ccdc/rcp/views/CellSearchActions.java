package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.views;

import org.eclipse.jface.action.IToolBarManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;

/**
 * 
 * Gives ability to search based off a config page and save out as .cif
 * 
 * TODO: do not really like these action bars. 
 * @author Dean P. Ottewell
 */
public class CellSearchActions {
	
	private static final Logger logger = LoggerFactory.getLogger(CellSearchActions.class);

	private CellSearchManager manager;
	
	public CellSearchActions(CellSearchManager manager){
		this.manager = manager;
	}
	
	public void createActions(IToolBarManager toolbar) {		
		//TODO: do not use many actions here
	}
	
}
