package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Lattice;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.UnitCell;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellInteraction;

import java.io.Serializable;

/**
 * Configure for all data needed for a CCDC crystal search
 * 
 * TODO: use a CellConfig to search with and also to display
 * TODO: use these store the results also?
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchConfig extends CellInteraction implements ICellSearchConfig, Serializable{
	
	@Override
	public boolean equals(Object obj) {
		//TODO:
		return true;
	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		//TODO: complete with reset of values
		return result;
	}
	
	//CellParameter
	
	/**
	 * Must implement clear() method on beans being used with BeanUI.
	 */
	public void clear() {
		this.elements = null;
		this.spacegroup = null;
		this.refcode = null;
		this.ccdcNum = null;
		this.chemicalName = null;
		this.lengthTol = null;
		this.angleTol = null;
		this.crystalSys = null;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String elements;

	private String spacegroup;
	
	private String refcode;
	
	private String ccdcNum;
	
	private String formula;
	
	private String chemicalName;

	private Double lengthTol;
	
	private Double angleTol;
	
	//TODO: keep cell parameter? has merit and just wrap this around crystal
	//private CellParameter unitcell;

	
	private Crystal crystalSys;
	
	private Lattice latt;
	
	public CellSearchConfig(){
		//intialise unitcell
		//this.unitcell = new CellParameter();
		
		//Cant initialise
		//Lattice latt = new Lattice(angleTol, angleTol, angleTol, angleTol, angleTol, angleTol, null);
		//this.crytalSys = new Crystal();
		//UnitCell genericUnit = new UnitCell(new Lattice.LatticeBuilder(1).build()); //Generic build
		
		//Generic crystal initialisation
		//crystalSys = new Crystal(new Lattice.LatticeBuilder(1).build(), CrystalSystem.CUBIC);
	}
	
	
	public String getElements() {
		return elements;
	}

	public void setElements(String elements) {
		this.elements = elements;
	}

	public String getSpacegroup() {
		return spacegroup;
	}

	public void setSpacegroup(String spacegroup) {
		this.spacegroup = spacegroup;
	}

	public String getRefcode() {
		//refcode.length() != 6
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public String getCcdcNum() {
		return ccdcNum;
	}

	public void setCcdcNum(String ccdcNum) {
		this.ccdcNum = ccdcNum;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String associateName) {
		this.chemicalName = associateName;
	}

	public void setSearchCrysal(Crystal crystal){
		this.crystalSys = crystal;
	}
	
	public void setSearchLattice(Lattice latt){
		this.latt = latt;
		this.crystalSys = new Crystal(latt); //,this.crystalSys.getCrystalsystem());
	}
	
	
	/*
	 * 
	 * TODO: just hacked bean together to interac twith unti cell system currently have. 
	 * need to reconfigure view to match to the different value names
	 * */
	public void setAVal(double a){
		this.setA(a);
	}

	public double getAVal(){
		return this.getA();
	}
	
	public void setBVal(double b){
		this.setB(b);
	}	
	public double getBVal(){
		return this.getB();
	}
	
	public void setCVal(double c){
		this.setC(c);
	}
	public double getCVal(){
		return this.getC();
	}
	
	public double getAlphaVal() {
		return this.getAl();
	}
	public void setAlphaVal(double alpha) {
		this.setAl(alpha);
	}
	
	public double getBetaVal() {
		return this.getBe();
	}
	public void setBetaVal(double beta) {
		this.setBe(beta);
	}
	
	public double getGammaVal() {
		return this.getGa();
	}
	public void setGammaVal(double gamma) {
		this.setGa(gamma);
	}
	
	@Override
	public Crystal getSearchCrystal() {
		return crystalSys;
	}
	@Override
	public Lattice getLattice() {
		return crystalSys.getUnitCell().getLattice();
	}
	
	@Override
	public void setUnitCell(UnitCell unitcell) {
		//No unit cell generator for crystal. need to uniform this before decide the approach.
		//Crystal crystal = new Crystal(lattice)
	}
	
	@Override
	public UnitCell getUnitCell() {
		return crystalSys.getUnitCell();
	}	
	
	public void setAbsoluteAngleTol(double angleTol) {
		this.angleTol = angleTol;
	}
	public double getAbsoluteAngleTol() {
		return angleTol;
	}
	public void setPercentageLengthTol(double lengthTol) {
		this.lengthTol = lengthTol;
	}
	public double getPercentageLengthTol() {
		return lengthTol;
	}
	
	//TODO: better json format
	@Override
	public String toString(){
		String format = "";
		format +="Cell{";
		
		format += crystalSys.getUnitCell().getLattice().toString();
//		format += "A:" +unitcell.getUnitA().toString()+ ",";
//		format += "B:" +unitcell.getUnitB().toString()+ ",";
//		format += "C:" +unitcell.getUnitC().toString()+ ",";
//		
//		format += "Alpha:" +unitcell.getAngleAlpha().toString()+ ",";
//		format += "Beta:" +unitcell.getAngleBeta().toString() + ",";
//		format += "Gamma:" +unitcell.getAngleGamma().toString()+ ",";
//		
		format += "Angle Tol:" +Double.toString(angleTol)+ ",";
		format += "Perecent Length Tol:" +Double.toString(lengthTol).toString()+",";;
		
		format += "}";
		return format;
	}
	
	

}
