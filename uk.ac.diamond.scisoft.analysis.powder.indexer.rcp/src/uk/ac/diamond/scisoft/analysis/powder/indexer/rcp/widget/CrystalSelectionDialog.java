package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.widget;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views.CrystalSelectionComp;

public class CrystalSelectionDialog extends Dialog {

	private CrystalSelectionComp crystalChoice;
	
	private Map<String, Boolean> shouldSearchSystems = new HashMap<String, Boolean>();
	
    public CrystalSelectionDialog(Shell parentShell,Map<String,Boolean> intialSearchSystems ) {
        super(parentShell);
        this.shouldSearchSystems  = intialSearchSystems;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
    	crystalChoice = new CrystalSelectionComp(container, SWT.NONE, shouldSearchSystems);
    	return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 250);
    }
    
	 @Override
	protected void okPressed() {
		super.okPressed();
	}

	public CrystalSelectionComp getCrystalChoice() {
		return crystalChoice;
	}

	
	 
}