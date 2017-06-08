package uk.ac.diamond.scisoft.analysis.powder.indexer.rcp;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "uk.ac.diamond.scisoft.analysis.powder.indexer.rcp";
    
	private static Activator staticActivator;
	/**
	 * The constructor
	 */
	public Activator() {
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
        staticActivator = this;
    }
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		staticActivator = null;
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return staticActivator;
	}
	
	
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin("uk.ac.diamond.scisoft.analysis.powder.indexer.rcp", path);
    }
    		 
    public static Image getImage(String path) {
        return getImageDescriptor(path).createImage();
    }
 
	public static ILog getPluginLog() {
		return staticActivator.getLog();
	}

	/**
	 * Used for transient local properties that are not designed to be read outside.
	 * @return
	 */
	public static IPreferenceStore getLocalPreferenceStore() {
		return staticActivator.getPreferenceStore();
	}
	
	static BundleContext getContext() {
		return staticActivator.getContext();
	}

	public static <T> T getService(Class<T> serviceClass) {
		ServiceReference<T> ref = staticActivator.getBundle().getBundleContext().getServiceReference(serviceClass);
		return staticActivator.getBundle().getBundleContext().getService(ref);
	}

	
}