package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;

import com.vividsolutions.jts.geom.Point;

public class UncertaintyPointFeature extends PointFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final String ATTRIBUTE_NAME_UNCERTAINTY = "u";

	/**
	 */
	private double uncertainty;

	public UncertaintyPointFeature(String id, Point point, double uncertainty) {
		super(id, point);
		this.uncertainty = uncertainty;
	}

	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:Point," + ATTRIBUTE_NAME_UNCERTAINTY + ":Double";
	}

	@Override
	protected void initSimpleFeatureBuilder(SimpleFeatureBuilder builder) {
		super.initSimpleFeatureBuilder(builder);
		builder.add(this.uncertainty);
	}

}
