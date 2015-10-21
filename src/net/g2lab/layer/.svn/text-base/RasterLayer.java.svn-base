/**
 * A layer containing raster values.
 */
package net.g2lab.layer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

import javax.media.jai.Interpolation;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.parameter.ParameterValueGroup;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class RasterLayer extends AbstractLayer {

	/**
	 */
	private GridCoverage2D coverage;
	private Envelope envelope;

	public RasterLayer(GridCoverage2D coverage) {
		this.coverage = coverage;
		this.envelope = createEnvelope(coverage);
	}

	@Override
	public Envelope getEnvelope() {
		return envelope;
	}
	
	private Envelope createEnvelope(GridCoverage2D coverage) {
		Envelope2D otherEnvelope = coverage.getEnvelope2D();
		double x0 = otherEnvelope.getMinX();
		double x1 = otherEnvelope.getMaxX();
		double y0 = otherEnvelope.getMinY();
		double y1 = otherEnvelope.getMaxY();
		Envelope envelope = new Envelope(x0, x1, y0, y1);
		return envelope;
	}

	@Override
	public Integer getIntAttribute(String attributeName, double x, double y) {
		try {
			return (int) ((byte[]) coverage.evaluate(new DirectPosition2D(x, y)))[0];
		} catch (PointOutsideCoverageException e) {
			return NO_DATA_INT;
		}
	}

	@Override
	public Double getDoubleAttribute(String attributeName, double x, double y) {
		try {
			return (double)((byte[]) coverage.evaluate(new DirectPosition2D(x, y)))[0];
		} catch (PointOutsideCoverageException e) {
			return NO_DATA_DOUBLE;
		}
	}

	@Override
	public String getStringAttribute(String attributeName, double x, double y) {
		try {
			return ((int[])coverage.evaluate(new DirectPosition2D(x, y)))[0]+"";
		} catch (PointOutsideCoverageException e) {
			return NO_DATA_STRING;
		}
	}
	
	public Image getAsImage(int width, int height) {
		Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
		scaleX = (float)width / (float) coverage.getRenderedImage().getWidth();
		scaleY = (float) height / (float) coverage.getRenderedImage().getHeight();
		Hints hints = new Hints(Hints.COVERAGE_PROCESSING_VIEW, ViewType.NATIVE);
		RenderedImage scaled = scale(this.coverage, interp, scaleX, scaleY, hints);
		return getAsBufferedImage(scaled);
	}
	
	public BufferedImage getAsBufferedImage(RenderedImage img) {
	    if (img instanceof BufferedImage) {
	        return (BufferedImage)img;  
	    }   
	    ColorModel cm = img.getColorModel();
	    int width = img.getWidth();
	    int height = img.getHeight();
	    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    Hashtable properties = new Hashtable();
	    String[] keys = img.getPropertyNames();
	    if (keys!=null) {
	        for (int i = 0; i < keys.length; i++) {
	            properties.put(keys[i], img.getProperty(keys[i]));
	        }
	    }
	    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
	    img.copyData(raster);
	    return result;
	}
	
	/**
     * Applies a scale on the photographic view of the given coverage.
     *
     * @param coverage The coverage to scale.
     * @param interp The interpolation to use.
	 * @param xScale 
	 * @param yScale 
	 * @return 
     */
    private RenderedImage scale(final GridCoverage2D coverage, final Interpolation interp, float xScale, float yScale, Hints hints) {
    	CoverageProcessor processor = new CoverageProcessor(hints);
    	
//        // Caching initial properties.
//        final RenderedImage originalImage = coverage.getRenderedImage();
//        final int w = originalImage.getWidth();
//        final int h = originalImage.getHeight();

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation("Scale").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("xScale").setValue(xScale);
        param.parameter("yScale").setValue(yScale);
        param.parameter("xTrans").setValue(Float.valueOf(0.0f));
        param.parameter("yTrans").setValue(Float.valueOf(0.0f));
        param.parameter("Interpolation").setValue(interp);

        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param);

        RenderedImage scaledImage = scaled.getRenderedImage();
        return scaledImage;
    }

}
