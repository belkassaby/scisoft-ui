package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerParam;
import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerService;

import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;

/**
 * @author Dean P. Ottewell
 *
 */
public class PowderIndexerPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		//TODO: Load in intial values from the indexer class themselves...
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		//TODO:Load in through constants...which in many ways is not a constant but the loading service... nice...
		//TODO: were loading in them all... every param... are we okay? do we need to talk about this?
		store.setDefault(PowderIndexerConstants.selectedIndexer, PowderIndexerConstants.INDEXERS.iterator().next());
		
		//TODO:Just load in every params defaults
		IPowderIndexerService powderIndexerFindServ = (IPowderIndexerService)Activator.getService(IPowderIndexerService.class);
		

		Iterator<String> powderIndexer = powderIndexerFindServ.getRegisteredIndexers().iterator();
		while(powderIndexer.hasNext()){
			String indexerID = powderIndexer.next();
			
			//The id will have the correpsonding path value for all indexers
			store.setDefault(indexerID, PowderIndexerConstants.INDEXERDEFAULTDIRECTORY);
			
			Map<String, IPowderIndexerParam> powderIndexerParams = powderIndexerFindServ.getIndexerParameters(indexerID);
			for (Entry<String, IPowderIndexerParam> powderIndexerParam : powderIndexerParams.entrySet()){
				IPowderIndexerParam param = powderIndexerParam.getValue();
				String name = param.getName();
				Number val = param.getValue();
				store.setDefault(name, val.doubleValue());
			}
		}

		
		
		store.setDefault(PowderIndexerConstants.CUBICSEARCH, true);
		store.setDefault(PowderIndexerConstants.MONOCLINICSEARCH, true);
		store.setDefault(PowderIndexerConstants.ORTHORHOMBICSEARCH, true);
		store.setDefault(PowderIndexerConstants.HEXAGONALSEARCH, true);
		store.setDefault(PowderIndexerConstants.TETRAGONALSEARCH, true);
		store.setDefault(PowderIndexerConstants.TRIGONALSEARCH, true);
		store.setDefault(PowderIndexerConstants.HEXAGONALSEARCH, true);
		store.setDefault(PowderIndexerConstants.TRICLINICSEARCH, false);
		
	}

}
