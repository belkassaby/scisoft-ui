package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.CCDCService;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners.ICellSearchListener;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners.CellSearchConfigEvent;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;

/**
 * Managers the cell search data between the ccdc components.
 * 
 * There is a searching section too. 
 * 
 * Events:
 * 	- Cell Config Updates
 * 	- 
 *
 * Jobs spwan inside the views
 * 
 * TODO: what to do if the python setup breaks mid run
 * @author Dean P. Ottewell
 */
public class CellSearchManager {

	private final static Logger logger = LoggerFactory.getLogger(CellSearchManager.class);
	
	public CCDCService searcher;
	
	public CellParameter cell;

	private HashSet<ICellSearchListener> listeners; 
	
	List<CellParameter> searchResults; 
	
	public CellSearchManager(){
		
		//TODO: first handle a bad server
		//initialiseSearcher();
		listeners = new HashSet<ICellSearchListener>();
	}
	
	
	public void initialiseSearcher(){
		searcher = new CCDCService();
		
		
		searcher.setUpServer();
		
		
		
		
		if(searcher.serverAvaliable()){
			//Successfully available
			
		} else {
			searcher.terminateServer();
			logger.debug("CCDC not avalaible to search on");
		}
	}
		
	public void addSearchListener(ICellSearchListener listener) {
		listeners.add(listener);
	}
	
	public void removeSearchListener(ICellSearchListener listener) {
		listeners.remove(listener);
	}

	private void everythingChangesListeners(CellSearchConfigEvent evt) {
		for(ICellSearchListener listener : listeners) {
			if(evt.getUnitCellConfig() != null)
				listener.updateSearchConfig(evt.getUnitCellConfig());
		}
	}
	
	public void loadSearchMatches(List<ICellSearchConfig> matches){
		for(ICellSearchListener listener : listeners) {
			listener.loadSearchMatches(matches);
		}
	}
	
	public void loadSearchConfig(ICellSearchConfig searchConfig){
		everythingChangesListeners(new CellSearchConfigEvent(this, searchConfig));
	}
	
	//TODO: this is the entry point from the indexer. Trigger this and your good.
	public void updateSearchCrystal(Crystal searchCrystal) {
		for(ICellSearchListener listener : listeners) {
			listener.updateSearchCrystalConfig(searchCrystal);
		}
	}
}
