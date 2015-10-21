package net.g2lab.uncertainty;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SpectralUncertaintyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
			public void testComputeSimilaritySimilarity1() throws Exception {
				Vector spectralVector = new BasicVector(new double [] {1,2,3});
				Vector meanVector = new BasicVector(new double [] {1,2,3});
				assertThat(SpectralUncertainty.computeSimilarity(spectralVector, meanVector), is(1.));
			}
	
	@Test
			public void testComputeSimilaritySimilarity0() throws Exception {
				Vector spectralVector = new BasicVector(new double [] {1,2,3,4,5});
				Vector meanVector = new BasicVector(new double [] {-1,-2,-3,-4,-5});
				assertThat(SpectralUncertainty.computeSimilarity(spectralVector, meanVector), is(0.));
			}
	
	@Test
			public void testComputeSimilaritySimilarity05() throws Exception {
				Vector spectralVector = new BasicVector(new double [] {0,1});
				Vector meanVector = new BasicVector(new double [] {1,0});
				assertThat(SpectralUncertainty.computeSimilarity(spectralVector, meanVector), is(0.5));
			}
	

}
