package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import java.util.ArrayList;
import java.util.List;

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
import org.openstreetmap.osmosis.core.store.SingleClassObjectSerializationFactory;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.task.v0_6.SinkSource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl.DouglasPeuckerSimplifier;
import de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl.NodeInfo;

public class WaySimplifier implements SinkSource, EntityProcessor {

	private Sink sink;
	private IndexedObjectStore<NodeInfo> nodeStore;
	private IndexedObjectStoreReader<NodeInfo> nodeStoreReader;
	private GeometryFactory geometryFactory;
	private DouglasPeuckerSimplifier simplifier;

	public WaySimplifier(double epsilonMeters) {
		nodeStore = new IndexedObjectStore<NodeInfo>(
				new SingleClassObjectSerializationFactory(NodeInfo.class),
				"wsn");

		geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
		double degrees = epsilonMeters * 180 / (6371000 * Math.PI);
		simplifier = new DouglasPeuckerSimplifier(degrees);
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
		Point point = geometryFactory.createPoint(new Coordinate(n.getLongitude(), n.getLatitude()));
		nodeStore.add(n.getId(), new NodeInfo(n.getId(), point));
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
			realWayNodes.add(nodeStoreReader.get(wayNode.getNodeId()));
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

}
