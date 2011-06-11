package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl;

import org.openstreetmap.osmosis.core.store.StoreClassRegister;
import org.openstreetmap.osmosis.core.store.StoreReader;
import org.openstreetmap.osmosis.core.store.StoreWriter;
import org.openstreetmap.osmosis.core.store.Storeable;

import com.vividsolutions.jts.geom.Coordinate;

public class NodeInfo implements Storeable {
	
	private long id;
	private Coordinate coordinates;
	
	public NodeInfo(long id, double lat, double lon) {
		this.id = id;
		this.coordinates = new Coordinate(lon, lat);
	}
	
	public NodeInfo(StoreReader sr, StoreClassRegister scr) {
		this.id = sr.readLong();
		double x = sr.readDouble();
		double y = sr.readDouble();
		this.coordinates = new Coordinate(x, y);
	}

	@Override
	public void store(StoreWriter sw, StoreClassRegister arg1) {
		sw.writeLong(id);
		sw.writeDouble(coordinates.x);
		sw.writeDouble(coordinates.y);
	}
	
	public long getId() {
		return id;
	}

	public Coordinate getCoordinates() {
		return coordinates;
	}
}
