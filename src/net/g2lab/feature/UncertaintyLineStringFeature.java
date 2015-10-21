package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.Feature;

public class UncertaintyLineStringFeature extends LineStringFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	private int uncertainty = -1;

	private SimpleFeatureType simpleFeatureType;

	public UncertaintyLineStringFeature(String id, LineString lineString, int uncertainty) throws SchemaException {
		super(id, lineString);
		this.uncertainty = uncertainty;
		this.simpleFeatureType = DataUtilities.createType("feature", getFeatureTypeString());
	}
	
	public UncertaintyLineStringFeature(Feature feature, String uncertAttribute) throws SchemaException {
		this(feature.getID()+"", getLineString(feature), extractUncertainty(feature, uncertAttribute));
	}

	private static int extractUncertainty(Feature feature, String uncertAttribute) {
		return (Integer) feature.getAttribute(uncertAttribute);
	}

	private static LineString getLineString(Feature feature) {
		return (LineString) feature.getGeometry();
	}

	/**
	 * @return  the uncertainty
	 */
	public double getUncertainty() {
		return uncertainty;
	}

	/**
	 * @param uncertainty  the uncertainty to set
	 */
	public void setUncertainty(int uncertainty) {
		this.uncertainty = uncertainty;
	}
	
	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:LineString,u:Integer";
	}
	
	/* (non-Javadoc)
	 * @see net.g2lab.feature.Feature#getAsSimpleFeature()
	 */
	@Override
	public SimpleFeature getAsSimpleFeature() {
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeatureType);
		builder.add(this.id);
		builder.add(this.geometry);
		builder.add(this.uncertainty);
		return builder.buildFeature(id);
	}
	

}
