/**
 * 
 */
package net.g2lab.layer;


import static net.g2lab.feature.UncertaintyDoublePolygonFeature.ATTRIBUTE_UNCERTAINTY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import net.g2lab.feature.TestBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;


/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class RasterLayerTest {

	private RasterLayer layer;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.layer = new RasterLayer(TestBase.createRaster());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
		public void testCreateEnvelope() {
			Envelope envelope = layer.getEnvelope();
			assertThat(envelope.getMinX(), is(0.));
			assertThat(envelope.getMaxX(), is(1.));
			assertThat(envelope.getMinY(), is(0.));
			assertThat(envelope.getMaxY(), is(1.));
		}

	@Test
	public void testGetAttributeNoData() {
		assertThat(layer.getIntAttribute("attribute", 100, 100), is(AbstractLayer.NO_DATA_INT));
	}
	
	@Test
	public void testGetAttributeInteger() {
		assertThat(layer.getIntAttribute(ATTRIBUTE_UNCERTAINTY, .75, .75), is(1));
	}
	
	@Test
	public void testGetAttributeDouble() {
		assertThat(layer.getDoubleAttribute(ATTRIBUTE_UNCERTAINTY, .75, .75), is(1.));
	}
	

}
