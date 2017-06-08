package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.views;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.january.dataset.IDataset;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import uk.ac.diamond.scisoft.analysis.powder.indexer.indexers.CellParameter;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.IPowderCellListener;
import uk.ac.diamond.scisoft.analysis.powder.indexer.rcp.PowderIndexerManager;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;

/**
 * Widget for displaying cell units value.
 * 
 * @author Dean P. Ottewell
 */
public class CellParameterDelegate {		
	
	private Composite composite;
	
	private TableViewer viewer;

	private PowderIndexerManager controller;
	
	private String powderIndexerID;
	
	public CellParameterDelegate(PowderIndexerManager controller){
		this.controller = controller;
	}
	
	/**
	 * @param parent
	 */
	public void createControl(Composite parent) {
		this.composite = parent;
		
		//TODO: Comparator
		viewer = new TableViewer(this.composite,
				 SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		createColumns(viewer);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setContentProvider(new ArrayContentProvider());
		
		//TODO: add manager listeners 
		//TODO: id the listener...
		IPowderCellListener listener = new IPowderCellListener() {
			
			@Override
			public void theChosenIndexer(String indexerID) {
				powderIndexerID = indexerID;
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
				// TODO Auto-generated method stub
			}

			@Override
			public void cellChanged(List<CellParameter> cell) {
				viewer.setInput(cell);
			}

			@Override
			public void cellsChanged(List<Crystal> crystals) {
				// TODO Auto-generated method stub
				
			}
			
		
		};
		controller.addCellListener(listener);
		
		viewer.refresh();
	}
	
	//TODO: eally belongs somewhere else ... like a action button delegate
	//TODO: need delete table values action
	private Action exportCellAction(){
		return new Action("Export Cells", IAction.AS_PUSH_BUTTON) {
			public void run() {
				//TODO:maybe have a wizard for this 
			}
		};
		//Setup activator correctly. probably want a *.indexer.rcp and change that 
		//export.setImageDescriptor(Activator.getImageDescriptor("icons/mask-export-wiz.png"));
	}
	
	public List<TableViewerColumn> createColumns(final TableViewer viewer) {
		
		List<TableViewerColumn> ret = new ArrayList<TableViewerColumn>(8);
		
		TableViewerColumn table = new TableViewerColumn(viewer, SWT.CENTER, 0);
		
		table.getColumn().setText("Indexer");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return cell.getIndexerIdentifer().toString();
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.CENTER, 1);
		table.getColumn().setText("a" + " / " + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element;
				return Double.toString(cell.getA());
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.CENTER, 2);
		table.getColumn().setText("b" + " / " + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return Double.toString(cell.getB());
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.CENTER, 3);
		table.getColumn().setText("c" + " / "
				+ "" + "\u212B");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return Double.toString(cell.getC());
			}
		});
		ret.add(table);
		
		//Alpha
		table = new TableViewerColumn(viewer, SWT.CENTER, 4);
		table.getColumn().setText("\u03B1" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return Double.toString(cell.getAl());
			}
		});
		ret.add(table);
		
		//beta
		table = new TableViewerColumn(viewer, SWT.CENTER, 5);
		table.getColumn().setText("\u03B2" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return Double.toString(cell.getBe());
			}
		});
		ret.add(table);
		
		//Gamma
		table = new TableViewerColumn(viewer, SWT.CENTER, 6);
		table.getColumn().setText("\u03B3" + " / " + "\u00B0");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				return Double.toString(cell.getGa());
			}
		});
		ret.add(table);
		
		//Gamma
		table = new TableViewerColumn(viewer, SWT.CENTER, 7);
		table.getColumn().setText("Volume" + " / \u212B" + "\u00B3");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 
				
				Double vol = cell.getLattice().getVolume();
				return Double.toString(vol);
			}
		});
		ret.add(table);
		
		table = new TableViewerColumn(viewer, SWT.CENTER, 8);
		table.getColumn().setText("Figure of Merit");
		table.getColumn().setWidth(80);
		table.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CellParameter cell = (CellParameter) element; 

				return cell.getFigureMerit().toString();
			}
		});
		ret.add(table);
		
		
//		table = new TableViewerColumn(viewer, SWT.LEFT, 7);
//		table.getColumn().setText("Crystal System");
//		table.getColumn().setWidth(80);
//		table.setLabelProvider(new ColumnLabelProvider() {
//			@Override
//			public String getText(Object element) {
//				CellParameter cell = (CellParameter) element; 
//
//				return cell.getCrystalSystem().toString();
//			}
//		});
//		ret.add(table);
//		
		

		for( int i = 0; i < ret.size(); i++ )
		    table.getColumn().pack();
		
		return ret;
	}
	
	public void setFocus() {
		if (viewer!=null && !viewer.getControl().isDisposed()) viewer.getControl().setFocus();
	}

	public void refresh() {
		viewer.refresh();
	}
	
}
