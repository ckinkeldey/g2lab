/**
 * 
 */
package net.g2lab.layer;

import java.util.Collection;
import java.util.HashSet;

import net.g2lab.feature.PolygonFeature;
import net.g2lab.feature.UncertaintyDoublePolygonFeature;

import org.junit.After;
import org.junit.Before;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class CachedFeatureLayerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) {
		Collection<PolygonFeature> features = new HashSet<PolygonFeature>();
		Coordinate[] coords = new Coordinate[] {
				new Coordinate(0., 0.),
				new Coordinate(0., 1.),
				new Coordinate(1., 1.),
				new Coordinate(1., 0.),
				new Coordinate(0., 0.)
		};
		LinearRing shell = new GeometryFactory().createLinearRing(coords);
		Polygon polygon = new GeometryFactory().createPolygon(shell , null);
		PolygonFeature feature = new UncertaintyDoublePolygonFeature("poly0", polygon, 50.);
		features.add(feature);
		CachedFeatureLayer layer = new CachedFeatureLayer(features);
	}
}
