package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp;

import org.dawnsci.plotting.views.ToolPageView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;



/**
 * @author Dean P. Ottewell
 *
 */
public class PowderAnalysisPerspective implements IPerspectiveFactory {

	public static final String ID = "uk.ac.diamond.scisoft.diffraction.powder.rcp.PowderAnalysisPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
//		String editorArea = layout.getEditorArea();
//		layout.setEditorAreaVisible(false);
//	
//		IFolderLayout navigatorFolder = layout.createFolder("navigator-folder", IPageLayout.LEFT, 0.15f, editorArea);
//		navigatorFolder.addView("org.eclipse.ui.navigator.ProjectExplorer");
//		navigatorFolder.addView("org.dawnsci.fileviewer.FileViewer");
//		
//		
//		
//		String fixed = ToolPageView.FIXED_VIEW_ID + ":";
//
//		String peakFindingToolID = "org.dawnsci.plotting.tools.finding.PeakFindingTool";
//		String powderIndexerID = "uk.ac.diamond.scisoft.analysis.powder.diffraction.indexer.rcp.views.PowderIndexingRoutine";
//		String searchMatcherID = "uk.ac.diamond.scisoft.diffraction.powder.matcher.ccdc.rcp.views.CellSearchRoutineView";
//		
//		
//		layout.addView(powderIndexerID, IPageLayout.RIGHT, 0.4f, IPageLayout.ID_EDITOR_AREA);
//		// Top Left: Peak Finding Tool View
////		IFolderLayout left = layout.createFolder("peakfindingTool", IPageLayout.LEFT, 0.30f, editorArea);
////		left.addView(fixed + peakFindingToolID);
////		
//		// Bottom Left: Powder Indexer Routine
////		IFolderLayout bottomLeft = layout.createFolder("powderIndexingRoutine", IPageLayout.BOTTOM, 0.50f, editorArea);
////		bottomLeft.addView(powderIndexerID);
//
////		// Bottom Right: Powder Matcher 
////		IFolderLayout bottomRight = layout.createFolder("powderMatcherSearch", IPageLayout.BOTTOM, 0.50f, editorArea);
////		bottomRight.addView(searchMatcherID);
//
////		layout.getViewLayout(fixed + peakFindingToolID).setCloseable(false);
////		layout.getViewLayout(powderIndexerID).setCloseable(false);
//		//layout.getViewLayout(searchMatcherID).setCloseable(false);
//		IViewLayout powderView = layout.getViewLayout(powderIndexerID);
//		powderView.setCloseable(false);
	}

}
