package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityProcessor;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.store.IndexedObjectStore;
import org.openstreetmap.osmosis.core.store.IndexedObjectStoreReader;
import org.openstreetmap.osmosis.core.store.NoSuchIndexElementException;
import org.openstreetmap.osmosis.core.store.SingleClassObjectSerializationFactory;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.task.v0_6.SinkSource;

import de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl.DouglasPeuckerSimplifier;
import de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl.NodeInfo;

/**
 * Simplifies way geometry by running a Douglas-Peucker algorithm over them with
 * a specififed precision.
 * 
 * The current implementation does not cache the data in an intermediate store.
 * 
 * Required stream order: nodes, then ways.
 * Output stream order: ways, then nodes.
 * 
 * 
 * @author Igor Podolskiy
 */
public class WaySimplifier implements SinkSource, EntityProcessor {

	private Sink sink;
	private IndexedObjectStore<NodeInfo> nodeStore;
	private IndexedObjectStoreReader<NodeInfo> nodeStoreReader;
	private DouglasPeuckerSimplifier simplifier;

	private static final long SPHEROID_RADIUS = 6371000;

	/**
	 * Creates a new way simplifier.
	 * 
	 * @param epsilonMeters
	 *            the maximum acceptable deviation from the original way.
	 */
	public WaySimplifier(double epsilonMeters) {
		nodeStore = new IndexedObjectStore<NodeInfo>(
				new SingleClassObjectSerializationFactory(NodeInfo.class),
				"wsn");

		double epsilonDegrees = epsilonMeters * 180 / (SPHEROID_RADIUS * Math.PI);
		simplifier = new DouglasPeuckerSimplifier(epsilonDegrees);
	}

	@Override
	public void process(EntityContainer entityContainer) {
		entityContainer.process(this);
	}

	@Override
	public void complete() {
		sink.complete();
	}

	@Override
	public void release() {
		if (nodeStoreReader != null) {
			nodeStoreReader.release();
		}
		nodeStore.release();
		sink.release();
	}

	@Override
	public void setSink(Sink sink) {
		this.sink = sink;
	}

	@Override
	public void process(BoundContainer boundContainer) {
		sink.process(boundContainer);
	}

	@Override
	public void process(NodeContainer nodeContainer) {
		Node n = nodeContainer.getEntity();
		nodeStore.add(n.getId(), new NodeInfo(n.getId(), n.getLatitude(), n.getLongitude()));
		sink.process(nodeContainer);
	}

	@Override
	public void process(WayContainer wayContainer) {
		if (nodeStoreReader == null) {
			nodeStore.complete();
			nodeStoreReader = nodeStore.createReader();
		}

		List<WayNode> wayNodes = wayContainer.getEntity().getWayNodes();
		List<NodeInfo> realWayNodes = new ArrayList<NodeInfo>(wayNodes.size());
		for (WayNode wayNode : wayNodes) {
			try {
				realWayNodes.add(nodeStoreReader.get(wayNode.getNodeId()));
			} catch (NoSuchIndexElementException e)
			{
			}
		}
		
		simplifier.simplify(realWayNodes);
		wayContainer.getEntity().getWayNodes().clear();
		for (NodeInfo nodeInfo : realWayNodes) {
			wayContainer.getEntity().getWayNodes().add(new WayNode(nodeInfo.getId()));
		}

		sink.process(wayContainer);
	}

	@Override
	public void process(RelationContainer relationContainer) {
		sink.process(relationContainer);
	}

	@Override
	public void initialize(Map<String, Object> metaData) {
	}
}
