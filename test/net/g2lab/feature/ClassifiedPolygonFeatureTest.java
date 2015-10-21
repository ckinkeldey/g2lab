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
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ClassifiedPolygonFeatureTest extends TestBase {

	private ClassifiedPolygonFeature feature;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.feature = (ClassifiedPolygonFeature) createTestPolygonFeatures().get(0);
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
	}

	@Test
	public void testGetBoundaries() {
		assertThat(feature.getOuterRing(), is(notNullValue()));
		assertThat(feature.getInnerRing(), is(notNullValue()));
	}

	@Test
	public void testHasInnerRing() {
		assertThat(feature.hasInnerRing(), is(true));
	}

}
