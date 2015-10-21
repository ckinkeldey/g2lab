/**
 * 
 */
package net.g2lab.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class TestBase {

	public static GridCoverage2D createRaster() throws NoSuchAuthorityCodeException, FactoryException {
		CharSequence name = "testRaster";
		float[][] image = new float[][] {{0, 1}, {2, 3}};
		CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		Envelope envelope = new Envelope2D(crs, 0, 0, 1, 1);
		GridCoverage2D coverage = new GridCoverageFactory().create(name, image, envelope);
		return coverage;
	}
	
	public static Polygon createPolygon() {
		Coordinate[] coordinates = new Coordinate[] {
				new Coordinate(0, 0),
				new Coordinate(1, 0),
				new Coordinate(1, 1),
				new Coordinate(0, 1),
				new Coordinate(0, 0)
		};
		LinearRing shell = new GeometryFactory().createLinearRing(coordinates);
		Polygon polygon = new GeometryFactory().createPolygon(shell , new LinearRing[0]);
		return polygon;
	}

	public static Collection<PolygonFeature> createFeaturesInteger() {
		Collection<PolygonFeature> features = new HashSet<PolygonFeature>();
		Polygon polygon = createPolygon();
		features.add(new UncertaintyIntPolygonFeature("0", polygon, 5));
		return features;
	}

	public static Collection<PolygonFeature> createFeaturesDouble() {
		Collection<PolygonFeature> features = new HashSet<PolygonFeature>();
		Polygon polygon = createPolygon();
		features.add(new UncertaintyDoublePolygonFeature("0", polygon, 4.5));
		return features;
	}
	
	/** One polygon with two holes touching each other.
	 * @return 
	 * @throws SchemaException
	 */
	protected static List<ClassifiedPolygonFeature> createTestPolygonFeatures() throws SchemaException {
		List<ClassifiedPolygonFeature> features = new ArrayList<ClassifiedPolygonFeature>();
		LinearRing poly0 = new GeometryFactory().createLinearRing(createCoordinates(new double[] {0.0, 0.0, 4.0, 4.0, 0.0}, new double[] {0.0, 3.0, 3.0, 0.0, 0.0}));
		LinearRing poly1 = new GeometryFactory().createLinearRing(createCoordinates(new double[] {1.0, 1.0, 2.0, 2.0, 1.0}, new double[] {1.0, 2.0, 2.0, 1.0, 1.0}));
		LinearRing poly2 = new GeometryFactory().createLinearRing(createCoordinates(new double[] {2.5, 2.5, 3.0, 3.0, 2.5}, new double[] {1.0, 2.0, 2.0, 1.0, 1.0}));
		LinearRing[] holes = new LinearRing[2];
		holes[0] = new GeometryFactory().createLinearRing(poly1.reverse().getCoordinates());
		holes[1] = new GeometryFactory().createLinearRing(poly2.reverse().getCoordinates());
		Polygon poly0WithHoles = new GeometryFactory().createPolygon(poly0, holes);
		features.add(new ClassifiedPolygonFeature("0", "class0", poly0WithHoles));
		features.add(new ClassifiedPolygonFeature("1", "class1", new GeometryFactory().createPolygon(poly1, null)));
		features.add(new ClassifiedPolygonFeature("2", "class2", new GeometryFactory().createPolygon(poly2, null)));
		return features;
	}
	
	protected static Coordinate[] createCoordinates(double[] xCoord, double[] yCoord) {
		Coordinate[] coordinates = new Coordinate[xCoord.length];
		for (int i = 0; i < xCoord.length; i++) {
			coordinates[i] = new Coordinate(xCoord[i], yCoord[i]);
		}
		return coordinates;
	}

}
