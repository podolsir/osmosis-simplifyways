package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.pipeline.v0_6.SinkSourceManager;

public class WaySimplifierFactory extends TaskManagerFactory {

	@Override
	protected TaskManager createTaskManagerImpl(
			TaskConfiguration taskConfiguration) {
		return new SinkSourceManager(taskConfiguration.getId(),
				new WaySimplifier(), taskConfiguration.getPipeArgs());
	}

}
