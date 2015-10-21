package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Polygon;

public class UncertaintyIntPolygonFeature extends PolygonFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public static final String ATTRIBUTE_UNCERTAINTY = "u";
	
	/**
	 */
	private int uncertainty = -1;
	
	public UncertaintyIntPolygonFeature(SimpleFeature simpleFeature) {
		super(simpleFeature);
		try {
		this.uncertainty = (Integer) simpleFeature.getAttribute(ATTRIBUTE_UNCERTAINTY);
		} catch(Exception e) {
			LOG.error("Cannot read uncertainty attribute: " +e, e);
		}
	}
	
	public UncertaintyIntPolygonFeature(String id, Polygon polygon, int uncertainty) {
		super(id, polygon);
		this.uncertainty = uncertainty;
	}

	/**
	 * @return  the uncertainty
	 */
	public int getUncertainty() {
		return uncertainty;
	}

	/**
	 * @param uncertainty  the uncertainty to set
	 */
	public void setUncertainty(int uncertainty) {
		this.uncertainty = uncertainty;
	}
	
	/* (non-Javadoc)
	 * @see net.g2lab.feature.PolygonFeature#getFeatureTypeString()
	 */
	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:Polygon," + ATTRIBUTE_UNCERTAINTY + ":Integer";
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
