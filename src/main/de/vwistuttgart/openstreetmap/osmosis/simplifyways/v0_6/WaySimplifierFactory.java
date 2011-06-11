package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.pipeline.v0_6.SinkSourceManager;

/**
 * Instatiates way simplifiers using the parameters on the command line.
 * 
 * @author Igor Podolskiy
 */
public class WaySimplifierFactory extends TaskManagerFactory {

	private static final String ARG_EPSILON_METERS = "epsilonMeters";
	private static final double DEFAULT_EPSILON_METERS = 0.1;

	@Override
	protected TaskManager createTaskManagerImpl(
			TaskConfiguration taskConfiguration) {
		
		double epsilonMeters = getDoubleArgument(taskConfiguration,
				ARG_EPSILON_METERS, DEFAULT_EPSILON_METERS);
		
		return new SinkSourceManager(taskConfiguration.getId(),
				new WaySimplifier(epsilonMeters),
				taskConfiguration.getPipeArgs());
	}

}
