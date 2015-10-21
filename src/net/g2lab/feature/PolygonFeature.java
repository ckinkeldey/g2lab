package net.g2lab.feature;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/** 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class PolygonFeature extends Feature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	/**
	 */
	protected LinearRing outerRing;
	/**
	 */
	protected Collection<LinearRing> innerRings;
	/**
	 */
	protected Polygon polygon;

	public PolygonFeature(String id, Polygon polygon) {
		super(id);
		getBoundary(polygon);
	}
	
	public PolygonFeature(com.vividsolutions.jump.feature.Feature feature) {
		super("" + feature.getID());
		getBoundary(feature.getGeometry());
	}

	public PolygonFeature(SimpleFeature simpleFeature) {
		super(simpleFeature.getID());
		getBoundary((Geometry) simpleFeature.getDefaultGeometry());
	}

	private void getBoundary(Geometry geometry) {
		this.polygon = (Polygon) geometry.getGeometryN(0);
		this.outerRing = (LinearRing) polygon.getExteriorRing();
		this.innerRings = new HashSet<LinearRing>();
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			LinearRing inner = (LinearRing) polygon.getInteriorRingN(i);
			addInnerPolygon(inner);
		}
		this.geometry = polygon;
	}

	/**
	 * @return
	 */
	public LinearRing getOuterRing() {
		return outerRing;
	}

	public void addInnerPolygon(LinearRing inner) {
		this.innerRings.add(inner);
	}

	public Collection<LinearRing> getInnerRing() {
		return innerRings;
	}

	public Boolean hasInnerRing() {
		return this.innerRings.size() > 0;
	}

	/**
	 * @return
	 */
	public Polygon getPolygon() {
		return this.polygon;
	}

	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:Polygon";
	}

}
