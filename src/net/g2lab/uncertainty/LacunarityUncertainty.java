package net.g2lab.uncertainty;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPosition2D;
import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;
import org.la4j.vector.dense.BasicVector;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

/**
 * Computes uncertainty for a set of lacunarity images.
 * 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class LacunarityUncertainty {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static String PATH = "C:/Users/ck/Dropbox/hcu/expert-study/Lüdeke/";
	
	private static final String[] LACUNARITY_PATHS = {
		PATH + "lac-2003-clip.tif",
		PATH + "lac-2010-clip.tif",
	};
	
	private static final String OUT_PATH = PATH + "/lacunarityuncertainty/";
	private static final String[] OUT_PATHS_CLASS_MEMBERSHIP = {
		OUT_PATH + "lac-2003-member.tif",
		OUT_PATH + "lac-2010-member.tif",
	};
	private static final String OUT_PATH_UNCERTAINTY = 
		OUT_PATH + "lac-0310-u.tif";
	

	private GridCoverage2D[] membershipCoverages;
	private GridCoverage2D uncertaintyCoverage;

	private GridCoverage2D[] lacunCoverages;

	public LacunarityUncertainty(GridCoverage2D[] lacunCoverages) {
		this.lacunCoverages = lacunCoverages;
		this.membershipCoverages = new GridCoverage2D[lacunCoverages.length];
		for (int i = 0; i < lacunCoverages.length; i++) {
			LOG.info("Computing memberships for scene " + i + "...");
			computeMemberships(i);
		}
		LOG.info("Computing uncertainty...");
		BufferedImage uncertaintyImage = computeUncertainty();
		this.uncertaintyCoverage = new GridCoverageFactory().create(
				"uncertainty", uncertaintyImage, lacunCoverages[0].getEnvelope());
	}

	public int getNumScenes() {
		return lacunCoverages.length;
	}

	public GridCoverage2D[] getMembershipCoverages() {
		return membershipCoverages;
	}

	public GridCoverage2D getUncertaintyCoverage() {
		return uncertaintyCoverage;
	}

	private BufferedImage computeUncertainty() {
		int width = lacunCoverages[0].getRenderedImage().getWidth();
		int height = lacunCoverages[0].getRenderedImage().getHeight();
		byte[] buffer = new byte[width * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buffer[i * width + j] = (byte) computeUncertainty(j, i);
			}
		}
		return net.g2lab.raster.Raster.getGrayscaleImage(width, buffer);
	}

	private int computeUncertainty(int j, int i) {
		double minimum = Double.POSITIVE_INFINITY;
		int[] memberships = new int[membershipCoverages.length];
		for (int n = 0; n < getNumScenes(); n++) {
			membershipCoverages[n].getRenderedImage().getData().getPixel(j, i, memberships);
			if (memberships[n] < minimum) {
				minimum = memberships[n];
			}
		}
		return (int) (100. - minimum);
	}

	public void computeMemberships(int i) {
		BufferedImage membershipImage = createMembershipRaster(lacunCoverages[i]);
		this.membershipCoverages[i] = new GridCoverageFactory().create(
				"membership", membershipImage, lacunCoverages[i].getEnvelope());
	}

	private BufferedImage createMembershipRaster(GridCoverage2D lacunCoverages) {
		int width = lacunCoverages.getRenderedImage().getWidth();
		int height = lacunCoverages.getRenderedImage().getHeight();
		byte[] buffer = new byte[width * height];
		Raster lacunRaster = lacunCoverages.getRenderedImage().getData();
		int[] lacunarity = new int[1];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				lacunRaster.getPixel(j, i, lacunarity);
				double membership = computeMembership(lacunarity[0]/100.);
				buffer[i * width + j] = (byte) (100 * membership);
			}
		}
		return net.g2lab.raster.Raster.getGrayscaleImage(width, buffer);
	}

	/**
	 * 
	 * @param meanVector
	 * @param spectralVector
	 * @return
	 */
	public static double computeMembership(double lacunarity) {
		double lower = 1.1;
		double upper = 1.15;
		double peak = (lower+upper)/2;
		double interval = 0.05;
		double start = lower - interval/2;
		double stop = upper + interval/2;
		if (lacunarity < start || lacunarity > stop) {
			return 1;
		} else if (lacunarity <= peak){
			return (lacunarity - start) / (peak - start);
		} else {
			return 1 - (lacunarity - stop) / (peak - stop);
		}
	}

	public static void main(String[] args) {
		try {
			GridCoverage2D[] lacunarity = RasterIO.loadCoverages(LACUNARITY_PATHS);
			LOG.debug("raster0: " + lacunarity[0].getEnvelope());
			LOG.debug("raster1: " + lacunarity[1].getEnvelope());
			
			LOG.debug("crs0: " + lacunarity[0].getCoordinateReferenceSystem());
			LOG.debug("crs1: " + lacunarity[1].getCoordinateReferenceSystem());
			
			LacunarityUncertainty uncertainty = new LacunarityUncertainty(lacunarity);
			for (int i = 0; i < uncertainty.getNumScenes(); i++) {
				LOG.info("Writing " + OUT_PATHS_CLASS_MEMBERSHIP[i]);
				new RasterIO().writeCoverage(uncertainty.getMembershipCoverages()[i],
						new File(OUT_PATHS_CLASS_MEMBERSHIP[i]).toURI().toURL(), new GeoTiffFormat());
			}
			LOG.info("Writing " + OUT_PATH_UNCERTAINTY);
			new RasterIO().writeCoverage(uncertainty.getUncertaintyCoverage(),
					new File(OUT_PATH_UNCERTAINTY).toURI().toURL(), new GeoTiffFormat());
		} catch (Exception e) {
			LOG.error("Could not create raster: " + e, e);
		}
	}

}
