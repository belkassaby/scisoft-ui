package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.jobs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.CCDCService;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;


/**
 * Searches over CCDC and populates controller with results
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchRun implements IRunnableWithProgress {

	protected final Logger logger = LoggerFactory.getLogger(CellSearchRun.class);

	CellSearchManager manager;
	ICellSearchConfig searchConfig;
	
	public CellSearchRun(CellSearchManager manager, ICellSearchConfig searchConfig) {
		this.manager = manager;
		this.searchConfig = searchConfig;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		monitor.beginTask("Connecting to ccdc serivce", IProgressMonitor.UNKNOWN);
		CCDCService searchService = new CCDCService();
		
		monitor.beginTask("Setting up ccdc serivce", IProgressMonitor.UNKNOWN);
		searchService.setUpServer();
		
		if(searchService.serverAvaliable()){
			monitor.beginTask("Establishing connection to CCDC service", IProgressMonitor.UNKNOWN);
		} else {
			monitor.subTask("Could not establish connection to CCDC service");
			monitor.setCanceled(true);
		}
		
		monitor.beginTask("Running cell search procedure...", IProgressMonitor.UNKNOWN);
		
		monitor.beginTask("Configuring searcher procedure...", IProgressMonitor.UNKNOWN);
		
		if(monitor.isCanceled())
			monitor.done();
		
		if(this.searchConfig.getUnitCell() != null){
			monitor.subTask("Setting Search lattice");
			monitor.subTask("Setting Search lattices: " + searchConfig.getSearchCrystal().toString());
		//	searchService.setLattice(this.searchConfig.getSearchCrystal());
			//monitor.subTask("Unable to set lattice");
			//monitor.setCanceled(true);
		}
		
		if(this.searchConfig.getAbsoluteAngleTol() > 0 || this.searchConfig.getPercentageLengthTol() > 0){
			monitor.subTask("Setting Search Tolerance");
			//searchService.setCrystalSearchTolerance(this.searchConfig.getAbsoluteAngleTol(), this.searchConfig.getPercentageLengthTol());
			monitor.subTask("Success Setting Search Tolerance");
		}
		
		if(monitor.isCanceled())
			monitor.done();
		
		monitor.subTask("Searching on " + this.searchConfig.toString());
		monitor.beginTask("Running search procedure...", IProgressMonitor.UNKNOWN);
		
		//TODO: Thread spawn below to be able to cancel the activity as this can then run indefinitely 
		
		searchService.runIndependentCellSearch(searchConfig.getAVal(),searchConfig.getBVal(),searchConfig.getCVal(),searchConfig.getAlphaVal(),searchConfig.getBetaVal(),searchConfig.getGammaVal());
		//searchService.runCrystalSearch();
		
		//TODO: set reset parameters inside searchConfig
		
		//TODO: replace with manager..searcher.runCrystalCellSearch
		//		if(manager.searcher.runBasicCellSearch()){
		//			monitor.subTask("Lattice searched");
		//		} else {
		//			monitor.subTask("Unable to set lattice");
		//			monitor.setCanceled(true);
		//		}
		
		//Filtering on configuration
		
		// Check if indexer process is still active
		monitor.beginTask("Gathering search matches...", IProgressMonitor.UNKNOWN);
		Object matches = searchService.gatherMatches();
		//TODO:Check empty matches

		
		try {
			Object[] matchCast = (Object[]) matches;
			if(matchCast.length == 0){
				monitor.subTask("No matches returned");
				Thread.sleep(800);
				monitor.setCanceled(true);
				monitor.done();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		//TMP: just needed to cast into usable items to display
		//TODO: add proper cast system
		try {
			monitor.beginTask("Configuring matches...", IProgressMonitor.UNKNOWN);
			String[][] matchCast = (String[][]) matches;
			
			List<String[]> searchMatches = Arrays.asList(matchCast);
			monitor.subTask("Configureing " + searchMatches.size() + " matches ");
			
			List<ICellSearchConfig> matchResults = new ArrayList<ICellSearchConfig>();
			for (String[] match : searchMatches){
				CellSearchConfig cellConfigation = new CellSearchConfig();
				cellConfigation.setRefcode(match[0]);
				cellConfigation.setAVal(Double.parseDouble(match[1]));
				cellConfigation.setBVal(Double.parseDouble(match[2]));
				cellConfigation.setCVal(Double.parseDouble(match[3]));
				cellConfigation.setAlphaVal(Double.parseDouble(match[4]));
				cellConfigation.setBetaVal(Double.parseDouble(match[5]));
				cellConfigation.setGammaVal(Double.parseDouble(match[6]));
				cellConfigation.setFormula(match[7]);
				matchResults.add(cellConfigation);
				if(monitor.isCanceled())
					monitor.done();
			}
			
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					//Update the manager that we got some
					manager.loadSearchMatches(matchResults);
				}
			});
			
		} catch (Exception ex){
			System.out.println(ex);
		
			monitor.subTask("Unable to configure the matched search type");
			monitor.setCanceled(true);
			monitor.done();
		}
		
		//TODO: set searchhits
		//if (!manager.searchhits.isEmpty())
		monitor.subTask("Search successful. Best of luck!");
		
		monitor.done();
	}
}
