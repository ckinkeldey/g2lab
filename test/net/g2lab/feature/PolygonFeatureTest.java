/**
 * 
 */
package net.g2lab.feature;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class PolygonFeatureTest {

	private static final String ID = "0";
	private PolygonFeature feature;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.feature = new PolygonFeature(ID, TestBase.createPolygon());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreation() {
		assertThat(feature, is(notNullValue()));
		assertThat(feature.getId(), is(ID));
		assertThat(feature.geometry, is(notNullValue()));
	}
	
//	@Test
//	public void testGetAAsSimpleFeature() {
//		simpleFeature = feature.getAsSimpleFeature();
//	}
}
