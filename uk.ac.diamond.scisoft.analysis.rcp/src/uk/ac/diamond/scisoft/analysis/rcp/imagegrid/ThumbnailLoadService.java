/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package uk.ac.diamond.scisoft.analysis.rcp.imagegrid;

import gda.observable.IObserver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.diamond.scisoft.analysis.PlotServer;
import uk.ac.diamond.scisoft.analysis.PlotServerProvider;
import uk.ac.diamond.scisoft.analysis.plotserver.DataBean;
import uk.ac.diamond.scisoft.analysis.plotserver.DatasetWithAxisInformation;
import uk.ac.diamond.scisoft.analysis.plotserver.FileOperationBean;
import uk.ac.diamond.scisoft.analysis.plotserver.GuiBean;
import uk.ac.diamond.scisoft.analysis.plotserver.GuiParameters;
import uk.ac.diamond.scisoft.analysis.utils.ImageThumbnailLoader;


/**
 *
 */
public class ThumbnailLoadService implements Runnable, IObserver {
	transient private static final Logger logger = LoggerFactory.getLogger(ThumbnailLoadService.class);
	
	private boolean terminate = false;
	private boolean localProcessing;
	private ArrayDeque<AbstractGridEntry> highPriorityQueue;
	private ArrayDeque<AbstractGridEntry> lowPriorityQueue;
	private PlotServer plotServer;
	private ArrayList<String> files = new ArrayList<String>();
	private AbstractGridEntry currentProcessEntry = null;
	private final Semaphore locker = new Semaphore(1);
	private String viewName;
	
	public ThumbnailLoadService(String viewName) {
		highPriorityQueue = new ArrayDeque<AbstractGridEntry>();
		lowPriorityQueue = new ArrayDeque<AbstractGridEntry>();
		plotServer = PlotServerProvider.getPlotServer();
		plotServer.addIObserver(this); //FIXME: potential race condition
		try {
			localProcessing = plotServer.isServerLocal();
		} catch (Exception e) {
			// cannot happen but is needed for interface
		}
		this.viewName = viewName;
	}
	
	@Override
	public void run() {
		while (!terminate) {
			
			AbstractGridEntry entry = null;
			synchronized(highPriorityQueue) {
				if (highPriorityQueue.size() > 0)	
					entry = highPriorityQueue.pop();
			}
			if (entry != null)
				processJob(entry);
			else {
				synchronized(lowPriorityQueue) {
					if (lowPriorityQueue.size() > 0)
						entry = lowPriorityQueue.pop();
				}
				if (entry != null) processJob(entry);
				else {
					try {
						synchronized (this) {
							wait();
						}
					} catch (InterruptedException ex) {
					} 
				}
			}
		}
	}
	
	private void undoBlock() {
		locker.release();
	}
	
	private void requestImageFromServer(AbstractGridEntry entry) {
		GuiBean bean = new GuiBean();
		files.clear();
		FileOperationBean fopBean = new FileOperationBean(FileOperationBean.GETIMAGEFILE_THUMB);
		files.add(entry.getFilename());
		fopBean.setFiles(files);
		bean.put(GuiParameters.FILEOPERATION, fopBean);
		try {
			plotServer.updateGui(viewName, bean);
		} catch (Exception e) {
			locker.release();
			e.printStackTrace();
		}
	}
	
	private void loadAndCreateThumbnailImage(AbstractGridEntry entry) {
		IDataset ds = ImageThumbnailLoader.loadImage(entry.getFilename(),true, false);
		entry.createImage(ds);
		locker.release();
	}
	
	private void processJob(AbstractGridEntry entry) {
		try {
			locker.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentProcessEntry = entry;
		if (entry instanceof SWTGridEntry) {
			if (!((SWTGridEntry)entry).hasThumbnailImage()) {
				if (!localProcessing) 
					requestImageFromServer(entry);
				else
					loadAndCreateThumbnailImage(entry);
			} else {
				((SWTGridEntry)entry).loadThumbImage();
				try {
					Thread.sleep(30);
				} catch (InterruptedException ex) {}
				locker.release();
			}
		}		
	}
	public synchronized void addLoadJob(AbstractGridEntry entry, boolean highPriority) {
		notify();
		if (highPriority) {
			synchronized(highPriorityQueue) {
				highPriorityQueue.add(entry);
			} 
		} else {
			synchronized(lowPriorityQueue) {
				lowPriorityQueue.add(entry);
			}
		}
	}

	public synchronized void clearLowPriorityQueue() {
		synchronized(lowPriorityQueue) {
			lowPriorityQueue.clear();
		}
	}
	
	public synchronized void clearHighPriorityQueue() {
		synchronized(highPriorityQueue) {
			highPriorityQueue.clear();			
		}
	}
	
	@Override
	public void update(Object source, Object changeCode) {
		if (changeCode instanceof String && 
			changeCode.equals(viewName)) {
			DataBean dbPlot;
			try {
				dbPlot = plotServer.getData(viewName);
				Collection<DatasetWithAxisInformation> plotData = dbPlot.getData();
				Iterator<DatasetWithAxisInformation > iter = plotData.iterator();
				while (iter.hasNext())
				{
					DatasetWithAxisInformation dsAxis = iter.next();
					Dataset ds = dsAxis.getData();
					if (ds.getName().equals(currentProcessEntry.getFilename()))
					{
						currentProcessEntry.createImage(ds);
					} else {
						logger.error("Oops, no match between dataset {} and current entry {}", ds.getName(),
								currentProcessEntry.getFilename());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				undoBlock();
			}
		}
	}
	
	public synchronized void shutdown() {
		notify();
		synchronized(highPriorityQueue) {
			highPriorityQueue.clear();
		}
		synchronized(lowPriorityQueue) {
			lowPriorityQueue.clear();
		}
		plotServer.deleteIObserver(this);
		terminate = true;
	}
}
