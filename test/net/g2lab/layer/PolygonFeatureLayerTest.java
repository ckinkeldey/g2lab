/**
 * 
 */
package net.g2lab.layer;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import net.g2lab.feature.TestBase;
import net.g2lab.feature.UncertaintyDoublePolygonFeature;
import net.g2lab.feature.UncertaintyIntPolygonFeature;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;


/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class PolygonFeatureLayerTest {

	private PolygonFeatureLayer layer;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.layer = new PolygonFeatureLayer(TestBase.createFeaturesInteger());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetEnvelope() {
		Envelope envelope = layer.getEnvelope();
		assertThat(envelope.getMinX(), is(0.));
		assertThat(envelope.getMaxX(), is(1.));
		assertThat(envelope.getMinY(), is(0.));
		assertThat(envelope.getMaxY(), is(1.));
	}

	@Test
	public void testGetAttributeNoData() {
		assertThat(layer.getIntAttribute("attribute", 100, 100), is(-1));
	}
	
	@Test
	public void testGetAttributeInteger() {
		assertThat(layer.getIntAttribute(UncertaintyIntPolygonFeature.ATTRIBUTE_UNCERTAINTY, .5, .5), is(5));
	}
	
	@Test
	public void testGetAttributeDouble() {
		this.layer = new PolygonFeatureLayer(TestBase.createFeaturesDouble());
		assertThat(layer.getDoubleAttribute(UncertaintyDoublePolygonFeature.ATTRIBUTE_UNCERTAINTY, .5, .5), is(4.5));
	}
	

}
