package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Polygon;

public class UncertaintyDoublePolygonFeature extends PolygonFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public static final String ATTRIBUTE_UNCERTAINTY = "u";
	
	/**
	 */
	private double uncertainty = -1.0;
	
	public UncertaintyDoublePolygonFeature(SimpleFeature simpleFeature) {
		super(simpleFeature);
		try {
		this.uncertainty = (Double) simpleFeature.getAttribute(ATTRIBUTE_UNCERTAINTY);
		} catch(Exception e) {
			LOG.error("Cannot read uncertainty attribute: " +e, e);
		}
	}
	
	public UncertaintyDoublePolygonFeature(String id, Polygon polygon, double uncertainty) {
		super(id, polygon);
		this.uncertainty = uncertainty;
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
	public void setUncertainty(double uncertainty) {
		this.uncertainty = uncertainty;
	}
	
	/* (non-Javadoc)
	 * @see net.g2lab.feature.PolygonFeature#getFeatureTypeString()
	 */
	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:Polygon," + ATTRIBUTE_UNCERTAINTY + ":Double";
	}

	/* (non-Javadoc)
	 * @see net.g2lab.feature.Feature#initBuilder(org.geotools.feature.simple.SimpleFeatureBuilder)
	 */
	@Override
	protected void initSimpleFeatureBuilder(SimpleFeatureBuilder builder) {
		super.initSimpleFeatureBuilder(builder);
		builder.add(uncertainty);
	}
	
	

}
