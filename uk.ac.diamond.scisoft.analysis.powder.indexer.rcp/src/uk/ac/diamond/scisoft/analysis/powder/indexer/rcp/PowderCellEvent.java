package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import java.util.EventObject;

/**
 * @author Dean P. Ottewell
 */
public class PowderCellEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private IPowderCell powderCell;
	
	public PowderCellEvent(Object source, IPowderCell result) {
		super(source);
		this.powderCell = result;
	}
	
	public IPowderCell getPowderCell(){
		return powderCell;
	}

}
