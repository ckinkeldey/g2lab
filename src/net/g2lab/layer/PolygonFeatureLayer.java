package net.g2lab.layer;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.g2lab.feature.Feature;
import net.g2lab.feature.PartialBoundaries;
import net.g2lab.feature.PolygonFeature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A layer containing a number of features. A layer must not be empty.
 * 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class PolygonFeatureLayer extends AbstractLayer {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public static final String BOUNDARY_CLASS_ID = "-1";

	/**
	 */
	protected Map<String, PolygonFeature> featureMap;
	/**
	 */
	private Map<String, Collection<String>> adjacencyMap;

	/**
	 */
	private LinearRing boundingBox;

	public PolygonFeatureLayer(Collection<PolygonFeature> features) {
		if (features == null || features.size() == 0) {
			LOG.error("cannot create layer from empty features.");
		}
		this.featureMap = new HashMap<String, PolygonFeature>();
		this.adjacencyMap = new HashMap<String, Collection<String>>();
		addFeatures(features);
	}
	
	@Override
	public Envelope getEnvelope() {
		return boundingBox.getEnvelopeInternal();
	}
	
	/**
	 * @return the featureMap
	 */
	public Map<String, PolygonFeature> getFeatureMap() {
		return featureMap;
	}

	/**
	 * @param featureMap the featureMap to set
	 */
	public void setFeatureMap(Map<String, PolygonFeature> featureMap) {
		this.featureMap = featureMap;
	}

	public Feature getFeature(String id) {
		return this.featureMap.get(id);
	}

	/**
	 * @return
	 */
	public LinearRing getBoundingBox() {
		return boundingBox;
	}

	public Collection<String> getNeighbours(Feature feature) {
		return this.adjacencyMap.get(feature.getId());
	}

	private void addFeatures(Collection<PolygonFeature> features) {
		LOG.info("adding features...");
		for (PolygonFeature feature : features) {
			this.featureMap.put(feature.getId(), feature);
		}
		computeBoundingBox();
//		computeAdjacencies();
	}

	private void computeBoundingBox() {
		LOG.info("computing bounding box...");
		Geometry[] geometries = new Geometry[featureMap.size()];
		int i = 0;
		for (PolygonFeature feature : this.featureMap.values()) {
			// LOG.debug("...computing bounding box for feature " +
			// feature.getId() + ".");
			geometries[i++] = ((Geometry) feature.getOuterRing());
		}
		GeometryCollection allBoundaries = new GeometryCollection(geometries, new GeometryFactory());
		this.boundingBox = (LinearRing) ((Polygon) allBoundaries.getEnvelope()).getExteriorRing();
		Envelope env = boundingBox.getEnvelopeInternal();
		LOG.info("bounding box: (" + env.getMinX() + " " + env.getMinY() + " " + env.getMaxX() + " " + env.getMaxY() + ")");
	}
	
	public Integer getIntAttribute(String attributeName, double x, double y) {
		Point point = new GeometryFactory().createPoint(new Coordinate(x, y));
		for (PolygonFeature feature : this.featureMap.values()) {
			SimpleFeature simpleFeature = feature.getAsSimpleFeature();
			boolean intersects = point.intersects(feature.getPolygon());
			Object attribute = simpleFeature.getAttribute(attributeName);
			if (simpleFeature != null && intersects && attribute != null) {
				return (Integer)attribute;
			}
		}
		LOG.error("no feature found in point " + point);
		return -1;
	}
	
	public Double getDoubleAttribute(String attributeName, double x, double y) {
		Point point = new GeometryFactory().createPoint(new Coordinate(x, y));
		for (PolygonFeature feature : this.featureMap.values()) {
			SimpleFeature simpleFeature = feature.getAsSimpleFeature();
			boolean intersects = point.intersects(feature.getPolygon());
			Object attribute = simpleFeature.getAttribute(attributeName);
			if (simpleFeature != null && intersects && attribute != null) {
				return (Double) attribute;
			}
		}
		LOG.error("no feature found in point " + point);
		return -1.;
	}

	
	public String getStringAttribute(String attributeName, double x, double y) {
		Point point = new GeometryFactory().createPoint(new Coordinate(x, y));
		for (PolygonFeature feature : this.featureMap.values()) {
			if (point.intersects(feature.getPolygon())) {
				return (String) feature.getAsSimpleFeature().getAttribute(attributeName);
			}
		}
		return "";
	}

	public void computeAdjacencies() {
		LOG.info("computing topology for " + this.featureMap.keySet().size() + " features.");
		Iterator<String> iter = this.featureMap.keySet().iterator();
		int n = 0;
		while (iter.hasNext()) {
			PolygonFeature feature = featureMap.get(iter.next());
			Collection<String> ids = computeAdjacencies(feature);
			this.adjacencyMap.put(feature.getId(), ids);
			if (++n % 10 == 0) {
//				LOG.debug("computed topology for feature " + feature.getId() + " (" + n + " / " + this.featureMap.keySet().size() + ")");
			}
		}
	}

	private Collection<String> computeAdjacencies(PolygonFeature feature) {
		Geometry geom = (Geometry) feature.getPolygon();

		Geometry boundary = geom.getBoundary();
		Collection<String> neighbours = new HashSet<String>();
		if (boundary.intersects(boundingBox)) {
			neighbours.add(BOUNDARY_CLASS_ID);
		}
		for (PolygonFeature otherFeature : this.featureMap.values()) {
			Geometry otherGeom = (Geometry) otherFeature.getPolygon();

			if (feature.equals(otherFeature)) {
				continue;
			} else if (geom.intersects(otherGeom)) {
				neighbours.add(otherFeature.getId());
			}
		}
		return neighbours;
	}
	
	public Collection<PartialBoundaries> createSubBoundaries() {
		Collection<PartialBoundaries> allSubBoundaries = new HashSet<PartialBoundaries>();
		for (String id : featureMap.keySet()) {
			PolygonFeature feature = featureMap.get(id);
			List<PartialBoundaries> subBounds = splitUpBoundaries(feature);
			allSubBoundaries.addAll(subBounds);
		}
		return allSubBoundaries;
	}
	
	private List<PartialBoundaries> splitUpBoundaries(PolygonFeature feature) {
		List<PartialBoundaries> boundaries = new ArrayList<PartialBoundaries>();
		Polygon polygon = feature.getPolygon();
		addBoundary(feature, boundaries, polygon.getExteriorRing(), feature
				.getId() + "." + "0", true);
		// inner rings
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			addBoundary(feature, boundaries, polygon.getInteriorRingN(i),
					feature.getId() + "." + (i + 1), false);
		}
		return boundaries;
	}

	private void addBoundary(PolygonFeature feature, List<PartialBoundaries> boundaries,
			Geometry geom, String boundaryId, boolean outer) {
		PartialBoundaries boundary = new PartialBoundaries(boundaryId);
		boundary.setOuter(outer);
		Collection<String> neighbourIDs = adjacencyMap.get(feature.getId());
		for (String neighbourId : neighbourIDs) {
			Geometry neighGeom;
			if (neighbourId.compareTo(BOUNDARY_CLASS_ID) == 0) {
				boundary.putBoundary(neighbourId, geom
						.intersection(boundingBox));
			} else {
				PolygonFeature neighbour = this.featureMap.get(neighbourId);
				neighGeom = (Polygon) neighbour.getPolygon();
				boundary.putBoundary(neighbourId, geom.intersection(neighGeom));
			}

		}
		boundaries.add(boundary);
	}
	
	public Image getAsImage(int width, int height) {
		// TODO
		return null;
	}


}
