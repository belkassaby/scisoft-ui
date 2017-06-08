package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.january.dataset.DatasetFactory;
import org.eclipse.january.dataset.IDataset;
import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.fitting.functions.IdentifiedPeak;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.StandardConstantParameters;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences.PowderIndexerConstants;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.widget.PowderIndexerSetupWidget;

import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;

/**
 * @author Dean P. Ottewell
 */
public class PowderIndexerManager {

	private final static Logger logger = LoggerFactory.getLogger(PowderIndexerManager.class);
	
	private List<PowderIndexerManager> model;
	
	private PowderIndexerSetupWidget mainWidget;
	
	private HashSet<IPowderCellListener> listeners;

	public PowderIndexerManager(){
		listeners = new HashSet<IPowderCellListener>();
	}
	
	//Need like the identified peak right? what about just allow pass of IPeakOppurtunity?
	//TODO: change or remove most of below
	public void setMainWidget(PowderIndexerSetupWidget mainWidget) {
		this.mainWidget = mainWidget;
	}
	public List<PowderIndexerManager> getModel() {
		return model;
	}

	public void setModel(List<PowderIndexerManager> model) {
		this.model = model;
	}

	public void setCellData(List<CellParameter> data) {
		IPowderCell powderCell = new PowderCell();
		powderCell.setCells(data);
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}
	
	public void setPowderSearching(){
		IPowderCell powderCell = new PowderCell();
		powderCell.setPowderSearching(true);
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}
	
	//Only configured once peak data has been loaded
	public void finishedPowderSearching(){
		IPowderCell powderCell = new PowderCell();
		powderCell.setPowderSearching(false);
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}

	public void setUseingIndexerID(String useingIndexerID) {
		IPowderCell powderCell = new PowderCell();
		powderCell.setIndexerID(useingIndexerID);
		//TODO: catch validity failure before triggering
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}
	
	public void setPeakPosData(IDataset xData,IDataset yData){
		IPowderCell powderCell = new PowderCell();
		powderCell.setXData(xData);
		powderCell.setYData(yData);
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}
	
	public void setPeakPosData(List<IdentifiedPeak> peaks){
		IPowderCell powderCell = new PowderCell();
		powderCell.setPeaks(peaks);
		
		List<Double> xData = new ArrayList<Double>();
		List<Double> yData = new ArrayList<Double>();
		
		for(IdentifiedPeak peak : peaks){
			xData.add(peak.getPos());
			yData.add(peak.getHeight());
		}
		
		powderCell.setXData(DatasetFactory.createFromList(xData));
		powderCell.setYData(DatasetFactory.createFromList(yData));
		
		everythingChangesListeners(new PowderCellEvent(this, powderCell));
	}
	
	
	public void setWavelength(double wavelength) {
		
		IPreferenceStore powderIndexerPreferences = Activator.getLocalPreferenceStore();
		if (Activator.getLocalPreferenceStore().getString("Wavelength") == "") {
			logger.debug("No Wavelegnth preference is stored...");
		}
		
		Activator.getLocalPreferenceStore().setValue(StandardConstantParameters.wavelength, wavelength);
	}
	
	public void setMaximumVolume(double maxVol) {
		IPreferenceStore powderIndexerPreferences = Activator.getLocalPreferenceStore();
		if (Activator.getLocalPreferenceStore().getString("maxVolume") == "") {
			logger.warn("Powder indexer had no max volume in preference is stored...");
		}
		
		Activator.getLocalPreferenceStore().setValue(StandardConstantParameters.maxVolume, maxVol);
	}
	
	public void setMaxABC(double maxABC) {
		IPreferenceStore powderIndexerPreferences = Activator.getLocalPreferenceStore();
		if (Activator.getLocalPreferenceStore().getString("maxABC") == "") {
			logger.warn("Powder indexer had no max ABC in preference is stored...");
		}
		
		Activator.getLocalPreferenceStore().setValue(StandardConstantParameters.maxABC, maxABC);
	}
	
	public void setMinFigureMerit(double minFigureMerit){
		IPreferenceStore powderIndexerPreferences = Activator.getLocalPreferenceStore();
		if (Activator.getLocalPreferenceStore().getString("minFigureMerit") == "") {
			logger.warn("Powder indexer had no minimum figure meritin preference is stored...");
		}
		
		Activator.getLocalPreferenceStore().setValue(StandardConstantParameters.minFigureMerit, minFigureMerit);
	
	}
	
	public void setCrystalSystemSearch(Map<String, Boolean> shouldSearchSystems){
		IPreferenceStore powderIndexerPreferences = Activator.getLocalPreferenceStore();
		
		for(java.util.Map.Entry<String, Boolean> system : shouldSearchSystems.entrySet()){
			if (Activator.getLocalPreferenceStore().getString(system.getKey()) == "") {
				logger.warn("Powder indexer did not have crystal system " + system.getKey() + " set.");
			}
			Activator.getLocalPreferenceStore().setValue(system.getKey(), system.getValue());
		}
	
	}
	
	
	public void addCellListener(IPowderCellListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeCellListener(IPowderCellListener listener) {
		this.listeners.remove(listener);
	}

	private void everythingChangesListeners(PowderCellEvent evt) {
		for(IPowderCellListener listener : listeners) {

			if(evt.getPowderCell().getXData()!= null)
				listener.peakDataChanged(evt.getPowderCell().getXData(), evt.getPowderCell().getYData());
			
			
			if(evt.getPowderCell().getCells()!= null)
				listener.cellChanged(evt.getPowderCell().getCells());
			
			if(evt.getPowderCell().getIndexerID()!= null)
				listener.theChosenIndexer(evt.getPowderCell().getIndexerID());
			
			if (evt.getPowderCell().getSearchingStatus() != null){
				if(evt.getPowderCell().getSearchingStatus()){
					listener.finishedSearching();;
				} else {
					listener.isPowderSearching();;
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
}
