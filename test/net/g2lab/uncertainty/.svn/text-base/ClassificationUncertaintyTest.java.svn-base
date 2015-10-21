package net.g2lab.uncertainty;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClassificationUncertaintyTest {

	private ClassificationUncertainty uncertainty;
	private int[] clusterMeans;

	@Before
	public void setUp() throws Exception {
		this.clusterMeans = new int[] {0, 10, 3, 5};
		uncertainty = new ClassificationUncertainty(clusterMeans, getZeroUncertaintyPixels());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetZeroUncertainty() {
		assertThat(uncertainty, is(notNullValue()));
		int[] uncert = uncertainty.getUncertainty();
		assertThat(uncert[0], is(0));
	}
	
	@Test
	public void testGetMaxUncertainty() {
		uncertainty = new ClassificationUncertainty(clusterMeans, getExampleUncertaintyPixels());
		assertThat(uncertainty, is(notNullValue()));
		int[] uncert = uncertainty.getUncertainty();
		assertThat(uncert[0], is(100));
	}
	
	private int[][] getZeroUncertaintyPixels() {
		return new int[][] {
				new int[] {0, 10, 3, 5}
			};
	}
	
	private int[][] getExampleUncertaintyPixels() {
		return new int[][] {
				new int[] {10, 10, 3, 5}
			};
	}
	

}
