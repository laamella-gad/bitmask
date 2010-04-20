package com.laamella.bitmask;

import junit.framework.TestCase;

public class BitmaskTest extends TestCase {
	private final Bitmask tenByTenBitmask = new Bitmask(10, 10);

	public void testDataSize1() {
		assertEquals(10, Tools.getBits(tenByTenBitmask).length);
	}

	public void testDataSize2() {
		assertEquals(3, Tools.getBits(new Bitmask(64, 3)).length);
	}

	public void testDataSize3() {
		assertEquals(6, Tools.getBits(new Bitmask(65, 3)).length);
	}

	public void testDataSize4() {
		assertEquals(6, Tools.getBits(new Bitmask(128, 3)).length);
	}

	public void testEmptyAfterConstruction() {
		assertEquals(0, tenByTenBitmask.countBits());
	}

	public void testGetSetBit1() {
		assertFalse(tenByTenBitmask.getBit(3, 6));
		tenByTenBitmask.setBit(3, 6);
		assertTrue(tenByTenBitmask.getBit(3, 6));
	}

	public void testGetSetBit2() {
		final Bitmask bitMask = new Bitmask(64, 2);
		assertFalse(bitMask.getBit(63, 0));
		bitMask.setBit(63, 0);
		assertTrue(bitMask.getBit(63, 0));
	}

	public void testGetSetBit3() {
		assertFalse(tenByTenBitmask.getBit(0, 1));
		tenByTenBitmask.setBit(0, 1);
		assertTrue(tenByTenBitmask.getBit(0, 1));
	}

	public void testSetClearBit1() {
		tenByTenBitmask.setBit(2, 1);
		assertTrue(tenByTenBitmask.getBit(2, 1));
		tenByTenBitmask.clearBit(2, 1);
		assertFalse(tenByTenBitmask.getBit(2, 1));
	}

	public void testFill() {
		final Bitmask bitMask = new Bitmask(200, 3);
		bitMask.fill();
		assertEquals(600, bitMask.countBits());
	}

	public void testInvert1() {
		final Bitmask bitMask = new Bitmask(200, 3);
		bitMask.invert();
		assertEquals(600, bitMask.countBits());
		bitMask.invert();
		assertEquals(0, bitMask.countBits());
	}

	public void testInvert2() {
		final Bitmask bitMask = new Bitmask(200, 3);
		bitMask.setBit(0, 2);
		bitMask.setBit(199, 0);
		bitMask.invert();
		assertEquals(598, bitMask.countBits());
		bitMask.invert();
		assertEquals(2, bitMask.countBits());
	}

	public void testOverlap1() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertTrue(bitMask1.overlaps(bitMask2, 0, 0));
	}

	public void testOverlap2() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlaps(bitMask2, 1, 0));
	}

	public void testOverlap3() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlaps(bitMask2, -1, 0));
	}

	public void testOverlap4() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlaps(bitMask2, 0, 1));
	}

	public void testOverlap5() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlaps(bitMask2, 0, -1));
	}

	public void testOverlap6() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		assertFalse(bitMask1.overlaps(bitMask2, 0, 0));
	}

	public void testOverlap7() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlaps(bitMask2, 0, 0));
	}

	public void testOverlap8() {
		final Bitmask bitMask1 = new Bitmask(2, 2);
		final Bitmask bitMask2 = new Bitmask(10, 10);
		bitMask1.fill();
		bitMask2.fill();
		bitMask2.clearBit(2, 2);
		bitMask2.clearBit(2, 3);
		bitMask2.clearBit(3, 2);
		bitMask2.clearBit(3, 3);
		assertFalse(bitMask1.overlaps(bitMask2, -2, -2));
	}

	public void testOverlap9() {
		final Bitmask bitMask1 = new Bitmask(2, 2);
		final Bitmask bitMask2 = new Bitmask(10, 10);
		bitMask1.fill();
		bitMask2.fill();
		bitMask2.clearBit(2, 2);
		bitMask2.clearBit(2, 3);
		bitMask2.clearBit(3, 2);
		bitMask2.clearBit(3, 3);
		assertTrue(bitMask1.overlaps(bitMask2, -3, -2));
	}

	public void testOverlap10() {
		final Bitmask bitMask1 = new Bitmask(2, 2);
		final Bitmask bitMask2 = new Bitmask(10, 10);
		bitMask1.fill();
		bitMask2.fill();
		bitMask2.clearBit(2, 2);
		bitMask2.clearBit(2, 3);
		bitMask2.clearBit(3, 2);
		bitMask2.clearBit(3, 3);
		assertFalse(bitMask2.overlaps(bitMask1, -2, -2));
	}

	public void testOverlap11() {
		final Bitmask bitMask1 = Tools.makeOnOffPatternBitmask(1000, 10);
		final Bitmask bitMask2 = Tools.makeOnOffPatternBitmask(1000, 10);
		bitMask2.invert();
		assertFalse(bitMask2.overlaps(bitMask1, 0, 0));
	}

	public void testOverlap12() {
		final Bitmask bitMask1 = Tools.makeOnOffPatternBitmask(1000, 10);
		final Bitmask bitMask2 = Tools.makeOnOffPatternBitmask(1000, 10);
		bitMask2.invert();
		assertFalse(bitMask2.overlaps(bitMask1, 1, 1));
	}

	public void testOverlap13() {
		final Bitmask bitMask1 = Tools.makeOnOffPatternBitmask(1000, 10);
		final Bitmask bitMask2 = Tools.makeOnOffPatternBitmask(1000, 10);
		bitMask2.invert();
		assertFalse(bitMask2.overlaps(bitMask1, 1, -1));
	}

	public void testOverlap14() {
		final Bitmask bitMask1 = Tools.makeOnOffPatternBitmask(1000, 10);
		final Bitmask bitMask2 = Tools.makeOnOffPatternBitmask(1000, 10);
		bitMask2.invert();
		assertTrue(bitMask2.overlaps(bitMask1, 1, 0));
	}

	public void sillyBenchmark() {
		final Bitmask bitMask1 = Tools.makeOnOffPatternBitmask(100, 100);
		final Bitmask bitMask2 = Tools.makeOnOffPatternBitmask(100, 100);
		bitMask2.invert();
		final long start = System.currentTimeMillis();
		final int times = 1000000;
		for (int time = 0; time < times; time++) {
			bitMask1.overlaps(bitMask2, 0, 0);
		}
		final long end = System.currentTimeMillis();
		System.out.println((end - start) / (double) times);
	}
}
