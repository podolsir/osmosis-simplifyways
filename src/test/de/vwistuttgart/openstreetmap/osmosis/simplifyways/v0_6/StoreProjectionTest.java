package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import java.util.Date;

import org.junit.Test;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.misc.v0_6.NullWriter;

public class StoreProjectionTest {

	@Test
	public void simple() {
		WaySimplifier sut = new WaySimplifier(1);
		sut.setSink(new NullWriter());
		sut.process(new NodeContainer(new Node(1, 1, new Date(), OsmUser.NONE, 1, 1d, 1d)));
		sut.process(new NodeContainer(new Node(2, 1, new Date(), OsmUser.NONE, 1, 2d, 2.01d)));
		sut.process(new NodeContainer(new Node(3, 1, new Date(), OsmUser.NONE, 1, 3d, 3d)));
		Way way = new Way(100, 1, new Date(), OsmUser.NONE, 1);
		way.getWayNodes().add(new WayNode(1));
		way.getWayNodes().add(new WayNode(2));
		way.getWayNodes().add(new WayNode(3));
		sut.process(new WayContainer(way));
		sut.complete();
		sut.release();
	}
	
}
