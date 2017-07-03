package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.widgets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.CCDCService;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.CellSearchManager;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.jobs.CellSearchRun;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.listeners.ICellSearchListener;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.ICellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.richbean.CellSearchConfig;
import uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp.wizards.CellSearchConfigWizard;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Lattice;

/**
 * 
 * TODO:viewer not clear the buttons I made...
 * TODO: grab the logs of the Python to determine where the problem in the configuration lies
 * 
 * @author Dean P. Ottewell
 *
 */
public class CellSearchWidget {
	
	private final Logger logger = LoggerFactory.getLogger(CellSearchWidget.class);

	private CellSearchManager manager;
	private TableViewer viewer;
	private List<TableViewerColumn> ret;
	private CellSearchConfig sConfig;
	
	private List<CellSearchConfig> searchMatches;
	
	//Buttons
	Button runSearch;
	Button configSearch;
	Button saveCif;
	
	//Server CCDC status
	Boolean searcherStatus;
	
	public CellSearchWidget(CellSearchManager manager) {
		this.manager = manager;
		//Force python configuration here
		//CCDCService searchService = new CCDCService();
		searcherStatus = true;//searchService.serverAvaliable();
	}

	public void createControl(final Composite parent) {
				
		BundleContext ctx = FrameworkUtil.getBundle(CellSearchWidget.class).getBundleContext();
		ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
		
		EventHandler handler = new EventHandler() {

			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				// TODO Auto-generated method stub
				//TODO: thread because adding the data might take long. However, I am using this to send a class... event
				
				String[] theObjects = event.getPropertyNames();
				
				Object obj = event.getProperty("INDEXERRESULTS");
				
				Lattice receiveLattice = (Lattice) event.getProperty("INDEXERRESULTS");
				
				if(receiveLattice != null) {
					CellSearchConfig configBean = new CellSearchConfig(receiveLattice);
					
					
					//Setting everything to prevent a complain
					configBean.setElements("H"); //TODO:Open elements table for this
					configBean.setRefcode("ARAVIZ");
					configBean.setFormula("Y1 3+,3(H4 B1 1-)");
					configBean.setSpacegroup("Pm-3m");
					configBean.setChemicalName("Yttrium tris(tetrahydridoborate");
					
					configBean.setAbsoluteAngleTol(1);
					configBean.setPercentageLengthTol(1);

					configBean.setCcdcNum("0x");
					
					sConfig = configBean;
				}
			} 	
		};

		Dictionary<String,Object> properties = new Hashtable<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, "powderanalysis/*");
		ctx.registerService(EventHandler.class, handler, properties);	
		
		Group searcher = new Group(parent, SWT.FILL);
		searcher.setText(" Cell Search CCDC ");
		searcher.setLayout(new GridLayout(3, false));
		searcher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		searcher.setBackground(parent.getBackground()); 
		
		configSearch = new Button(searcher, SWT.PUSH);
		configSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		configSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(sConfig == null){
				
					/*TMP DEFAULT SET*/
					// Tmp: Set some initial values. Theses values match perfectly to ARAVIZ silcon sample
					CellSearchConfig configBean = new CellSearchConfig(5.4767, 5.4767, 5.4767, 90.0, 90.0, 90.0);

					configBean.setElements("H"); //TODO:Open elements table for this
					configBean.setRefcode("ARAVIZ");
					configBean.setFormula("Y1 3+,3(H4 B1 1-)");
					configBean.setSpacegroup("Pm-3m");
					configBean.setChemicalName("Yttrium tris(tetrahydridoborate");
					
					//Setting everything to prevent a complain
					configBean.setAbsoluteAngleTol(1);
					configBean.setPercentageLengthTol(1);
					
					configBean.setCcdcNum("0x");
					
					sConfig = configBean;
				}
				
				CellSearchConfigWizard configPage = new CellSearchConfigWizard(parent, sConfig);
				
				final Wizard wiz = new Wizard() {
					//set 
					@Override
					public boolean performFinish() {
						CellSearchConfigWizard configPage = (CellSearchConfigWizard) this.getStartingPage();
						CellSearchConfig configBean = configPage.gatherConfiguration();
						sConfig = configBean;
						//Alert of the new search configuration parameter
						manager.loadSearchConfig(configBean);
						
						return true;
					}
				};
				
				wiz.setNeedsProgressMonitor(true);
				wiz.addPage(configPage);

				final WizardDialog wd = new WizardDialog(parent.getShell(),wiz);
				wd.setPageSize(new Point(500, 350));
				wd.create();
				wd.getCurrentPage();
				if (wd.open() == WizardDialog.OK);
			}	
		});
		
		if(searcherStatus) {
			configSearch.setText("Search...");
			configSearch.setEnabled(searcherStatus); //Disabled until peak data loaded
			//configSearch.setImage(Activator.getImage("icons/autoIndex.png"));
		} else {
			configSearch.setText("Configuration");
			configSearch.setEnabled(searcherStatus); //Disabled until peak data loaded
		}
		
		runSearch = new Button(searcher, SWT.PUSH);
		runSearch.setText("Run");
		runSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		runSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSearchRun();
			}	
		});
		runSearch.setEnabled(false); //Disabled until configured
		

		
		saveCif = new Button(searcher, SWT.PUSH);
		saveCif.setText("Save as CIF");
		saveCif.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		saveCif.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: save all those that are checked

				CCDCService searchService = new CCDCService();
				
				List<ICellSearchConfig> cellsConfig= (List<ICellSearchConfig>) viewer.getInput();
				int[] selected = viewer.getTable().getSelectionIndices();
				for (int i : selected){
					ICellSearchConfig cell = cellsConfig.get(i);
					
					//System.getProperty("java.io.tmpdir")
					searchService.generateRefcodeCif("/tmp/", cell.getRefcode());
					
				}

			}	
		});
		saveCif.setEnabled(false); //Disabled until peak data loaded
		
		
		
		ICellSearchListener listener = new ICellSearchListener() {
			
			@Override
			public void updateSearchConfig(CellSearchConfig searchConfig) {
				sConfig = searchConfig;
				runSearch.setEnabled(true);
				saveCif.setEnabled(true);
			}
			
			@Override
			public void perfromSearch() {
				// TODO Auto-generated method stub
			}

			@Override
			public void loadSearchMatches(List<CellSearchConfig> searchConfig) {
				searchMatches = searchConfig;
				
				viewer.refresh();
				
				//TODO:viewer clear more?
				
				viewer.setInput(searchMatches);
				viewer.refresh();
				//Set save cif active
				saveCif.setEnabled(true);
			}

			@Override
			public void updateSearchCrystalConfig(Crystal searchCrystal) {
				//sConfig.setSearchCrysal(searchCrystal);
			}
		};
		
		manager.addSearchListener(listener);
		
		ProgressMonitorDialog dia = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		
		
		//TODO: method is slightly invasive...
		if(!searcherStatus){
			IconAndMessageDialog alert = new IconAndMessageDialog(Display.getCurrent().getActiveShell()) {
				
				@Override
				protected Image getImage() {
					// TODO Auto-generated method stub
					return null;
				}
			};
			alert.open();
		}
		
		//Now create viewer
		createTableControl(parent);
	}
	
	public void handleSearchRun(){
		ProgressMonitorDialog dia = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		
		CellSearchRun job = new CellSearchRun(manager,sConfig);
		try {
			dia.run(true, true, job);
			//TODO: grab results on finish
		} catch (InvocationTargetException e1) {	
			logger.error(e1.getMessage());
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Cell Search Error", "An error occurred during a search run!" + System.lineSeparator() +
					 "Specific error :" + e1.getMessage());
			
		} catch (InterruptedException e1) {
			logger.error("Unconfigured Cell Searcher Job:" + e1.getMessage());
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Cell Searcher Configuration", "An problem in cell searcher configuration has preventing the searcher from running" + System.lineSeparator() +
					 "Problem Area:" + e1.getMessage());
			
		} 
		//Should be able to just gather the results
		//"Check the data being configured is valid." + System.lineSeparator()
	}
	
	public void createTableControl(Composite parent) {
		viewer = new TableViewer(parent,
				 SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
		ret = createColumns(viewer);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.getTable().setSize(1000, 100);
		viewer.setContentProvider(new ArrayContentProvider());
		
		//TODO: data content
		//viewer.setInput(manager.getCellData());
		//manager.setResultsView(viewer);

		viewer.refresh();
	}
	
	public List<TableViewerColumn> createColumns(final TableViewer viewer) {
		
		List<TableViewerColumn> ret = new ArrayList<TableViewerColumn>();
		TableViewerColumn table = new TableViewerColumn(viewer, SWT.LEFT, 0);
		table.getColumn().setWidth(80);
		table.getColumn().setText("REFCODE");

		table.setLabelProvider(new ColumnLabelProvider() {
        Map<Object, Button> refcodeLink = new HashMap<Object, Button>();
    	
	        @Override
	        public void update(ViewerCell cell) {
	
	            TableItem item = (TableItem) cell.getItem();
	            Button openReport;
	            openReport = new Button((Composite) cell.getViewerRow().getControl(),SWT.NONE);
	        	openReport.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
	        	refcodeLink.put(cell.getElement(), openReport);
	        	
	        	Object element = cell.getElement();
	        	
	        	CellSearchConfig unitCell = (CellSearchConfig) element;
	        	
	        	openReport.setText(String.valueOf(unitCell.getRefcode()));
	        	
	        	openReport.addListener(SWT.Selection, new Listener() {
					
					@Override
					public void handleEvent(Event event) {
						 switch (event.type) {
	        	          case SWT.Selection:

	        	            CCDCService searcher = new CCDCService();
	        	            searcher.generateRefcodeReport("/tmp/", unitCell.getRefcode());
	        	            org.eclipse.swt.program.Program.launch("/tmp/" + unitCell.getRefcode()+".html");
	        	            //TODO: open in browser editor

	        	            Browser browse = new Browser(Display.getCurrent().getActiveShell(), SWT.NONE);
	        	            browse.setUrl("/tmp/" + unitCell.getRefcode()+".html");
	        	            
	        	            break;
	        	          }
	        	        }
				});
	        	
	            TableEditor editor = new TableEditor(item.getParent());
	            editor.grabHorizontal  = true;
	            editor.grabVertical = true;
	            editor.setEditor(openReport , item, cell.getColumnIndex());
	            editor.layout();
	        }
	        
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getRefcode());
			}
		
		});

//		table.setLabelProvider(new ColumnLabelProvider() {
//		@Override
//		public String getText(Object element) {
//			CellSearchConfig cell = (CellSearchConfig) element;
//			return String.valueOf(cell.getRefcode());
//		}
//	});
		ret.add(table);
		
		//ret = createCellParamColumns(viewer,ret);
		table = new TableViewerColumn(viewer, SWT.LEFT, 1);
		table.getColumn().setText("a" + " / " + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				double a = cell.getA();
				return String.valueOf(a);
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.LEFT, 2);
		table.getColumn().setText("b" + " / " + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getB());
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.LEFT, 3);
		table.getColumn().setText("c" + " / "
				+ "" + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getC());
			}
		});
		ret.add(table);
		
		//Alpha
		table = new TableViewerColumn(viewer, SWT.LEFT, 4);
		table.getColumn().setText("\u03B1" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getAl());
			}
		});
	
		ret.add(table);
		
		//beta
		table = new TableViewerColumn(viewer, SWT.LEFT, 5);
		table.getColumn().setText("\u03B2" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getBe());
			}
		});
		ret.add(table);
		
		//Gamma
		table = new TableViewerColumn(viewer, SWT.LEFT, 6);
		table.getColumn().setText("\u03B3" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getGa());
			}
		});
		ret.add(table);
		
		
		
		table = new TableViewerColumn(viewer, SWT.LEFT, 7);
		table.getColumn().setText("Formula");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellSearchConfig cell = (CellSearchConfig) element;
				return String.valueOf(cell.getFormula());
			}
		});
		ret.add(table);
		

		table = new TableViewerColumn(viewer, SWT.LEFT, 8);
		table.getColumn().setText("Save CIF");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new CheckerLabelProvider());
		ret.add(table);
		

		return ret;
	}
	
	
	private class CheckerLabelProvider extends ColumnLabelProvider {

        public Map<Object, Button> buttons = new HashMap<Object, Button>();

        @Override
        public void update(ViewerCell cell) {
            TableItem item = (TableItem) cell.getItem();

            Button button = new Button((Composite) cell.getViewerRow().getControl(),SWT.CHECK);
        	button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        	buttons.put(cell.getElement(), button);
        	button.addSelectionListener(new SelectionAdapter()
        	{
        	    @Override
        	    public void widgetSelected(SelectionEvent e)
        	    {
        	    	cell.setText(Boolean.toString(button.getSelection()));
        	    }
        	});
        	     	
            TableEditor editor = new TableEditor(item.getParent());
            editor.grabHorizontal  = true;
            editor.grabVertical = true;
            editor.setEditor(button , item, cell.getColumnIndex());
            editor.layout();
        }
        
        @Override
		public String getText(Object element) {
        	Button state = buttons.get(element);
        	
			return state.getText();
		}
	}
	
}
