package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners;

import java.util.EventObject;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;

/**
 * @author Dean P. Ottewell
 */
public class CellSearchConfigEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private ICellSearchConfig searchConfig;
	
	public CellSearchConfigEvent(Object source, ICellSearchConfig  result) {
		super(source);
		this.searchConfig = result;
	}
	
	public ICellSearchConfig  getUnitCellConfig(){
		return searchConfig;
	}

}
