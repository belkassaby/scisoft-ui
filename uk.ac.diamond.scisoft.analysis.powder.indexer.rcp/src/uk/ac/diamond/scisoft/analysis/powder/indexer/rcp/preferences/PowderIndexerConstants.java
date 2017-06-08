package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.diamond.scisoft.analysis.powder.indexer.IPowderIndexerService;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;

public class PowderIndexerConstants {
	
	public static final String indexer           = "uk.ac.diamond.scisoft.analysis.powder.diffraction.indexer";
	
	private static IPowderIndexerService indexerServ = (IPowderIndexerService)Activator.getService(IPowderIndexerService.class);
	
	public static Collection<String> INDEXERS = indexerServ.getRegisteredIndexers();
	
	public static Map<String, String> INDEXERSPATH; //TODO: grab tbhe default paths from indexers themsles
	
	
	//Genric cosntants decided on for the ui..
	public static final String wavelength = "Wavelength";
	
	public static final String maxVolume = "MaxVol";
	
	public static final String maxABC = "MaxABC";
	
	public static final String minFigureMerit = "minFigureMerit";
	
	
	public static String[] crystalSystems = {"cubic","monoclinic", "orthorhombic","tetragonal","trigonal","hexagonal","triclinic"};
	
	
	//TODO: not have here...
	public static final String cubicSearch = "cubic";
	public static final String monoclinicSearch = "monoclinic";
	public static final String orthorhombicSearch = "orthorhombic";
	public static final String tetragonalSearch = "tetragonal";
	public static final String trigonalSearch = "trigonal";
	public static final String hexagonalSearch = "hexagonal";
	public static final String triclinicSearch = "triclinic";
	
}
