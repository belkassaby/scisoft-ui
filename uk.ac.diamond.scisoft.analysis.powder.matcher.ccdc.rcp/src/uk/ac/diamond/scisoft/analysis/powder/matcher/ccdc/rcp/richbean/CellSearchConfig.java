package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Lattice;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.UnitCell;

import java.io.Serializable;

/**
 * Configure for all data needed for a CCDC crystal search
 * 
 * TODO: use a CellConfig to search with and also to display
 * TODO: use these store the results also?
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchConfig extends Lattice implements ICellSearchConfig, Serializable{
	
	@Override
	public boolean equals(Object obj) {
		//TODO:does it matter though...
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
	
	private Crystal crystalSys;
	
	public CellSearchConfig(){
		//Default values
		//Default lattice TODO: should be intialising to zero?
		super(0.0, 0.0, 0.0, 90.0, 90.0, 90.0);
	}
	
	//TODO: setting the rese tof the search parameters?
	public CellSearchConfig(double a, double b, double c, double al, double be, double ga){
		super(a, b, c, al, be, ga);
	}
	
	public CellSearchConfig(Lattice latt) {
		super(latt.getA(), latt.getB(), latt.getC(), latt.getAl(), latt.getBe(), latt.getGa());
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
	
	double a;
	public void setA(double a) {
		this.a = a;
	}
	
	double b;
	public void setB(double b) {
		this.b = b;
	}

	double c;
	public void setC(double c) {
		this.c = c;
	}
	
	double al;
	public void setAl(double al) {
		this.al = al;
	}

	double be;
	public void setBe(double be) {
		this.be = be;
	}

	double ga;
	public void setGa(double ga) {
		this.ga = ga;
	}
	
	
	
	
	//TODO: better json format
	@Override
	public String toString(){
		String format = "";
		format +="Cell{";
		
		format += "A:" + Double.toString(a)+ ",";
		format += "B:" + Double.toString(b)+ ",";
		format += "C:" + Double.toString(c)+ ",";
		
		format += "Alpha:" + Double.toString(al)+ ",";
		format += "Beta:" + Double.toString(be)+ ",";
		format += "Gamma:" + Double.toString(ga)+ ",";
		
		format += "Angle Tol:" +Double.toString(angleTol)+ ",";
		format += "Perecent Length Tol:" +Double.toString(lengthTol).toString()+",";;
		
		format += "}";
		return format;
	}
	
	

}
