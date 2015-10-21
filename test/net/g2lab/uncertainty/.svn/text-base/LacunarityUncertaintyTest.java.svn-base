/**
 * 
 */
package net.g2lab.uncertainty;


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
public class LacunarityUncertaintyTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testComputeMembership() {
		assertThat(LacunarityUncertainty.computeMembership(0.5), is(0.));
		assertThat(LacunarityUncertainty.computeMembership(1.5), is(0.));
		assertThat(LacunarityUncertainty.computeMembership(1.075), is(0.));
		assertThat(LacunarityUncertainty.computeMembership(1.1), is(0.5));
		assertThat(LacunarityUncertainty.computeMembership(1.125), is(1.));
		assertThat(LacunarityUncertainty.computeMembership(1.15), is(0.5));
		assertThat(LacunarityUncertainty.computeMembership(1.175), is(0.));
	}
	

}
