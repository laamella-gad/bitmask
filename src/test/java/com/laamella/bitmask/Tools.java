package com.laamella.bitmask;

import org.junit.Assert;

import junitx.util.PrivateAccessor;

public class Tools {
	public static String dumpLongs(final Bitmask bitmask) {
		final StringBuffer dump = new StringBuffer();
		final long[] bits = getBits(bitmask);
		for (int i = 0; i < bits.length; i++) {
			dump.append(Long.toBinaryString(bits[i])).append("\n");
		}
		return dump.toString();
	}

	public static Bitmask makeOnOffPatternBitmask(final int width, final int height) {
		final Bitmask bitMask = BitmaskFactory.createEmptyBitmask(width, height);
		for (int x = 0; x < bitMask.getWidth(); x++) {
			for (int y = 0; y < bitMask.getHeight(); y++) {
				if ((x + y) % 2 == 1) {
					bitMask.setBit(x, y);
				}
			}
		}
		return bitMask;
	}

	public static Bitmask createBitmaskFromResource(final String resourceName) {
		final String string = BitmaskFactoryTest.readStringResource(resourceName);
		return BitmaskFactory.createBitmaskFromAsciiArt(string, 'o');
	}

	public static long[] getBits(final Bitmask bitmask) {
		try {
			return (long[]) PrivateAccessor.getField(bitmask, "bits");
		} catch (final NoSuchFieldException e) {
			Assert.fail("For some reason, the bits field is inaccessible");
			return null;
		}
	}

}
