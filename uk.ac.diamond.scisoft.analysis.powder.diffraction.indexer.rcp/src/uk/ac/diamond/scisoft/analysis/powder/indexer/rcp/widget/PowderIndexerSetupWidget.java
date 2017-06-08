package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.widget;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.january.dataset.IDataset;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.Activator;
import uk.ac.diamond.scisoft.analysis.fitting.functions.IdentifiedPeak;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.StandardConstantParameters;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.IPowderCellListener;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.PowderIndexerManager;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.jobs.LightweightConsoleView;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.jobs.ProgressIndexerRun;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.preferences.PowderIndexerConstants;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream; 

/**
 * TODO the name left is only as reference that it'll be on the left
 * 
 * @author Dean P. Ottewell
 *
 */
public class PowderIndexerSetupWidget {
	
	private final Logger logger = LoggerFactory.getLogger(PowderIndexerSetupWidget.class);

	private Button runIndexing;
	
	private PowderIndexerManager manager;
	
	private IDataset peakPos;

	private Collection<String> indexersToRun ; 
	
	private Text maxABCTxt;
	
	private Label maxABCLb;
	
	private Text wavelengthTxt;
	
	private Text maximumVolumeTxt;
	
	private final String RUNINDEXINGTEXT = "Run Indexing";
	
	//TODO: put in preferences
	private Map<String, Boolean> shouldSearchSystems = new LinkedHashMap<String, Boolean>();
	
	
	public PowderIndexerSetupWidget(PowderIndexerManager manager) {
		this.manager = manager;
		
		indexersToRun = new ArrayList<String>();
		
		//TODO: change to preference setter
		shouldSearchSystems.put(StandardConstantParameters.cubicSearch, true);
		shouldSearchSystems.put(StandardConstantParameters.hexagonalSearch, true); 
		shouldSearchSystems.put(StandardConstantParameters.trigonalSearch,true); 
		shouldSearchSystems.put(StandardConstantParameters.tetragonalSearch, true);  
		shouldSearchSystems.put(StandardConstantParameters.orthorhombicSearch, true);
		shouldSearchSystems.put(StandardConstantParameters.monoclinicSearch,true);
		shouldSearchSystems.put(StandardConstantParameters.triclinicSearch, false); 
	}

	public void createControl(final Composite left) {
		
		Composite comp = new Composite(left, SWT.NONE);
		comp.setLayout(new GridLayout(1, true));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		
		BundleContext ctx = FrameworkUtil.getBundle(PowderIndexerSetupWidget.class).getBundleContext();
		ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
		
		EventHandler handler = new EventHandler() {

			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				//TODO: thread because adding the data might take long. However, I am using this to send a class... event
				
				String[] theObjects = event.getPropertyNames();
				
				
				List<IdentifiedPeak> peaks =  (List<IdentifiedPeak>) event.getProperty("PEAKRESULTS");
				manager.setPeakPosData(peaks);
				
			} 	
		};

		Dictionary<String,Object> properties = new Hashtable<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, "peakfinding/*");
		ctx.registerService(EventHandler.class, handler, properties);	
		
		IPowderCellListener listener = new IPowderCellListener() {

			@Override
			public void theChosenIndexer(String indexerID) {
				//TODO: grab the combo id
			}
			
			@Override
			public void isPowderSearching() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void finishedSearching() {
				// TODO Auto-generated method stub	
			}
	
			@Override
			public void peakDataChanged(IDataset nXData, IDataset nYData) {
				peakPos = nXData;
				if(canRunRoutine()){
					runIndexing.setEnabled(true);
					runIndexing.setText(RUNINDEXINGTEXT);
				}
			}

			@Override
			public void cellChanged(List<CellParameter> cell) {
				// TODO Auto-generated method stub
			}

			@Override
			public void cellsChanged(List<Crystal> crystals) {
				// TODO Auto-generated method stub
				
			}
		};
		manager.addCellListener(listener);
		
		
		Composite indexerConfiguration = new Composite(comp, SWT.NONE);
		indexerConfiguration.setLayout(new GridLayout(2, false));
		indexerConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
//		Composite searchConfigLeft = new Composite(indexerConfiguration, SWT.BORDER);
//		searchConfigLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		searchConfigLeft.setLayout(new GridLayout(2, false));
//		
		Group searchConfigLeft = new Group(indexerConfiguration , SWT.BORDER);
		searchConfigLeft.setText(" Parameters ");
		searchConfigLeft.setLayout(new GridLayout(2, false));
		searchConfigLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false)); //SWT.CENTER, SWT.FILL, true, false)
		searchConfigLeft.setBackground(left.getBackground()); 
		
		
		
		Label wavelengthLb = new Label(searchConfigLeft, SWT.NONE);
		wavelengthLb.setText("Wavelength" + "/ \u212B");
		wavelengthLb.setToolTipText("As shown by the second vertical line");
		
		wavelengthTxt = new Text(searchConfigLeft,  SWT.BORDER | SWT.RIGHT );
		wavelengthTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		wavelengthTxt.addListener(SWT.Verify, verifyDouble);
		
		wavelengthTxt.setText("1.560000");
		
		
		Label maximumVolumelb = new Label(searchConfigLeft, SWT.NONE);
		maximumVolumelb.setText("Maximum Volume" + " / \u212B" + "\u00B3");
		
		maximumVolumeTxt = new Text(searchConfigLeft,  SWT.BORDER  | SWT.RIGHT );
		maximumVolumeTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		maximumVolumeTxt.addListener(SWT.Verify, verifyDouble);
		
		maximumVolumeTxt.setText("5000.000");
		maximumVolumeTxt.setTextDirection(SWT.RIGHT_TO_LEFT);
		
		maxABCLb = new Label(searchConfigLeft, SWT.NONE);
		maxABCLb.setText("Maximum a,b,c / \u212B");
		
		maxABCTxt = new Text(searchConfigLeft,  SWT.BORDER | SWT.RIGHT );
		maxABCTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		maxABCTxt.addListener(SWT.Verify, verifyDouble);
		maxABCTxt.setText("35.000");
		
		Label minFigureMeritLb = new Label(searchConfigLeft, SWT.NONE);
		minFigureMeritLb.setText("Minimum figure-of-merit");
		
		Text minFigureMeritTxt = new Text(searchConfigLeft,  SWT.BORDER | SWT.RIGHT );
		minFigureMeritTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		minFigureMeritTxt.addListener(SWT.Verify, verifyDouble);

		minFigureMeritTxt.setText("10.000");

		
		
		Label crystalSelectionLb= new Label(searchConfigLeft, SWT.NONE);
		crystalSelectionLb.setText("Limit Crystal Searching");
		
		Button crystalselectioBtn = new Button(searchConfigLeft, SWT.NONE);
		crystalselectioBtn.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		crystalselectioBtn.setText("Selection");
		crystalselectioBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: pass crystal data
				CrystalSelectionDialog dialog = new CrystalSelectionDialog(comp.getShell(),shouldSearchSystems);
				dialog.open();
				shouldSearchSystems  = dialog.getCrystalChoice().getSearchLimtiation();
				manager.setCrystalSystemSearch(shouldSearchSystems);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Group indexerSelectionRight = new Group(indexerConfiguration , SWT.V_SCROLL);
		indexerSelectionRight.setText("  Indexing Routine  ");
		indexerSelectionRight.setLayout(new GridLayout(2, false));
		indexerSelectionRight.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		indexerSelectionRight.setBackground(left.getBackground()); 
		
		for(String id : PowderIndexerConstants.INDEXERS){
			indexersToRun.add(id);
			controlUseGenerator(indexerSelectionRight, id, true);
			Label textLb = new Label(indexerSelectionRight, SWT.NONE);
			textLb.setText(id);
			textLb.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		}

		
		indexerConfiguration.pack();
		
		
		//TODO: only show below if selected

		runIndexing = new Button(comp, SWT.PUSH);
		//runIndexing.setImage(Activator.getImage("icons/autoIndex.png"));
		runIndexing.setText("Load peak position data");
		runIndexing.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		runIndexing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (peakPos == null){
					logger.error("no data to index!");
					//TODO: generate a pop up to tell the user off!
				}
				
				//Load in the new configurations
				manager.setWavelength(Double.parseDouble(wavelengthTxt.getText()));
				manager.setMinFigureMerit(Double.parseDouble(minFigureMeritTxt.getText()));
				manager.setMaxABC(Double.parseDouble(maxABCTxt.getText()));
				manager.setMaximumVolume(Double.parseDouble(maximumVolumeTxt.getText()));
				
				
				ProgressMonitorDialog dia;// = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
				
				dia = new IndexerProgressDialog(Display.getCurrent().getActiveShell());
				
				
				if (indexersToRun.size()!= 0 ){
					logger.error("no indexer to run!");
				}
				//TODO: properly choose a selection to run and iterate over all of them
				for(String shouldRun : indexersToRun) {
					
						ProgressIndexerRun job = new ProgressIndexerRun(shouldRun, "tmptst", manager,peakPos);
						
						try {
							dia.run(true, true, job);

						
						} catch (InvocationTargetException e1) {	
							logger.error(e1.getMessage());
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "Peak indexing Error", "An error occured during auto indexing!" + System.lineSeparator() +
									"Check the data being indexed is valid." + System.lineSeparator()
									 + "Specific error :" + e1.getTargetException().getMessage());
							
						} catch (InterruptedException e1) {
							logger.error("Error running Job:" + e1.getMessage());
						}
				}
				
				
			}	
		});
		runIndexing.setEnabled(false); //Disabled until peak data loaded
	}
	
	//TODO: hacks, need to review my control generator use. Must be a generic version somewhere I call upon.
	private Composite controlUseGenerator(Composite comp, String identifier, boolean isSelected){
		Button shouldSearch = new Button(comp, SWT.CHECK);
		shouldSearch.setSelection(isSelected);
		shouldSearch.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false));
		shouldSearch.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(shouldSearch.getSelection()){
					indexersToRun.add(identifier);
//					wavelengthVal.setEnabled(true);
					
					//Check if parameter is available, enable if not 
//					IPowderIndexer indexer = PowderIndexerFactory.createIndexer(identifier);
//					indexer.getInitialParamaters();
//					
					//TODO: check through parameters and see if matches are there
					
				} else { 
					indexersToRun.remove(identifier);
				}
				
				runIndexing.setEnabled(canRunRoutine());
					
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		return comp;
	}
	
	
	private Listener verifyDouble = new Listener() {

		@Override
		public void handleEvent(Event e) {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9' || chars[i] == '.')) {
					e.doit = false;
					return;
				}
			}
		}
	};
	
	private Listener verifyInt = new Listener() {

		@Override
		public void handleEvent(Event e) {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9')) {
					e.doit = false;
					return;
				}
			}
		}
	};
	
	
	private Boolean canRunRoutine(){
		return 	(indexersToRun.size() >= 1 && peakPos != null);
	}

	
	
	
	private void consoleStreamView() {
		
		//MessageDialog dia = new MessageDialog(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex)
		MessageConsole console = new MessageConsole("IndexerStream", null, null, true);
		
		MessageConsoleStream stream = new MessageConsoleStream(console);
		
		 ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
	}
}

class IndexerProgressDialog extends ProgressMonitorDialog {

	private MessageConsole messageConsole; 
	
	public IndexerProgressDialog(Shell parent) {
		super(parent);
		//this.getShell().setText("Determining Structure");
		//this.getShell().setImage( Activator.getImage("icons/powderIndexing.png"));
	}

	
	@Override
	protected Image getImage() {
		Image icon = Activator.getImage("icons/powderIndexing.png");
		//Maginfiy
		icon.getImageData().scaledTo(1000, 1000);
		return icon;
	}
	
	@Override
	protected void finishedRun() {
		// TODO Auto-generated method stub
		//Do not close but change the button
		Button cancel = getCancelButton();
		cancel.setText("Finish");
	
		//super.finishedRun();
	}
	
	
	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		super.okPressed();
	}
	
	
	@Override
	protected void cancelPressed() {
		decrementNestingDepth(); //TODO: this will break things
		super.cancelPressed();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		this.getShell().setText("Determining Structure");
		this.getShell().setImage( Activator.getImage("icons/powderIndexing.png"));
//		// initialize the dialog units
//		initializeDialogUnits(parent);
//		Point defaultSpacing = LayoutConstants.getSpacing();
////		GridLayoutFactory.fillDefaults().margins(LayoutConstants.getMargins())
////				.spacing(defaultSpacing.x * 2,
////				defaultSpacing.y).numColumns(this.getColumnCount()).applyTo(parent);
//
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
//
////		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI);
////		text.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false));
//
//		
//		createDialogAndButtonArea(parent);
//
//		
		super.createContents(parent);
		
//		Text outputArea = new Text(parent, SWT.V_SCROLL);
//		outputArea.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true));
//		outputArea.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
//		outputArea.setText("I am a test string");
//		outputArea.setEditable(false);
		
	
		return parent;
	}
	
	private MessageConsole getMessageConsole() {
		if (messageConsole == null) {
			messageConsole = new MessageConsole("CONSOLE_NAME", null);
			ConsolePlugin.getDefault().getConsoleManager()
					.addConsoles(new IConsole[] { messageConsole });
		}
		return messageConsole;
	}

	
//	public void createcontrol(parent) {
//		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI);
//		OutputStream out = new OutputStream() {
//			@Override
//			public void write(int b) throws IOException {
//				if (text.isDisposed())
//					return;
//				text.append(String.valueOf((char) b));
//			}
//		};
//		
//		final PrintStream oldOut = System.out;
//		System.setOut(new PrintStream(out));
//		text.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent e) {
//				System.setOut(oldOut);
//			}
//		});
//	
//	})
	
}
