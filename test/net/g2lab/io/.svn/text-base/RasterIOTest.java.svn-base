/**
 * 
 */
package net.g2lab.io;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.junit.After;
import org.junit.Before;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class RasterIOTest {

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

	public static void main(String[] args) throws DataSourceException, IOException, UnsupportedOperationException, NoSuchAuthorityCodeException, FactoryException {
		String filename = 
//			"C:/temp/ubonn/Eschschallen_rule.tif";
			"C:/Users/ck/Dropbox/hcu/icchange/halle/2003-reclass.tif";
		String outFilename = "C:/Users/ck/Dropbox/hcu/icchange/halle/2003-reclass-1.tif";
		
		URL url = new File(filename).toURI().toURL();
		GridCoverage2D raster = new RasterIO().readCoverage(url, new GeoTiffFormat());
		new RasterIO().writeCoverage(raster, new File(outFilename).toURI().toURL(), new GeoTiffFormat());
	}
}
