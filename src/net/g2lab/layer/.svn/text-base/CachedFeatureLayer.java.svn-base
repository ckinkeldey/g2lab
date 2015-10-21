package net.g2lab.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;

import net.g2lab.feature.PolygonFeature;
import net.g2lab.feature.UncertaintyDoublePolygonFeature;

import org.geotools.geometry.jts.LiteShape;

import com.vividsolutions.jts.geom.Geometry;


/** A polygon feature layer using a raster instead of polygons to get values.
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class CachedFeatureLayer extends PolygonFeatureLayer {

	/**
	 */
	private boolean updateRaster = true;
	/**
	 */
	private Map<String, BufferedImage> rasters;
	/**
	 */
	private int rasterWidth = 100;
	/**
	 */
	private int rasterHeight = 100;
	/**
	 */
	private double scaleFactorX;
	/**
	 */
	private double scaleFactorY;
	/**
	 */
	private BufferedImage raster;

	public CachedFeatureLayer(Collection<PolygonFeature> features) {
		this(features, 1000, 1000);
	}
			
	public CachedFeatureLayer(Collection<PolygonFeature> features, int numPointsX, int numPointsY) {
		super(features);
		this.rasterWidth = numPointsX;
		this.rasterHeight = numPointsY;
		this.raster = createRaster();
		double coordWidth = this.getBoundingBox().getEnvelopeInternal()
				.getWidth();
		double coordHeight = this.getBoundingBox().getEnvelopeInternal()
				.getHeight();
		this.scaleFactorX = (rasterWidth-1) / coordWidth;
		this.scaleFactorY = (rasterHeight-1) / coordHeight;
	}

	
	
	/* (non-Javadoc)
	 * @see net.g2lab.layer.PolygonFeatureLayer#getDoubleAttribute(java.lang.String, double, double)
	 */
	@Override
	public Double getDoubleAttribute(String attributeName, double x, double y) {
		int localX = (int) Math.round(x * scaleFactorX);
		int localY = (int) Math.round(y * scaleFactorY);
		int value = raster.getRaster().getSample(localX, localY, 0);
		return new Double(value);
	}

	private BufferedImage createRaster() {
		BufferedImage image = new BufferedImage(rasterWidth, rasterHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(new Color(0, 0, 0));
		g2d.fillRect(0, 0, image.getWidth()-1, image.getHeight()-1);
		AffineTransform transform = g2d.getTransform(); // returns a copy
		transform.scale(scaleFactorX, scaleFactorY);
		for (PolygonFeature feature : featureMap.values()) {
			g2d.setColor(new Color(200, 200, 200));
			drawGeometry(g2d, feature.getPolygon(), transform);
			int uncertainty = (int) ((UncertaintyDoublePolygonFeature)feature).getUncertainty();
			g2d.setColor(new Color(uncertainty));
			drawGeometry(g2d, feature.getPolygon(), transform);
		}
		return image;
	}

	public void drawGeometry(Graphics2D g2d, Geometry geom, AffineTransform transform) {
		LiteShape shape = new LiteShape(geom, transform, false);
		g2d.fill(shape);
	}

	public void setRasters(Map<String, BufferedImage> rasters) {
		this.rasters = rasters;
	}

	public Map<String, BufferedImage> getRasters() {
		return rasters;
	}

}
