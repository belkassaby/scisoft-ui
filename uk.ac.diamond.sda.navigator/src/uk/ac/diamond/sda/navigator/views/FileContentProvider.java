package uk.ac.diamond.sda.navigator.views;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.ui.PlatformUI;

import uk.ac.gda.common.rcp.util.EclipseUtils;
import uk.ac.gda.util.io.SortingUtils;

public class FileContentProvider implements ILazyTreeContentProvider {

	public enum FileSortType {
		ALPHA_NUMERIC, ALPHA_NUMERIC_DIRS_FIRST;
	}
	
	private TreeViewer treeViewer;
	private FileSortType sort = FileSortType.ALPHA_NUMERIC_DIRS_FIRST;
    private BlockingQueue<UpdateRequest> queue;

	/**
	 * Caching seems to be needed to keep the path sorting
	 * fast. 
	 * NOTE Is there a better way of doing this.
	 */
	private Map<File, List<File>> cachedSorting;

	public FileContentProvider() {
		this.cachedSorting = new WeakHashMap<File, List<File>>(89);
		this.queue         = new LinkedBlockingDeque<UpdateRequest>(89);
	}

	
	@Override
	public void dispose() {
		cachedSorting.clear();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		treeViewer = (TreeViewer) viewer;
		treeViewer.refresh();
	}

	@Override
	public void updateElement(Object parent, int index) {

		if (PlatformUI.isWorkbenchRunning()) {
			createUpdateElementJobIfNotAlreadyGoing();
			queue.add(new UpdateRequest(parent, index));
		} else {
			final File node = (File) parent;
			final List<File> fa = getFileList(node);
			updateElementInternal(node, index, fa);
		}
	}
	
	private Job updateElementJob;
	
	/**
	 * Somewhat long method name, but you get the idea.
	 */
	private void createUpdateElementJobIfNotAlreadyGoing() {
		
		if (updateElementJob==null) {
			updateElementJob = new Job("Update directory contents") {
				
				private Cursor busy = treeViewer.getControl().getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					while(true) {
                        try {

                    		final UpdateRequest req = queue.take();
                    		treeViewer.getControl().getDisplay().syncExec(new Runnable() {
								@Override
								public void run() {
									treeViewer.getControl().setCursor(busy);
								}
                			});

                			final File node = (File) req.getElement();
                			final List<File> fa = getFileList(node);
                			
                			treeViewer.getControl().getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									updateElementInternal(node, req.getIndex(), fa);
								}
                			});

                        } catch (Exception ne) {
                        	queue.clear();
                        	continue;
                        } finally {
                        	treeViewer.getControl().getDisplay().syncExec(new Runnable() {
								@Override
								public void run() {
									treeViewer.getControl().setCursor(null);
								}
                			});
                        }
					}
				}	
			};
			updateElementJob.setSystem(true);
			updateElementJob.setUser(false);
			updateElementJob.schedule(100);

		}
	}


	public void updateElementInternal(Object parent, int index, List<File> fa) {
		
		
		if (fa!=null && index < fa.size()) {
			File element = fa.get(index);
			treeViewer.replace(parent, index, element);
			updateChildCount(element, -1);
		}
	}

	@Override
	public void updateChildCount(Object element, int currentChildCount) {
		
		final File node = (File) element;
		if (node==null) return;
		
		final File[] fa = node.listFiles();
		if (fa!=null) {
			int size = fa.length;
			treeViewer.setChildCount(element, size);
		} else {
		
		    treeViewer.setChildCount(element, 0);
		}
		
	}

	private List<File> getFileList(File node) {
		
		if (!node.isDirectory()) return null;
		
		if (sort==FileSortType.ALPHA_NUMERIC) {
			final File[] fa = node.listFiles();
			if (fa==null) return null;
			return Arrays.asList(fa);
		}
		
		if (cachedSorting.containsKey(node)) {
			return cachedSorting.get(node);
		}
		
		// Simulate Lustreless
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		final List<File> sorted;
		if (sort==FileSortType.ALPHA_NUMERIC) {
			sorted = SortingUtils.getSortedFileList(node, false);
		} else {
			sorted = SortingUtils.getSortedFileList(node, true);
		}
		cachedSorting.put(node, sorted);
		return sorted;
	}


	@Override
	public Object getParent(Object element) {
		if (element==null || !(element instanceof File)) {
			return null;
		}
		final File node = ((File) element);
		return node.getParentFile();
	}


	public FileSortType getSort() {
		return sort;
	}


	public void setSort(FileSortType sort) {
		this.sort = sort;
	}
	
	private class UpdateRequest {
		private Object element;
		private int index;

		public Object getElement() {
			return element;
		}

		public void setElement(Object element) {
			this.element = element;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		UpdateRequest(final Object element, final int index) {
			this.element = element;
			this.index   = index;
		}
	}

}