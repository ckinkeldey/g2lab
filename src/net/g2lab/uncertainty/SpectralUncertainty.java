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

/**
 * Computes uncertainty for a set of spectral images. It uses an uncertainty
 * measure based on spectral distance to the mean spectral vector of each class.
 * 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class SpectralUncertainty {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

//	private static final String PATH = "file:///C://temp/petersroda/";
//
//	private static final String OUT_PATH_UNCERTAINTY = PATH + "uncertainty-2000-2003.tif";
//
//	private static final String LC_PATH = PATH + "2000.tif";
//	private static final String SPECTRAL_PATH = PATH + "Petersroda_MNF_2000.tif";
//	private static final String OUT_PATH_SIMILARITY = PATH + "similarity-2000.tif";
//	private static final int NUM_LC_CLASSES = 8;
//	private static final int NUM_BANDS = 40;
//
//	private static final String LC_PATH_2003 = PATH + "2003.tif";
//	private static final String SPECTRAL_PATH_2003 = PATH + "Petersroda_MNF_2003.tif";
//	private static final String OUT_PATH_SIMILARITY_2003 = PATH + "similarity-2003.tif";
//	private static final int NUM_LC_CLASSES_2003 = 8;
//	private static final int NUM_BANDS_2003 = 30;
	
	// icchange example
	private static String PATH = "C:/temp/icchange/";
	
	private static final String[] LC_PATHS = {
		PATH + "2003-iso3-32630.tif",
		PATH + "2004-iso3-32630.tif",
		PATH + "2004-2-iso3-32630.tif",
	};
	
	private static final String[] SPECTRAL_PATHS = {
		PATH + "2003-32630.tif",
		PATH + "2004-32630.tif",
		PATH + "2004-2-32630.tif",
	};
	
	private static final int[] NUM_CLASSES = {3, 3, 3};
	private static final String[] CLASS_NAMES = {"1", "2", "3"};
	private static final int[] NUM_BANDS = {4, 4 , 4};
	
	private static final String OUT_PATH = PATH + "/spectraluncertainty/";
	private static final String[] OUT_PATHS_SIMILARITY = {
		OUT_PATH + "2003-32630-similarity.tif",
		OUT_PATH + "2004-32630-similarity.tif",
		OUT_PATH + "2004-2-32630-similarity.tif",
	};
	private static final String OUT_PATH_UNCERTAINTY = 
		OUT_PATH + "2003-2004-2004-2-u.tif";
	

	private GridCoverage2D[] lcCoverage;
	private GridCoverage2D[] spectralCoverage;
	private double[][] refrenceVectors;
	private int[] classCount;
	private int[] numClasses;
	private int[] numBands;

	private GridCoverage2D[] similarityCoverages;
	private GridCoverage2D uncertaintyCoverage;

	public SpectralUncertainty(int[] numClasses, int[] numBands,
			GridCoverage2D[] lcCoverages, GridCoverage2D[] spectralCoverages) {
		this.lcCoverage = lcCoverages;
		this.spectralCoverage = spectralCoverages;
		this.numClasses = numClasses;
		this.numBands = numBands;
		this.similarityCoverages = new GridCoverage2D[lcCoverages.length];
		for (int i = 0; i < lcCoverages.length; i++) {
			LOG.info("Computing similarity for scene " + i + "...");
			computeSimilarity(i);
		}
		LOG.info("Computing uncertainty...");
		BufferedImage uncertaintyImage = computeUncertainty();
		this.uncertaintyCoverage = new GridCoverageFactory().create(
				"uncertainty", uncertaintyImage, spectralCoverages[0].getEnvelope());
	}

	public int getNumScenes() {
		return lcCoverage.length;
	}

	public GridCoverage2D[] getSimilarityCoverages() {
		return similarityCoverages;
	}

	public GridCoverage2D getUncertaintyCoverage() {
		return uncertaintyCoverage;
	}

	private BufferedImage computeUncertainty() {
		int width = spectralCoverage[0].getRenderedImage().getWidth();
		int height = spectralCoverage[0].getRenderedImage().getHeight();
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
		int[] similarity = new int[3];
		for (int n = 0; n < getNumScenes(); n++) {
			similarityCoverages[n].getRenderedImage().getData()
					.getPixel(j, i, similarity);
			if (similarity[0] < minimum) {
				minimum = similarity[0];
			}
		}
		return (int) (100 - minimum);
	}

	/**
	 * @param numClasses
	 * @param numBands
	 */
	public void computeSimilarity(int i) {
		computeRepresentativeVectors(numClasses[i], numBands[i], lcCoverage[i],
				spectralCoverage[i]);
		BufferedImage similarityImage = createSimilarityRaster(numClasses[i],
				numBands[i], lcCoverage[i], spectralCoverage[i]);
		this.similarityCoverages[i] = new GridCoverageFactory().create(
				"similarity", similarityImage, spectralCoverage[i].getEnvelope());

	}

	private BufferedImage createSimilarityRaster(int numLcClasses,
			int numChannels, GridCoverage2D lcCoverage,
			GridCoverage2D spectralCoverage) {
		int width = spectralCoverage.getRenderedImage().getWidth();
		int height = spectralCoverage.getRenderedImage().getHeight();
		byte[] buffer = new byte[width * height];
		Raster lcRaster = lcCoverage.getRenderedImage().getData();
		int[] lc = new int[lcRaster.getNumBands()];
		Raster mnfRaster = spectralCoverage.getRenderedImage().getData();
		double[] spectral = new double[mnfRaster.getNumBands()];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				lcRaster.getPixel(j, i, lc);
				BasicVector meanVector = new BasicVector(
						refrenceVectors[getIndexOfClass(lc, 0)]);
				BasicVector spectralVector = new BasicVector(
						mnfRaster.getPixel(j, i, spectral));
				double similarity = computeSimilarity(meanVector,
						spectralVector);
				buffer[i * width + j] = (byte) (100 * similarity);
			}
		}

		return net.g2lab.raster.Raster.getGrayscaleImage(width, buffer);
	}

	/**
	 * Computes a similarity value between the given spectral vector and the
	 * mean vector using the spectral angle between the two vectors.
	 * 
	 * @param meanVector
	 * @param spectralVector
	 * @return
	 */
	public static double computeSimilarity(Vector spectralVector,
			Vector meanVector) {
		double meanNorm = meanVector.fold(Vectors.mkEuclideanNormAccumulator());
		double spectralNorm = spectralVector.fold(Vectors
				.mkEuclideanNormAccumulator());
		double numerator = meanVector.innerProduct(spectralVector);
		double denominator = meanNorm * spectralNorm;
		return 1 - Math.acos(numerator / denominator) / Math.PI;
	}

	private void computeRepresentativeVectors(int numLcClasses,
			int numChannels, GridCoverage2D lcCoverage,
			GridCoverage2D spectralCoverage) {
		this.refrenceVectors = new double[numLcClasses][numChannels];
		for (int i = 0; i < numLcClasses; i++) {
			Arrays.fill(refrenceVectors[i], 0);
		}

		this.classCount = new int[numLcClasses];
		Arrays.fill(classCount, 0);

		Raster lcRaster = lcCoverage.getRenderedImage().getData();
		int[] lc = new int[lcRaster.getNumBands()];
		Raster mnfRaster = spectralCoverage.getRenderedImage().getData();
		int[] spectral = new int[mnfRaster.getNumBands()];

		for (int i = 0; i < lcRaster.getHeight(); i++) {
			for (int j = 0; j < lcRaster.getWidth(); j++) {
				lcRaster.getPixel(j, i, lc);
				mnfRaster.getPixel(j, i, spectral);
				classCount[getIndexOfClass(lc, 0)]++;
				addVector(refrenceVectors[getIndexOfClass(lc, 0)], spectral);
			}
		}
		computeMean(refrenceVectors, classCount);

	}

	public int getIndexOfClass(int[] array, int i) {
		return Arrays.asList(CLASS_NAMES).indexOf(""+array[i]);
	}

	private void computeMean(double[][] meanVectors, int[] classCount) {
		for (int i = 0; i < classCount.length; i++) {
			for (int j = 0; j < meanVectors[0].length; j++) {
				meanVectors[i][j] /= classCount[i];
			}
		}
	}

	private void addVector(double[] average, int[] spectral) {
		for (int i = 0; i < average.length; i++) {
			average[i] += spectral[i];
		}
	}

	public static void main(String[] args) {
		try {
			GridCoverage2D[] lc = RasterIO.loadCoverages(LC_PATHS);
			GridCoverage2D[] spectral = RasterIO.loadCoverages(SPECTRAL_PATHS);
			SpectralUncertainty uncertainty = new SpectralUncertainty(
					NUM_CLASSES, NUM_BANDS, lc, spectral);
			for (int i = 0; i < uncertainty.getNumScenes(); i++) {
				LOG.info("Writing " + OUT_PATHS_SIMILARITY[i]);
				new RasterIO().writeCoverage(uncertainty.getSimilarityCoverages()[i],
						new File(OUT_PATHS_SIMILARITY[i]).toURI().toURL(), new GeoTiffFormat());
			}
			LOG.info("Writing " + OUT_PATH_UNCERTAINTY);
			new RasterIO().writeCoverage(uncertainty.getUncertaintyCoverage(),
					new File(OUT_PATH_UNCERTAINTY).toURI().toURL(), new GeoTiffFormat());
		} catch (Exception e) {
			LOG.error("Could not create uncertainty raster: " + e, e);
		}
	}

}
