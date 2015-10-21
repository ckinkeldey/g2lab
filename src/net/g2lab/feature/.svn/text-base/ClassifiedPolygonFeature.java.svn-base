package net.g2lab.feature;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/** A polygon with id and classname attribute
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class ClassifiedPolygonFeature extends PolygonFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public static final String SIMPLE_FEATURE_TYPE = "id:String,*geom:Polygon,className:String";
	
	/**
	 */
	private String className;

	public ClassifiedPolygonFeature(String id, String className, Polygon polygon) {
		super(id, polygon);
		this.className = className;
		getBoundary(polygon);
	}

	public ClassifiedPolygonFeature(com.vividsolutions.jump.feature.Feature feature, String classAttribute) {
		super(feature);
		this.className = feature.getString(classAttribute);
	}
	
	public ClassifiedPolygonFeature(SimpleFeature simpleFeature, String classAttribute) {
		super(simpleFeature);
		String className = (String) simpleFeature.getAttribute(classAttribute);
		if (className != null) {
			this.className = className;
		} else {
			this.className = "";
		}
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
	}

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

	public Polygon getPolygon() {
		return this.polygon;
	}
	
	public String getObjectClass() {
		return className;
	}

	public void setObjectClass(String className) {
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see net.g2lab.feature.Feature#getAsSimpleFeature()
	 */
	@Override
	public SimpleFeature getAsSimpleFeature() {
		SimpleFeatureType type;
		try {
			type = DataUtilities.createType("feature", SIMPLE_FEATURE_TYPE);
			SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
			builder.add(this.id);
			builder.add(this.polygon);
			builder.add(this.className);
			return builder.buildFeature(id);
		} catch (SchemaException e) {
			LOG.error("could not create feature type: " + e, e);
			return null;
		}
	}

	
}
