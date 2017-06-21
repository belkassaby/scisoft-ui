package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners;
import java.util.EventListener;
import java.util.List;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
/**
 * Useing **TODO: what was then name of this broadcast setup again?
 * 
 * @author Dean P. Ottewell
 *
 */
public interface ICellSearchListener extends EventListener {
	
	public void updateSearchConfig(CellSearchConfig searchConfig);
	
	public void updateSearchCrystalConfig(Crystal searchCrystal);
	
	public void perfromSearch();
	
	public void loadSearchMatches(List<CellSearchConfig> searchConfig);
	
}
