package de.vwistuttgart.openstreetmap.osmosis.simplifyways;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.plugin.PluginLoader;

import de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.WaySimplifierFactory;

/**
 * Plugin loader for the simplify ways tasks.
 * 
 * @author Igor Podolskiy
 *
 */
public class SimplifyWaysPluginLoader implements PluginLoader{

	@Override
	public Map<String, TaskManagerFactory> loadTaskFactories() {
		Map<String, TaskManagerFactory> map = new HashMap<String, TaskManagerFactory>();
		map.put("simplify-ways", new WaySimplifierFactory());
		map.put("simplify-ways-0.6", new WaySimplifierFactory());
		return map;
	}

}
