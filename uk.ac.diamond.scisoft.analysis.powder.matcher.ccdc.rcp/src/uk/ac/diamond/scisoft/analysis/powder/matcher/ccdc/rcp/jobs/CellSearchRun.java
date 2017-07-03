package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.jobs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dawnsci.analysis.api.rpc.AnalysisRpcException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.CCDCService;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;


/**
 * Searches over CCDC and populates controller with results/
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchRun implements IRunnableWithProgress {

	protected final Logger logger = LoggerFactory.getLogger(CellSearchRun.class);

	CellSearchManager manager;
	CellSearchConfig searchConfig;

	private Object matches;
	
	public CellSearchRun(CellSearchManager manager, CellSearchConfig searchConfig) {
		this.manager = manager;
		this.searchConfig = searchConfig;

	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException{

		
//		try {
//			throw new Exception("Could not create python interpreter");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw new InterruptedException("Could not create python interpreter");
//		} //TODO: just throw this error inside the searcher 
//			
//			
		monitor.beginTask("Connecting to ccdc service", IProgressMonitor.UNKNOWN);
		CCDCService searchService = new CCDCService();
		
		monitor.beginTask("Setting up ccdc service", IProgressMonitor.UNKNOWN);
		try {
			searchService.setUpServer();
		} catch (Exception e) {
			throw new InterruptedException(e.getMessage());
		}
		
		if(searchService.serverAvailable()){
			monitor.beginTask("Establishing connection to CCDC service", IProgressMonitor.UNKNOWN);
		} else {
			monitor.subTask("Could not establish connection to CCDC service");
			monitor.setCanceled(true);
			//throw new InterruptedException("Could not establish a connection to CCDC service. "); grab the message from the searchservice right?
		}
		
		monitor.beginTask("Running cell search procedure...", IProgressMonitor.UNKNOWN);
		
		monitor.beginTask("Configuring searcher procedure...", IProgressMonitor.UNKNOWN);
		
		if(monitor.isCanceled())
			
//		if(this.searchConfig.get != null){
			monitor.done();
//			monitor.subTask("Setting Search lattice");
//			monitor.subTask("Setting Search lattices: " + searchConfig.getSearchCrystal().toString());
//		//	searchService.setLattice(this.searchConfig.getSearchCrystal());
//			//monitor.subTask("Unable to set lattice");
//			//monitor.setCanceled(true);
//		}
		
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
//		try {
//			matches = searchService.performSearchMatches(searchConfig.getA(),searchConfig.getB(),searchConfig.getC(),searchConfig.getAl(),searchConfig.getBe(),searchConfig.getGa());
//		} catch (AnalysisRpcException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		searchService.runIndependentCellSearch(searchConfig.getA(),searchConfig.getB(),searchConfig.getC(),searchConfig.getAl(),searchConfig.getBe(),searchConfig.getGa());
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
		//TODO: call all filters that should be additionally applied
		
		
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
			
			List<CellSearchConfig> matchResults = new ArrayList<CellSearchConfig>();
			for (String[] match : searchMatches){
				
				double a = Double.parseDouble(match[1]);
				double b = Double.parseDouble(match[2]);
				double c = Double.parseDouble(match[3]);
				double al = Double.parseDouble(match[4]);
				double be = Double.parseDouble(match[5]);
				double ga = Double.parseDouble(match[6]);
				
				CellSearchConfig cellConfigation = new CellSearchConfig(a,b,c,al,be,ga);
				
				cellConfigation.setRefcode(match[0]);
				
				
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
