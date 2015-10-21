package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Point;

public class PointFeature extends Feature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public PointFeature(String id, Point point) {
		super(id);
		this.geometry = point;
	}

	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:Point";
	}

}
