package com.mycompany.populationdensity;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class PopularityDensityTestCase extends TestCase {

	PoupularityDensityInterface[] imps;

	PoupularityDensityInterface sequential;
	PoupularityDensityInterface parllel;
	PoupularityDensityInterface smartSeq;
	PoupularityDensityInterface smartPar;
	PoupularityDensityInterface locked;
	PoupularityDensityInterface impEmpty;
	PoupularityDensityInterface badBounds;
	PoupularityDensityInterface zeroPop;

	CensusData data;
	CensusData zero;
	CensusData empty;

	@Before
	public void setUp() throws Exception {
		FileHandler fileHandler = new FileHandler();

		data = fileHandler.parse("C:/Users/bhargav/Desktop/Data/CenPop2010.txt");
		zero = fileHandler.parse("C:/Users/bhargav/Desktop/Data/zeroPop.txt");
		empty = fileHandler.parse("C:/Users/bhargav/Desktop/Data/empty.txt");

		imps = new PopulationQueryVerison[5];

		sequential = new SimpleAndSequential_V1(20, 25, data);
		sequential.preprocess();
		imps[0] = sequential;

		parllel = new SimpleAndParallel_V2(20, 25, data);
		parllel.preprocess();
		imps[1] = parllel;

		smartSeq = new SmarterAndSequential_V3(20, 25, data);
		smartSeq.preprocess();
		imps[2] = smartSeq;

		smartPar = new SmarterAndParallel_V4(20, 25, data);
		smartPar.preprocess();
		imps[3] = smartPar;

		locked = new SmarterAndLockBased_V5(20, 25, data);
		locked.preprocess();
		imps[4] = locked;

		zeroPop = new SimpleAndSequential_V1(20, 25, zero);
		zeroPop.preprocess();
		impEmpty = new SimpleAndSequential_V1(20, 25, empty);
		impEmpty.preprocess();
	}

	/**
	 * This method test total population
	 */
	public void testTotalPopulation() {
		for (int i = 0; i < imps.length; i++) {
			assertEquals(imps[i].getPopulation(), 312471327);
		}
	}

	

	public void testMiddleThreeColumnsPopulation() {
		for (int i = 0; i < imps.length; i++) {
			assertEquals(imps[i].execute(9, 1, 11, 25), 52392739);
		}
	}
	public void testWithCoordinates() {
		for (int i = 0; i < imps.length; i++) {
			assertEquals(imps[i].execute(1, 12, 9, 25), 710231);
		}
	}

	public void testBottomFourRowsPopulation() {
		for (int i = 0; i < imps.length; i++) {
			assertEquals(imps[i].execute(1, 1, 20, 4), 36493611);
		}
	}

	public void testZeroPopulation() {
		assertEquals(zeroPop.execute(1, 1, 20, 25), 0);
	}

	public void emptyFilePopulation() {
		assertEquals(impEmpty.execute(1, 1, 20, 25), 0);
	}

	public void testWholeGrid() {
		for (int i = 0; i < imps.length; i++) {
			assertEquals(imps[i].execute(1, 1, 20, 25), 312471327);
		}
	}


	@Test(expected = java.lang.IndexOutOfBoundsException.class)
	public void badBoundsThrows() {
		badBounds = new SimpleAndSequential_V1(0, 25, data);
	}
}
