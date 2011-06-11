package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl;

import org.openstreetmap.osmosis.core.store.StoreClassRegister;
import org.openstreetmap.osmosis.core.store.StoreReader;
import org.openstreetmap.osmosis.core.store.StoreWriter;
import org.openstreetmap.osmosis.core.store.Storeable;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * A lightweight object that stores the information that is needed for way
 * simplification.
 * 
 * @author Igor Podolskiy
 */
public class NodeInfo implements Storeable {

	private long id;
	private Coordinate coordinates;

	/**
	 * Creates a new node information structure.
	 * 
	 * @param id the node id
	 * @param lat the node latitude
	 * @param lon the node longitude
	 */
	public NodeInfo(long id, double lat, double lon) {
		this.id = id;
		this.coordinates = new Coordinate(lon, lat);
	}

	/**
	 * Infrastructure constructor for deserialization from intermediate store.
	 * 
	 * @param sr
	 * @param scr
	 */
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

	/**
	 * Returns this node's id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns this nodes coordinates.
	 * 
	 * @return the coordinates.
	 */
	public Coordinate getCoordinates() {
		return coordinates;
	}
}
