package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import java.util.List;

import org.eclipse.january.dataset.IDataset;

import uk.ac.diamond.scisoft.analysis.fitting.functions.IdentifiedPeak;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;

/**
 * @author Dean P. Ottewell
 */
public class PowderCell implements IPowderCell {

	private IDataset xData = null; 
	private IDataset yData = null;
	
	private List<CellParameter> cells;
	
	private Boolean isSearching = null;
	
	private String indexerId;

	private List<IdentifiedPeak> peaks;
	
	@Override
	public List<CellParameter> getCells() {
		return cells;
	}

	@Override
	public void setCells(List<CellParameter> cells) {
		this.cells = cells;
	}

	@Override
	public void setPowderSearching(boolean searching) {
		this.isSearching = searching;
	}

	@Override
	public Boolean getSearchingStatus() {
		return isSearching;
	}
	
	//TODO: id check the validity in service
	@Override
	public void setIndexerID(String indexerID) {
		this.indexerId = indexerID;
	}

	@Override
	public String getIndexerID() {
		return this.indexerId;
	}

	@Override
	public IDataset getXData() {
		return this.xData;
	}

	@Override
	public void setXData(IDataset xData) {
		this.xData = xData;
	}

	@Override
	public IDataset getYData() {
		return this.yData;
	}

	@Override
	public void setYData(IDataset yData) {
		this.yData = yData;
	}
	
	@Override
	public void setPeaks(List<IdentifiedPeak> peaks) {
		this.peaks = peaks;
	}
}
