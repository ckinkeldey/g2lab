/**
 * 
 */
package net.g2lab.layer;

import java.awt.Image;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public abstract class AbstractLayer implements Layer {

	public static final Integer NO_DATA_INT = null;
	public static final double NO_DATA_DOUBLE = Double.NaN;
	public static final String NO_DATA_STRING = "";

	protected int widthPx, heightPx;
	protected float scaleX, scaleY;
	
	public abstract Envelope getEnvelope();
	
	public abstract Image getAsImage(int width, int height);
	
	protected double getX(int xPx) {
		return xPx / scaleX + getEnvelope().getMinX();
	}
	
	protected double getY(int yPx) {
		return (heightPx - yPx) / scaleY + getEnvelope().getMinY();
	}
	
	public int getXCoordPx(double x) {
		return (int) Math.round(scaleX * (x - getEnvelope().getMinX()));
	}

	public int getYCoordPx(double y) {
		return (int) (heightPx - Math.round(scaleY * (y - getEnvelope().getMinY())));
	}
	
	protected int getWidthPx(double width) {
		return (int) Math.round(scaleX * width);
	}

	protected int getHeightPx(double height) {
		return (int) Math.round(scaleY * height);
	}
	
}
