package net.g2lab.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.LineString;

public class LineStringFeature extends Feature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public LineStringFeature(String id, LineString lineString) {
		super(id);
		this.geometry = lineString;
	}

	@Override
	public String getFeatureTypeString() {
		return "id:String,*geom:LineString";
	}

}
