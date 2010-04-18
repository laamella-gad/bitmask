package com.laamella.bitmask;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

public class BitmaskTest extends TestCase {
	private final Bitmask tenByTenBitmask = new Bitmask(10, 10);

	private final long[] getBits(final Bitmask bitmask) {
		try {
			return (long[]) PrivateAccessor.getField(bitmask, "bits");
		} catch (final NoSuchFieldException e) {
			fail("For some reason, the bits field is inaccessible");
			return null;
		}
	}

	private final String dumpLongs(final Bitmask bitmask) {
		final StringBuffer dump = new StringBuffer();
		final long[] bits = getBits(bitmask);
		for (int i = 0; i < bits.length; i++) {
			dump.append(Long.toBinaryString(bits[i])).append("\n");
		}
		return dump.toString();
	}

	public void testDataSize1() {
		assertEquals(10, getBits(tenByTenBitmask).length);
	}

	public void testDataSize2() {
		assertEquals(3, getBits(new Bitmask(64, 3)).length);
	}

	public void testDataSize3() {
		assertEquals(6, getBits(new Bitmask(65, 3)).length);
	}

	public void testDataSize4() {
		assertEquals(6, getBits(new Bitmask(128, 3)).length);
	}

	public void testToString() {
		System.out.println(tenByTenBitmask);
	}

	public void testEmptyAfterConstruction() {
		assertEquals(0, tenByTenBitmask.count());
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
		assertEquals(600, bitMask.count());
	}

	public void testInvert1() {
		final Bitmask bitMask = new Bitmask(200, 3);
		bitMask.invert();
		assertEquals(600, bitMask.count());
		bitMask.invert();
		assertEquals(0, bitMask.count());
	}

	public void testInvert2() {
		final Bitmask bitMask = new Bitmask(200, 3);
		bitMask.setBit(0, 2);
		bitMask.setBit(199, 0);
		bitMask.invert();
		assertEquals(598, bitMask.count());
		bitMask.invert();
		assertEquals(2, bitMask.count());
	}

	public void testOverlap1() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertTrue(bitMask1.overlap(bitMask2, 0, 0));
	}

	public void testOverlap2() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlap(bitMask2, 1, 0));
	}

	public void testOverlap3() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlap(bitMask2, -1, 0));
	}

	public void testOverlap4() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlap(bitMask2, 0, 1));
	}

	public void testOverlap5() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlap(bitMask2, 0, -1));
	}

	public void testOverlap6() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		bitMask1.setBit(0, 0);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		assertFalse(bitMask1.overlap(bitMask2, 0, 0));
	}

	public void testOverlap7() {
		final Bitmask bitMask1 = new Bitmask(1, 1);
		final Bitmask bitMask2 = new Bitmask(1, 1);
		bitMask2.setBit(0, 0);
		assertFalse(bitMask1.overlap(bitMask2, 0, 0));
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
		assertFalse(bitMask1.overlap(bitMask2, -2, -2));
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
		assertTrue(bitMask1.overlap(bitMask2, -3, -2));
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
		assertFalse(bitMask2.overlap(bitMask1, -2, -2));
	}

	private final void makeOnOffPattern(final Bitmask bitMask) {
		for (int x = 0; x < bitMask.getWidth(); x++) {
			for (int y = 0; y < bitMask.getHeight(); y++) {
				if ((x + y) % 2 == 1) {
					bitMask.setBit(x, y);
				}
			}
		}
	}

	public void testOverlap11() {
		final Bitmask bitMask1 = new Bitmask(1000, 10);
		final Bitmask bitMask2 = new Bitmask(1000, 10);
		makeOnOffPattern(bitMask1);
		makeOnOffPattern(bitMask2);
		bitMask2.invert();
		assertFalse(bitMask2.overlap(bitMask1, 0, 0));
	}

	public void testOverlap12() {
		final Bitmask bitMask1 = new Bitmask(1000, 10);
		final Bitmask bitMask2 = new Bitmask(1000, 10);
		makeOnOffPattern(bitMask1);
		makeOnOffPattern(bitMask2);
		bitMask2.invert();
		assertFalse(bitMask2.overlap(bitMask1, 1, 1));
	}

	public void testOverlap13() {
		final Bitmask bitMask1 = new Bitmask(1000, 10);
		final Bitmask bitMask2 = new Bitmask(1000, 10);
		makeOnOffPattern(bitMask1);
		makeOnOffPattern(bitMask2);
		bitMask2.invert();
		assertFalse(bitMask2.overlap(bitMask1, 1, -1));
	}

	public void testOverlap14() {
		final Bitmask bitMask1 = new Bitmask(1000, 10);
		final Bitmask bitMask2 = new Bitmask(1000, 10);
		makeOnOffPattern(bitMask1);
		makeOnOffPattern(bitMask2);
		bitMask2.invert();
		assertTrue(bitMask2.overlap(bitMask1, 1, 0));
	}

	public void sillyBenchmark() {
		final Bitmask bitMask1 = new Bitmask(100, 100);
		final Bitmask bitMask2 = new Bitmask(100, 100);
		makeOnOffPattern(bitMask1);
		makeOnOffPattern(bitMask2);
		bitMask2.invert();
		final long start = System.currentTimeMillis();
		final int times = 1000000;
		for (int time = 0; time < times; time++) {
			bitMask1.overlap(bitMask2, 0, 0);
		}
		final long end = System.currentTimeMillis();
		System.out.println((end - start) / (double) times);
	}
}
