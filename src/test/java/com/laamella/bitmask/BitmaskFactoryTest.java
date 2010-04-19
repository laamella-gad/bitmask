package com.laamella.bitmask;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class BitmaskFactoryTest extends TestCase {

	public static String readStringResource(final String resourceName) {
		try {
			return FileUtils.readFileToString(new File(resourceName.getClass().getResource(resourceName).getFile()));
		} catch (final IOException e) {
			fail(e.getMessage());
			return null;
		}
	}

	public void testAlpha05() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 0.5);
		assertEquals(readStringResource("/circle_with_alpha_at_0.5.txt"), bitmask.toString());
	}

	public void testAlpha0() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 0);
		assertEquals(readStringResource("/circle_with_alpha_at_0.txt"), bitmask.toString());
	}

	public void testAlpha1() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 1);
		assertEquals(readStringResource("/circle_with_alpha_at_1.txt"), bitmask.toString());
	}

	public void testNoAlphaBreaks() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
		try {
			BitmaskFactory.createBitmaskFromAlphaChannel(image, 1);
		} catch (final IllegalArgumentException exception) {
			return;
		}
		fail();
	}

	public void testColorKeyWhite() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, Color.WHITE);
		assertEquals(readStringResource("/color_keyed_white.txt"), bitmask.toString());
	}

	public void testColorKeyRed() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, new Color(232, 20, 20));
		assertEquals(readStringResource("/color_keyed_red.txt"), bitmask.toString());
	}

	public void testColorKeyBlack() throws IOException {
		final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
		final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, Color.BLACK);
		assertEquals(readStringResource("/color_keyed_black.txt"), bitmask.toString());
	}
}
