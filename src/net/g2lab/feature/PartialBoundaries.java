package net.g2lab.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

/** A class for the management of partial boundaries of geo-objects.
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class PartialBoundaries {

	/**
	 */
	protected String id;
	/**
	 */
	protected Map<String, Geometry> partialBoundaries;
	/**
	 */
	protected boolean outer;

	public PartialBoundaries(String id) {
		this.id = id;
		this.partialBoundaries = new HashMap<String, Geometry>();
	}

	public Geometry getBoundary(String neighId) {
		return partialBoundaries.get(neighId);
	}

	public void putBoundary(String neighId, Geometry geometry) {
		partialBoundaries.put(neighId, geometry);
	}

	public Set<String> getNeighbourIDs() {
		return partialBoundaries.keySet();
	}
	
	public int size() {
		return partialBoundaries.size();
	}

	public String getObjId() {
		return id;
	}

	/**
	 * @return
	 */
	public boolean isOuter() {
		return outer;
	}
	
	/**
	 * @param outer
	 */
	public void setOuter(boolean outer) {
		this.outer = outer;
	}

}
