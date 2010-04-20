package com.laamella.bitmask;

import junit.framework.TestCase;

public class BitmaskModifierTest extends TestCase {
	public void testScaleDown() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final String scaledPattern = BitmaskFactoryTest.readStringResource("/scaled_down_test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask actual = BitmaskModifier.scale(bitmask, 10, 10);
		assertEquals(scaledPattern, actual.toString());
	}

	public void testScaleEqual() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask actual = BitmaskModifier.scale(bitmask, bitmask.getWidth(), bitmask.getHeight());
		assertEquals(pattern + "\n", actual.toString());
	}

	public void testScaleUp() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final String scaledPattern = BitmaskFactoryTest.readStringResource("/scaled_up_test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask actual = BitmaskModifier.scale(bitmask, 100, 63);
		assertEquals(scaledPattern, actual.toString());
	}

	public void testScaleTo0() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask actual = BitmaskModifier.scale(bitmask, 0, 0);
		assertEquals(1, actual.getWidth());
		assertEquals(1, actual.getHeight());
		assertFalse(actual.getBit(0, 0));
	}

	public void testCopyConstructorCreatesEqualBitmasks() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask bitmask2 = new Bitmask(bitmask);
		assertEquals(bitmask.toString(), bitmask2.toString());
	}

	public void testCopyConstructorDoesNotShareData() {
		final String pattern = BitmaskFactoryTest.readStringResource("/test_pattern.txt");
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
		final Bitmask bitmask2 = new Bitmask(bitmask);
		bitmask.setBit(0, 0);
		assertFalse(bitmask.toString().equals(bitmask2.toString()));
	}

	public void testDrawAt0_0() {
		final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
		final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
		final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
		BitmaskModifier.draw(bitmask1, bitmask2, 0, 0);
		assertEquals(expected, bitmask1);
	}

	public void testDrawLargeBitmaskOnTop() {
		final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
		final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
		final Bitmask scaled = BitmaskModifier.scale(bitmask2, bitmask1.getWidth() + 4, bitmask1.getHeight() + 4);
		final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
		BitmaskModifier.draw(bitmask1, scaled, -2, -2);
		System.out.println(bitmask1);
		//		assertEquals(expected, bitmask1);
	}

	public void testEraseLargeBitmaskOnTop() {
		final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
		final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
		final Bitmask scaled = BitmaskModifier.scale(bitmask2, bitmask1.getWidth() + 4, bitmask1.getHeight() + 4);
		final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
		BitmaskModifier.erase(bitmask1, scaled, -2, -2);
		System.out.println(bitmask1);
		//		assertEquals(expected, bitmask1);
	}
}
