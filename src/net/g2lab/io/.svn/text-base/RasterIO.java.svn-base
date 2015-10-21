package net.g2lab.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class RasterIO {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public GridCoverage2D readCoverage(URL inURL) throws DataSourceException,
			IOException {
		return readCoverage(new URL[] { inURL })[0];
	}

	public GridCoverage2D[] readCoverage(URL[] inURLs) throws IOException, UnsupportedOperationException {
		int numScenes = inURLs.length;
		GridCoverage2D[] images = new GridCoverage2D[numScenes];
		for (int i = 0; i < numScenes; i++) {
//			File file = new File(inURLs[i].getFile());
			LOG.info("reading image from " + inURLs[i]);
			try {
				AbstractGridFormat format = GridFormatFinder.findFormat(inURLs[i]);
				LOG.debug("format: " + format);
		        AbstractGridCoverage2DReader reader = format.getReader(inURLs[i]);
				// WorldImageReader reader = new WorldImageReader(file);
				images[i] = (GridCoverage2D) reader.read(null);
				LOG.debug("crs: " + images[i].getCoordinateReferenceSystem());
			} catch (IOException e) {
				throw new IOException("Cannot open raster: " + e, e);
			} catch (UnsupportedOperationException e) {
				throw new UnsupportedOperationException("Cannot open raster: " + e, e);
			}
		}
		return images;
	}

	public GridCoverage2D readCoverage(URL inURL, AbstractGridFormat format, Hints hints) throws IOException, UnsupportedOperationException, NoSuchAuthorityCodeException, FactoryException {
		return readCoverage(new URL[] { inURL }, format, hints)[0];
	}
	
	public GridCoverage2D readCoverage(URL inURL, AbstractGridFormat format) throws IOException, UnsupportedOperationException, NoSuchAuthorityCodeException, FactoryException {
		return readCoverage(new URL[] { inURL }, format)[0];
	}
	
	public GridCoverage2D[] readCoverage(URL[] inURLs, AbstractGridFormat format) throws IOException, UnsupportedOperationException, NoSuchAuthorityCodeException, FactoryException {
		return readCoverage(inURLs, format, new Hints());
	}
	
	public GridCoverage2D[] readCoverage(URL[] inURLs, AbstractGridFormat format, Hints hints) throws IOException, UnsupportedOperationException, NoSuchAuthorityCodeException, FactoryException {
		int numScenes = inURLs.length;
		GridCoverage2D[] images = new GridCoverage2D[numScenes];
		for (int i = 0; i < numScenes; i++) {
			LOG.info("reading image from " + inURLs[i]);
			try {
				LOG.debug("format: " + format); 
		        AbstractGridCoverage2DReader reader = format.getReader(inURLs[i], hints);
				images[i] = (GridCoverage2D) reader.read(null);
				LOG.debug("crs: " + images[i].getCoordinateReferenceSystem());
				LOG.debug("dimensions: " + images[i].getRenderedImage().getWidth() + " x " + images[i].getRenderedImage().getHeight());
			} catch (IOException e) {
				throw new IOException("Cannot open raster: " + e, e);
			} catch (UnsupportedOperationException e) {
				throw new UnsupportedOperationException("Cannot open raster: " + e, e);
			}
		}
		return images;
	}
	
	public void writeCoverage(GridCoverage2D coverage, URL outURL, AbstractGridFormat format) throws IllegalArgumentException, IOException {
		GridCoverageWriter writer = format.getWriter(outURL);
		writer.write(coverage, null);
	}

	public static GridCoverage2D[] loadCoverages(String[] paths) throws UnsupportedOperationException, NoSuchAuthorityCodeException, MalformedURLException, IOException, FactoryException {
		GridCoverage2D[] coverages = new GridCoverage2D[paths.length];
		for (int i = 0; i < paths.length; i++) {
			coverages[i] = new RasterIO().readCoverage(new File(paths[i]).toURI().toURL());
		}
		return coverages;
	}

}
