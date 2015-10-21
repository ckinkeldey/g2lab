package net.g2lab.feature;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A geographic feature consisting of an id and a geometry.
 * @author  <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 */
public abstract class Feature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	/**
	 */
	protected String id;
	/**
	 */
	protected Geometry geometry;
	
	public Feature(String id) {
		this.id = id;
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return  the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}
	
	public SimpleFeature getAsSimpleFeature() {
		SimpleFeatureType type;
		try {
			type = DataUtilities.createType("feature", getFeatureTypeString());
			SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
			initSimpleFeatureBuilder(builder);
			return builder.buildFeature(id);
		} catch (SchemaException e) {
			LOG.error("could not create feature type: " + e, e);
			return null;
		}
	}

	/**
	 */
	public abstract String getFeatureTypeString();
	
	protected void initSimpleFeatureBuilder(SimpleFeatureBuilder builder) {
		builder.add(this.id);
		builder.add(this.geometry);
	}
	
	public static Collection<SimpleFeature> getAsSimpleFeatures(Collection<Feature> features) {
		Collection<SimpleFeature> simpleFeatures = new HashSet<SimpleFeature>(features.size());
		for (Feature feature : features) {
			simpleFeatures.add(feature.getAsSimpleFeature());
		}
		return simpleFeatures;
	}

	
}
