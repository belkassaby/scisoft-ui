package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import java.util.EventListener;
import java.util.List;

import org.eclipse.january.dataset.IDataset;

import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
/**
 * TODO: data pass for peaks
 * TODO: region bounds might be better off as separate
 * 
 * @author Dean P. Ottewell
 */
public interface IPowderCellListener extends EventListener {

	public void cellChanged(List<CellParameter> cell);
	
	public void cellsChanged(List<Crystal> crystals);
	
	public void peakDataChanged(IDataset nXData, IDataset nYData);
	
	public void theChosenIndexer(String indexerID);
	
	public void isPowderSearching();
	
	public void finishedSearching();
}
