package uk.ac.diamond.scisoft.analysis.powder.matcher.ccdc.rcp;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.january.dataset.IDataset;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Crystal;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.CrystalSystem;
import uk.ac.diamond.scisoft.analysis.powder.indexer.crystal.Lattice;

public class GenericPowderIndexerEventExamples {
	
	static BundleContext ctx;
	static ServiceReference<EventAdmin> ref;
	
	
	public GenericPowderIndexerEventExamples() {
		//TODO: something to do in activator?
		GenericPowderIndexerEventExamples.ctx = FrameworkUtil.getBundle(GenericPowderIndexerEventExamples.class).getBundleContext();
        GenericPowderIndexerEventExamples.ref = ctx.getServiceReference(EventAdmin.class);
	}
	
	
	public void sendIndexerResults(){
		GenericPowderIndexerEventExamples.ctx = FrameworkUtil.getBundle(GenericPowderIndexerEventExamples.class).getBundleContext();
        GenericPowderIndexerEventExamples.ref = ctx.getServiceReference(EventAdmin.class);
        
		EventAdmin eventAdmin = ctx.getService(ref);
        //The object in this case being a list of cell parametr
		Map<String,Object> properties = new HashMap<String, Object>();
		
		List<Crystal> cells = new ArrayList<Crystal>();
		
		Lattice latt = new Lattice(0, 0, 0, 0, 0, 0);
		Crystal cell =new Crystal(latt); 
		cells.add(cell);
		//TODO: where to put this trigger line?
        properties.put("INDEXERRESULTS", cells);
        
        //Going to be triggered on a button and would like to know its arrived. However, should check before beforeing this action the 
        //view is active... If thats the case maybe based to have it async...
        Event event = new Event("powderanalysis/syncEvent", properties);
        eventAdmin.sendEvent(event);

	}
	
	public void receiveIndexerResults(){
		EventHandler handler = new EventHandler() {
			public void handleEvent(final Event event) {

				//TODO: thread because adding the data might take long. However, I am using this to send a class... event
					
				String[] theObjects = event.getPropertyNames();
				
				Object obj = event.getProperty("INDEXERRESULTS");
				
				Crystal receiveObj = (Crystal) event.getProperty("INDEXERRESULTS");

			} 	
		};

		Dictionary<String,Object> properties = new Hashtable<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, "powderanalysis/*");
		ctx.registerService(EventHandler.class, handler, properties);	
	}	
}
