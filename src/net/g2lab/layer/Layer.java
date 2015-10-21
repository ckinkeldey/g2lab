package net.g2lab.layer;

import java.awt.Image;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author  <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 */
public interface Layer {

	
	/**
	 */
	Envelope getEnvelope();
	
	Integer getIntAttribute(String attributeName, double x, double y);
	
	Double getDoubleAttribute(String attributeName, double x, double y);
	
	String getStringAttribute(String attributeName, double x, double y);

	Image getAsImage(int width, int height);
}
