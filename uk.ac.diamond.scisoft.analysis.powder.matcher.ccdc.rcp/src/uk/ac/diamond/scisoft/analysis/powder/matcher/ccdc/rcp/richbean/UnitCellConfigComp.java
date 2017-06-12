package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.decorator.BoundsDecorator;
import org.eclipse.richbeans.widgets.decorator.FieldDecorator;
import org.eclipse.richbeans.widgets.decorator.FloatDecorator;
import org.eclipse.richbeans.widgets.decorator.TextFieldDecorator;
import org.eclipse.richbeans.widgets.wrappers.TextWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Composite setup for cell conifg
 * 	TODO: check the invalid entries for parameters such as element limit choice and ccdcNum limit size 
 * @author Dean P. Ottewell
 */
public class UnitCellConfigComp extends Composite {

	private FieldDecorator<Text> aVal, bVal, cVal;
	private FieldDecorator<Text> alpVal,betaVal,gamVal;
	
	private FieldDecorator<Text> angleTol,lengthTol;
	
	private TextWrapper elements; 
	private TextWrapper spacegroup;
	private TextWrapper refcode;
	private TextWrapper ccdcNum;
	private TextWrapper formula;
	private TextWrapper associateName;
	
	public UnitCellConfigComp(Composite parent, int style) {
		super(parent, style);
		createContent();
	}

	private void createContent() {
		
		this.setLayout(new GridLayout(2, false));

		
		//CellParameter
		Label unitCellLbl = new Label(this,SWT.NONE);
		unitCellLbl.setText("Unit Cell: ");
		
		Composite cellValue = new Composite(this, SWT.NONE);
		cellValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		cellValue.setLayout(new GridLayout(6,false));
		
		
		
		Label label = new Label(cellValue, SWT.NONE);
		label.setText("a" + " / " + "\u212B");;
		
    	Composite comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		Text aValTxt = new Text(comp, SWT.NONE);
		aValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final BoundsDecorator abounds = new FloatDecorator(aValTxt);
		abounds.setMinimum(0);
		abounds.setMaximum(100);
    	this.aVal = new TextFieldDecorator(aValTxt, abounds);
		controlUseGenerator(comp, aVal,true);

		
		label = new Label(cellValue, SWT.NONE);
		label.setText("b" + " / " + "\u212B");
		
		comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		
		Text bValTxt = new Text(comp, SWT.NONE);
		bValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final BoundsDecorator bbounds = new FloatDecorator(bValTxt);
		bbounds.setMinimum(0);
		bbounds.setMaximum(100);
		this.bVal = new TextFieldDecorator(bValTxt, bbounds);

		controlUseGenerator(comp, bVal,true);
		
		label = new Label(cellValue, SWT.NONE);
		label.setText("c" + " / " + "\u212B");
		
		comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		
		Text cValTxt = new Text(comp, SWT.NONE);
		cValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final BoundsDecorator cbounds = new FloatDecorator(cValTxt);
		cbounds.setMinimum(0);
		cbounds.setMaximum(100);
		this.cVal = new TextFieldDecorator(cValTxt, cbounds);
		
		controlUseGenerator(comp, cVal,true);
		
		//Alpha

		label = new Label(cellValue, SWT.NONE);
		label.setText("\u03B1" + " / " + "\u00B0");
		
		comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		Text alpValTxt = new Text(comp, SWT.NONE);
		alpValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final FloatDecorator alpbounds = new FloatDecorator(alpValTxt);
		cbounds.setMinimum(0);
		cbounds.setMaximum(360);
		this.alpVal = new TextFieldDecorator(alpValTxt, alpbounds);
		
		controlUseGenerator(comp, alpVal,true);
		
		//beta
		label = new Label(cellValue, SWT.NONE);
		label.setText("\u03B2" + " / " + "\u00B0");
		
		comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		
		Text betaValTxt = new Text(comp, SWT.NONE);
		betaValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final FloatDecorator betabounds = new FloatDecorator(betaValTxt);
		cbounds.setMinimum(0);
		cbounds.setMaximum(360);
		this.betaVal = new TextFieldDecorator(betaValTxt, betabounds);

		controlUseGenerator(comp, betaVal,true);
		
		//Gamma
		label = new Label(cellValue, SWT.NONE);
		label.setText("\u03B3" + " / " + "\u00B0");
		
		comp = new Composite(cellValue, SWT.NONE);
    	comp.setLayout(new GridLayout(2, false));
		
		
		Text gamValTxt = new Text(comp, SWT.NONE);
		gamValTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final FloatDecorator gammabounds = new FloatDecorator(gamValTxt);
		cbounds.setMinimum(0);
		cbounds.setMaximum(360);
		this.gamVal = new TextFieldDecorator(gamValTxt, gammabounds);
		controlUseGenerator(comp, gamVal,true);
		
		
		
		
		
		
		label = new Label(this, SWT.NONE);
		label.setText("Absolute Angle Tolerance ");
		
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		Text angleTolTxt = new Text(comp, SWT.NONE);
		angleTolTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final FloatDecorator angleTolBounds = new FloatDecorator(angleTolTxt);
		angleTolBounds.setMinimum(0);
		angleTolBounds.setMaximum(360);
		this.angleTol = new TextFieldDecorator(angleTolTxt, angleTolBounds);
		controlUseGenerator(comp, angleTol,false);
		angleTol.setEnabled(false);
		
		label = new Label(this, SWT.NONE);
		label.setText("Percentage Length Tolerance ");
		
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		Text lengthTolTxt = new Text(comp, SWT.NONE);
		lengthTolTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final FloatDecorator lengthTolBounds = new FloatDecorator(lengthTolTxt);
		lengthTolBounds.setMinimum(0);
		lengthTolBounds.setMaximum(100);
		this.lengthTol = new TextFieldDecorator(lengthTolTxt, lengthTolBounds);
		controlUseGenerator(comp, lengthTol,false);
		lengthTolTxt.setEnabled(false);
		
		//RegexDecorator based on elements
		//Search Configuration
		Label elementsLbl = new Label(this, SWT.NONE);
		elementsLbl.setText("Elements");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		this.elements =  new TextWrapper(comp, SWT.NONE);//TextFieldDecorator(elementsTxt, textBounds);

//		String invalidText = " ";
//		RegexDecorator regexSet = new RegexDecorator(elementsTxt, invalidText);
//		textBounds.setDelegate(regexSet);
		//TMp deactivated as regex did not work
		
		controlUseGenerator(comp, elements,false);
		
		
		Label sapcegroupLbl = new Label(this, SWT.NONE);
		sapcegroupLbl.setText("Spacegroup");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		this.spacegroup =  new TextWrapper(comp, SWT.NONE);//new TextFieldDecorator(spacegroupTxt, textBounds);
		
		controlUseGenerator(comp, spacegroup,false);
		
		
		Label refcodeLbl = new Label(this, SWT.NONE);
		refcodeLbl.setText("Refcode");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		this.refcode =  new TextWrapper(comp, SWT.NONE);//new TextFieldDecorator(refcodeTxt, textBounds);
		refcode.setTextLimit(6); //REFCODE is 6 constant letters
		
		controlUseGenerator(comp, refcode,false);
		
		
		Label ccdcNumLbl = new Label(this, SWT.NONE);
		ccdcNumLbl.setText("CCDC Number");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		
		this.ccdcNum =  new TextWrapper(comp, SWT.NONE);//new TextFieldDecorator(ccdcNumTxt, textBounds);
		controlUseGenerator(comp, ccdcNum,false);
		
		
		Label formulaLbl = new Label(this, SWT.NONE);
		formulaLbl.setText("Formula");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));

		this.formula =  new TextWrapper(comp, SWT.NONE);//new TextFieldDecorator(formulaTxt, textBounds);
		
		controlUseGenerator(comp, formula,false);
		
		
		Label associateNameLbl = new Label(this, SWT.NONE);
		associateNameLbl.setText("Associate Name");
		comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		this.associateName =  new TextWrapper(comp, SWT.NONE);//new TextFieldDecorator(associateNameTxt , textBounds);
		
		controlUseGenerator(comp, associateName,false);
	}

	

	
	private Composite controlUseGenerator(Composite comp, IFieldWidget data, boolean isOn){
		Button shouldSearch = new Button(comp, SWT.CHECK);
		shouldSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		shouldSearch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				  Button btn = (Button) e.getSource();
				  data.setEnabled(btn.getSelection());
				  data.off();
				  //data.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GR));
//				  data.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
//				  data.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
//				  data.redraw();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
		data.setEnabled(isOn);
		if(isOn) {
			data.on();
		} else {
			data.off();
		}
		
		shouldSearch.setSelection(isOn);
		return comp;
	}
	
	//Cell Configuration
	
	public IFieldWidget getAVal() {
		//TODO: the method to only get values if they are actively checked
		//Really just want to return do not change
		if(!aVal.isOn()){
			double valDefault = 5.4311946;
			aVal.setValue(valDefault );
			
			lengthTol.setValue(100);
			
		}
		return aVal;
	}
	
	public IFieldWidget getBVal() {
		if(!bVal.isOn()){
			double valDefault = 5.4311946;
			bVal.setValue(valDefault);
			
			lengthTol.setValue(100);
		}
		return bVal;
	}
	
	public IFieldWidget getCVal() {
		if(!cVal.isOn()){
			double valDefault = 5.4311946;
			cVal.setValue(valDefault );

			lengthTol.setValue(100);
			
		
		}
		
		return cVal;
	}
	
	public IFieldWidget getAlphaVal() {
		if(!alpVal.isOn()){
			double valDefault = 90.0;
			alpVal.setValue(valDefault);
		
			angleTol.setValue(180);
			
		}
		return alpVal;
	}
	
	public IFieldWidget getBetaVal() {
		if(!betaVal.isOn()){
			double valDefault = 90.0;
			betaVal.setValue(valDefault);
		
			angleTol.setValue(180);
		}
		return betaVal;
	}
	
	public IFieldWidget getGammaVal() {
		if(!gamVal.isOn()){
			double valDefault = 90.0;
			gamVal.setValue(valDefault);
			angleTol.setValue(180);
		}
		return gamVal;
	}

	
	//Search configuration
	/**
	 * @return all elements search on
	 */
	public IFieldWidget getElements() {
		if(!elements.isOn()){
			elements.setValue("");
		}
		return elements;
	}
	
	public IFieldWidget getSpacegroup(){
		if(!spacegroup.isOn()){
			spacegroup.setValue("");
		}
		return spacegroup;
	}
	
	public IFieldWidget getRefcode(){
		if(!refcode.isOn()){
			refcode.setValue("");
		}
		return refcode;
	}
	
	public IFieldWidget getCcdcNum() {
		if(!ccdcNum.isOn()){
			ccdcNum.setValue("");
		}
		return ccdcNum;
	}

	public IFieldWidget getFormula(){
		if(!formula.isOn()){
			formula.setValue("");
		}
		return formula;
	}

	public IFieldWidget getAssociateName() {
		if(!associateName.isOn()){
			associateName.setValue("");
		}
		return associateName;
	}

	
	public IFieldWidget getAbsoluteAngleTol() {
		if(!angleTol.isOn()){
			angleTol.setValue(0.0);
		}
		return angleTol;
	}

	public IFieldWidget getPercentageLengthTol() {
		if(!lengthTol.isOn()){
			lengthTol.setValue(0.0);
		}
		return lengthTol;
	}
	
	@Override
	public String toString() {
		return "TODO: string method";
	}

	
}