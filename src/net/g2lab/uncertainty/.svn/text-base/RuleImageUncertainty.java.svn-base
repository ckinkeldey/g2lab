package net.g2lab.uncertainty;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffFormat;

/**
 * Computes uncertainty from a set of rule images. It uses an uncertainty
 * measure based on spectral distance to the mean spectral vector of each class.
 * 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class RuleImageUncertainty {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static String PATH = "C:/temp/petersroda/";

	private static final String[] OUT_PATHS_RULE_IMAGES = { PATH + "ruleimage-2000.tif",
		PATH + "ruleimage-2003.tif",
//		PATH + "ruleimage-2009.tif"
	};
	private static final String OUT_PATH_RULE_IMAGE_ALL = PATH + 
//			"/ruleimage-min-00_03_09.tif";
			"/ruleimage-min-00_03.tif";
	
	private static final String OUT_PATH_U = PATH + 
//			"/uncertainty-00_03_09.tif";
			"/uncertainty-00_03.tif";

	private static final int[] NUM_CLASSES = { 8, 7, 7 };
	
	private static final String[] CLASS_NAMES = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
//	private static double RULE_VALUE_UNCLASSIFIED = 1.0;
//	private static final int CLASS_ID_UNCLASSIFIED = 0;

	private static final String[] LC_PATHS = { 
			PATH + "2000.tif",
			PATH + "2003.tif", 
//			PATH + "2009.tif" 
			};

	private static final String[] RULE_IMG_PATHS = {
			PATH + "Petersroda_MNF_2000_rule.tif",
			PATH + "Petersroda_MNF_2003_rule.tif",
//			PATH + "Petersroda_MNF_2009_rule.tif" 
			};

	private GridCoverage2D[] lcCoverage;
	private GridCoverage2D[] ruleImageCoverages;
	private int[] numClasses;

	private GridCoverage2D uncertaintyCoverage;
	private GridCoverage2D minRuleCoverage;
	private GridCoverage2D[] ruleImages;

	public RuleImageUncertainty(int[] numClasses, GridCoverage2D[] lcCoverages,
			GridCoverage2D[] ruleImgCoverages) {
		this.lcCoverage = lcCoverages;
		this.ruleImageCoverages = ruleImgCoverages;
		this.numClasses = numClasses;

		LOG.info("Computing rule images...");
		this.ruleImages = computeRuleImages();
				
		LOG.info("Computing overall rule image...");
		this.minRuleCoverage = computeMinRuleImage();

		LOG.info("Computing uncertainty...");
		BufferedImage uncertaintyImage = computeUncertainty(minRuleCoverage);
		this.uncertaintyCoverage = new GridCoverageFactory().create(
				"uncertainty", uncertaintyImage,
				ruleImgCoverages[0].getEnvelope());
	}

	public int getNumScenes() {
		return lcCoverage.length;
	}

	public GridCoverage2D getUncertaintyCoverage() {
		return uncertaintyCoverage;
	}

	public GridCoverage2D[] getRuleImages() {
		return ruleImages;
	}
	
	public GridCoverage2D getMinRuleImageCoverage() {
		return minRuleCoverage;
	}

	private GridCoverage2D[] computeRuleImages() {
		int width = ruleImageCoverages[0].getRenderedImage().getWidth();
		int height = ruleImageCoverages[0].getRenderedImage().getHeight();
		byte[][] ruleBuffers = new byte[getNumScenes()][width * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int bufferIndex = i * width + j;
					for (int nScene = 0; nScene < getNumScenes(); nScene++) {
						ruleBuffers[nScene][bufferIndex] = (byte) (100 * getRuleValue(nScene, j, i));
					}
			}
		}
		GridCoverage2D[] coverages = new GridCoverage2D[getNumScenes()];
		for (int nScene = 0; nScene < getNumScenes(); nScene++) {
			BufferedImage ruleImage = net.g2lab.raster.Raster.getGrayscaleImage(width, ruleBuffers[nScene]);
			coverages[nScene] = new GridCoverageFactory().create("rule",
					ruleImage, ruleImageCoverages[0].getEnvelope());
		}
		return coverages;
	}
	
	private GridCoverage2D computeMinRuleImage() {
		int width = ruleImageCoverages[0].getRenderedImage().getWidth();
		int height = ruleImageCoverages[0].getRenderedImage().getHeight();
		byte[] buffer = new byte[width * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int bufferIndex = i * width + j;
				buffer[bufferIndex] = (byte) ((byte) 100 * getMinRuleValue(j, i));
			}
		}
		BufferedImage ruleImage = net.g2lab.raster.Raster.getGrayscaleImage(width, buffer);
		return new GridCoverageFactory().create("min rule",
				ruleImage, ruleImageCoverages[0].getEnvelope());
	}

	private double getMinRuleValue(int i, int j) {
		double minimum = Double.POSITIVE_INFINITY;
		for (int nScene = 0; nScene < getNumScenes(); nScene++) {
			double ruleValue = getRuleValue(nScene, i, j);
			if (ruleValue < minimum) {
				minimum = ruleValue;
			}
		}
		return minimum;
	}

	private double getRuleValue(int scene, int i, int j) {
		int[] lcClass = new int[1];
		double[] ruleValues = new double[numClasses[scene]];
//		LOG.debug("i,j == " + i + "," + j );
		lcClass = lcCoverage[scene].getRenderedImage().getData().getPixel(i, j, lcClass);
//		if (lcClass[0] == CLASS_ID_UNCLASSIFIED) {
//			return RULE_VALUE_UNCLASSIFIED;
//		} else {
			int classIndex = getRuleImageBandForClass(lcClass[0]);
			ruleValues = ruleImageCoverages[scene].getRenderedImage().getData()
					.getPixel(i, j, ruleValues);
			return ruleValues[classIndex];
//		}
	}

	public int getRuleImageBandForClass(int classNo) {
		return Arrays.asList(CLASS_NAMES).indexOf("" + classNo);
	}

	private BufferedImage computeUncertainty(GridCoverage2D minRuleImage) {
		int width = minRuleImage.getRenderedImage().getWidth();
		int height = minRuleImage.getRenderedImage().getHeight();
		byte[] buffer = new byte[width * height];
		int[] value = new int[1];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				minRuleImage.getRenderedImage().getData().getPixel(j, i, value);
				buffer[i * width + j] = (byte) (100 - value[0]);
			}
		}
		return net.g2lab.raster.Raster.getGrayscaleImage(width, buffer);
	}

	public static void main(String[] args) {
		try {
			GridCoverage2D[] lc = RasterIO.loadCoverages(LC_PATHS);
			GridCoverage2D[] ruleImages = RasterIO.loadCoverages(RULE_IMG_PATHS);
			RuleImageUncertainty uncertainty = new RuleImageUncertainty(NUM_CLASSES, lc, ruleImages);
			
			GridCoverage2D[] ruleCoverages = uncertainty.getRuleImages();
			for (int i = 0; i < ruleCoverages.length; i++) {
				LOG.info("Writing " + OUT_PATHS_RULE_IMAGES[i]);
				new RasterIO().writeCoverage(ruleCoverages[i],
						new File(OUT_PATHS_RULE_IMAGES[i]).toURI().toURL(),
						new GeoTiffFormat());
			}

			LOG.info("Writing " + OUT_PATH_RULE_IMAGE_ALL);
			new RasterIO().writeCoverage(uncertainty.getMinRuleImageCoverage(),
					new File(OUT_PATH_RULE_IMAGE_ALL).toURI().toURL(),
					new GeoTiffFormat());

			LOG.info("Writing " + OUT_PATH_U);
			new RasterIO().writeCoverage(uncertainty.getUncertaintyCoverage(),
					new File(OUT_PATH_U).toURI().toURL(), new GeoTiffFormat());
		} catch (Exception e) {
			LOG.error("Could not create uncertainty raster: " + e, e);
		}
	}

}
