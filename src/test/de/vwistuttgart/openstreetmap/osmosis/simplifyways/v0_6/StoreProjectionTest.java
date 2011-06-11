package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import de.vwistuttgart.openstreetmap.osmosis.test.task.v0_6.SinkEntityInspector;

public class StoreProjectionTest {

	private Iterable<EntityContainer> createWay(long id, double... coords) {
		List<EntityContainer> result = new LinkedList<EntityContainer>();
		Way way = new Way(id, 1, new Date(), OsmUser.NONE, 1);
		for (int i = 0; i < coords.length; i += 2) {
			NodeContainer nc = new NodeContainer(new Node(i, 1, new Date(), 
					OsmUser.NONE, 1, coords[i], coords[i+1]));
			result.add(nc);
			way.getWayNodes().add(new WayNode(i));
		}
		result.add(new WayContainer(way));
		return result;
	}
	
	@Test
	public void simplify() {
		SinkEntityInspector sei = new SinkEntityInspector();
		WaySimplifier sut = new WaySimplifier(1);
		sut.setSink(sei);
		for (EntityContainer i : createWay(100, 1d, 1d, 2d, 2d, 3d, 3d)) {
			sut.process(i);
		}		
		sut.complete();
		sut.release();
		
		Way way = (Way) sei.getLastEntityContainer().getEntity();
		Assert.assertEquals(2, way.getWayNodes().size());
	}
	
	@Test
	public void noSimplify() {
		SinkEntityInspector sei = new SinkEntityInspector();
		WaySimplifier sut = new WaySimplifier(1);
		sut.setSink(sei);
		for (EntityContainer i : createWay(100, 1d, 1d, 5d, 5d, 3d, 3d)) {
			sut.process(i);
		}		
		sut.complete();
		sut.release();
		
		Way way = (Way) sei.getLastEntityContainer().getEntity();
		Assert.assertEquals(3, way.getWayNodes().size());
	}
	
}
