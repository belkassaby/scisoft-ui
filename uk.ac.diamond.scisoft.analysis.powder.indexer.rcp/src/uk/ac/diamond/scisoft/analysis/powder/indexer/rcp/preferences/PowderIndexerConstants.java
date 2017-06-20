package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerService;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;

public class PowderIndexerConstants {
	
	public static final String selectedIndexer           = "uk.ac.diamond.scisoft.analysis.powder.diffraction.indexer";
	
	private static IPowderIndexerService indexerServ = (IPowderIndexerService)Activator.getService(IPowderIndexerService.class);
	
	public static Collection<String> INDEXERS = indexerServ.getRegisteredIndexers();
	
	public static Map<String, String> INDEXERSPATH; //TODO: grab tbhe default paths from indexers themsles
	
	public static final String INDEXERDEFAULTDIRECTORY = System.getProperty("java.io.tmpdir"); //TODO: not as whole collective
	
	//Genric cosntants decided on for the ui..
	public static final String wavelength = "Wavelength";
	
	public static final String maxVolume = "MaxVol";
	
	public static final String maxABC = "MaxABC";
	
	public static final String minFigureMerit = "minFigureMerit";
	
	
	public static String[] crystalSystems = {"cubic","monoclinic", "orthorhombic","tetragonal","trigonal","hexagonal","triclinic"};
	
	
	//TODO: not have here...
	public static final String CUBICSEARCH = "cubic";
	public static final String MONOCLINICSEARCH = "monoclinic";
	public static final String ORTHORHOMBICSEARCH = "orthorhombic";
	public static final String TETRAGONALSEARCH = "tetragonal";
	public static final String TRIGONALSEARCH = "trigonal";
	public static final String HEXAGONALSEARCH = "hexagonal";
	public static final String TRICLINICSEARCH = "triclinic";
	
}
