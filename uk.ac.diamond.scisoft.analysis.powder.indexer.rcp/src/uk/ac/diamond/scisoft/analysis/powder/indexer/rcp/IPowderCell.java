package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import java.util.List;

import org.eclipse.january.dataset.IDataset;

import uk.ac.diamond.scisoft.analysis.fitting.functions.IdentifiedPeak;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;

/**
 * 
 * 
 * 
 * @author Dean P. Ottewell
 */
public interface IPowderCell {

	public void setCells(List<CellParameter> cells);
	public List<CellParameter>  getCells();

	public void setIndexerID(String indexerID);
	public String getIndexerID();
	
	public void setPowderSearching(boolean searching);
	public Boolean getSearchingStatus();
	
	//Peak data below. TODO: Maybe I should put it in as identified peak
	public IDataset getXData();
	public void  setXData(IDataset xData);
	
	public IDataset getYData();
	public void setYData(IDataset yData);
	
	void setPeaks(List<IdentifiedPeak> peaks);
}
