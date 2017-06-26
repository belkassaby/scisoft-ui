package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.richbeans.api.binding.IBeanController;
import org.eclipse.richbeans.binding.BeanService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.UnitCellConfigComp;

/**
 * TODO: adjust to spawn peak finder widget
 * 
 * @author Dean P. Ottewell
 */
public class CellSearchConfigWizard extends WizardPage {

	private static final Logger logger = LoggerFactory.getLogger(CellSearchConfigWizard.class);
	
	//Tool common UI elements
	private Composite dialogContainer;
	
	private ICellSearchConfig configBean;
	
	IBeanController manager;
	UnitCellConfigComp cellView;
	
	public CellSearchConfigWizard(Composite parent, ICellSearchConfig configBean) {
		super("Configuration for cell search");
		this.setDescription("Cell search configuration for Cambridge Crystallographic Data Centre");
		this.setTitle("Cell Search Configuration");
		
		this.configBean = configBean;
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		this.setControl(parent);
	}
	
	@Override	
	public void createControl(Composite parent) {		
		dialogContainer = new Composite(parent, SWT.NONE);
		dialogContainer.setLayout(new GridLayout(1, true));
		dialogContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Composite
		cellView = new UnitCellConfigComp(dialogContainer, SWT.NONE);
		cellView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		
		// Connect the UI and bean
		//TODO: function which could throw a exception
		manager = BeanService.getInstance().createController(cellView, configBean);
		
	}
	

	
	
	public CellSearchConfig gatherConfiguration() {
		//Update on the current configuration of the bean
		try {
			manager.recordBeanFields();
			manager.fireValueListeners();
			manager.uiToBean();
			manager.switchState(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return (CellSearchConfig) manager.getBean();
    }
	
	@Override
	public boolean isPageComplete() {
		try {
			manager.beanToUI();
			manager.switchState(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		  	
        return true;
    }

}
