package de.vwistuttgart.openstreetmap.osmosis.simplifyways.v0_6.impl;

import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

public class DouglasPeuckerSimplifier {

	private GeometryFactory geometryFactory;

	public List<NodeInfo> simplify(List<NodeInfo> nodes) {
		geometryFactory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.FLOATING), 4326);
		boolean[] vector = new boolean[nodes.size()];
		Arrays.fill(vector, true);
		douglasPeucker(nodes, vector, 0, nodes.size(), 0.01d);
		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (!vector[i]) {
				nodes.remove(i);
			}
		}
		return nodes;
	}

	private void douglasPeucker(List<NodeInfo> nodes,
			boolean[] inclusionVector, int start, int end, double epsilon) {
		double dmax = 0;
		int index = 0;

		NodeInfo first = nodes.get(start);
		NodeInfo last = nodes.get(end - 1);
		LineString ls = geometryFactory.createLineString(new Coordinate[] {
				first.getCoordinates().getCoordinate(),
				last.getCoordinates().getCoordinate() });

		for (int i = start + 1; i < end - 1; i++) {
			NodeInfo p = nodes.get(i);
			double distance = p.getCoordinates().distance(ls);
			if (distance > dmax) {
				dmax = distance;
				index = i;
			}
		}

		if (dmax >= epsilon) {
			douglasPeucker(nodes, inclusionVector, start, index, epsilon);
			douglasPeucker(nodes, inclusionVector, index, end, epsilon);
		} else {
			for (int i = start + 1; i < end - 1; i++) {
				inclusionVector[i] = false;
			}
		}
	}
}
