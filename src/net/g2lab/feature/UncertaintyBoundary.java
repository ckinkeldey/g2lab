/**
 * 
 */
package net.g2lab.feature;

import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class UncertaintyBoundary extends PartialBoundaries {

	private static final double DEFAULT_UNCERT = 0;
	
	/**
	 */
	protected Map<String, Double> uncertainties;
	
	public UncertaintyBoundary(String id) {
		super(id);
		uncertainties = new HashMap<String, Double>();
	}

	/* (non-Javadoc)
	 * @see net.g2lab.feature.Boundary#putBoundary(java.lang.String, com.vividsolutions.jts.geom.Geometry)
	 */
	@Override
	public void putBoundary(String neighId, Geometry geometry) {
		putBoundary(neighId, geometry, DEFAULT_UNCERT);
	}
	
	public void putBoundary(String neighId, Geometry geometry, double uncertainty) {
		super.putBoundary(neighId, geometry);
		this.uncertainties.put(neighId, uncertainty);
	}

}
