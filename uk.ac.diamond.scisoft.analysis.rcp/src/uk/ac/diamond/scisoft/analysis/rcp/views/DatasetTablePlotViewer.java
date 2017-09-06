/*-
 * Copyright 2017 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.diamond.scisoft.analysis.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.dawnsci.plotting.api.IPlottingSystemViewer;
import org.eclipse.dawnsci.plotting.api.trace.ITableDataTrace;
import org.eclipse.dawnsci.plotting.api.trace.ITrace;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DatasetTablePlotViewer extends IPlottingSystemViewer.Stub<Composite> {

	private DatasetTableComposite table;
	
	@Override
	public void createControl(final Composite parent) {
		table = new DatasetTableComposite(parent, SWT.None);
	}
	
	
	@Override
	public boolean addTrace(ITrace trace){
		if (trace instanceof ITableDataTrace) {
			ITableDataTrace t = (ITableDataTrace)trace;
			
			table.setData(t.getData(),null,null);
			return true;
		}
		return false;
	}
	
	@Override
	public void removeTrace(ITrace trace) {
		
	}
	
	public Composite getControl() {
		if (table == null) return null;
		return table;
	}
	
	@Override
	public  <U extends ITrace> U createTrace(String name, Class<? extends ITrace> clazz) {
		if (clazz == ITableDataTrace.class) {

			return null;
		}
		return null;
	}
	
	@Override
	public boolean isTraceTypeSupported(Class<? extends ITrace> trace) {
		if (ITableDataTrace.class.isAssignableFrom(trace)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Collection<Class<? extends ITrace>> getSupportTraceTypes() {
		List<Class<? extends ITrace>> l = new ArrayList<>();
		l.add(ITableDataTrace.class);
		return l;
	}
	
}
