package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners;

import java.util.EventObject;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;

/**
 * @author Dean P. Ottewell
 */
public class CellSearchConfigEvent extends EventObject {
	
	private CellSearchConfig searchConfig;
	
	public CellSearchConfigEvent(Object source, CellSearchConfig  result) {
		super(source);
		this.searchConfig = result;
	}
	
	public CellSearchConfig  getUnitCellConfig(){
		return searchConfig;
	}

}
