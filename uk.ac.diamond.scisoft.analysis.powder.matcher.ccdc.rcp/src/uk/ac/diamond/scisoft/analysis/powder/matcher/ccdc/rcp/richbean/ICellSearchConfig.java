package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Lattice;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.UnitCell;;

/**
 * 
 * Configure for all data needed for a CCDC crystal search
 * 
 * TODO: use a CellConfig to search with and also to display
 * TODO: use these store the results also?
 * 
 * @author Dean P. Ottewell
 */
public interface ICellSearchConfig {
	
	//CCDC filters and search configurators
	public String getElements();
	public void setElements(String elements);
	public String getSpacegroup();
	public void setSpacegroup(String spacegroup);
	public String getRefcode();
	public void setRefcode(String refcode);
	public String getCcdcNum();
	public void setCcdcNum(String ccdcNum);
	public String getFormula();
	public void setFormula(String formula);
	public String getChemicalName();
	public void setChemicalName(String chemicalName);
	public void setAbsoluteAngleTol(double angleTol);
	public double getAbsoluteAngleTol();	
	public void setPercentageLengthTol(double lengthTol);
	public double getPercentageLengthTol();
	
	//Crystal parameters
	//Remvoing the dependence of indexer cellParameter and moving to norphos crystal
	//public CellParameter getUnitcell();
	//public void setUnitcell(CellParameter unitcell);
	
	//TODO: or should you be only allowed to get one of the following??
	public void setSearchCrysal(Crystal crystal);
	public Crystal getSearchCrystal();
	
	public void setSearchLattice(Lattice latt);
	public Lattice getLattice();

	public void setUnitCell(UnitCell unitcell);
	public UnitCell getUnitCell();
	
	//Should not be setting these just make interface extend cell param or well IPowderCell...
	public void setAVal(double a);
	public double getAVal();
	
	public void setBVal(double b);
	public double getBVal();
	
	public void setCVal(double c);
	public double getCVal();
	
	public double getAlphaVal();
	public void setAlphaVal(double alpha);
	
	public double getBetaVal();
	public void setBetaVal(double beta);

	public double getGammaVal();
	public void setGammaVal(double gamma) ;
	

}
