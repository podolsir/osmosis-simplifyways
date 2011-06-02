package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl;

import org.openstreetmap.osmosis.core.store.StoreClassRegister;
import org.openstreetmap.osmosis.core.store.StoreReader;
import org.openstreetmap.osmosis.core.store.StoreWriter;
import org.openstreetmap.osmosis.core.store.Storeable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class NodeInfo implements Storeable {
	
	private static final ThreadLocal<GeometryFactory> geometryFactory = new ThreadLocal<GeometryFactory>() {
		protected GeometryFactory initialValue() {
			return new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);			
		};
	};
	
	private long id;
	private Point coordinates;
	
	public NodeInfo(long id, Point coordinates) {
		this.id = id;
		this.coordinates = coordinates;
	}
	
	public NodeInfo(StoreReader sr, StoreClassRegister scr) {
		this.id = sr.readLong();
		double x = sr.readDouble();
		double y = sr.readDouble();
		this.coordinates = geometryFactory.get().createPoint(new Coordinate(x, y));
	}

	@Override
	public void store(StoreWriter sw, StoreClassRegister arg1) {
		sw.writeLong(id);
		sw.writeDouble(coordinates.getX());
		sw.writeDouble(coordinates.getY());
	}
	
	public long getId() {
		return id;
	}

	public Point getCoordinates() {
		return coordinates;
	}
}
