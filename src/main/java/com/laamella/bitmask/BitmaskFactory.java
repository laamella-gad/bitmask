package com.laamella.bitmask;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

/**
 * Factory for creating bitmasks in all kinds of ways that do not involve
 * another Bitmask.
 */
public final class BitmaskFactory {
	private BitmaskFactory() {
		// Can't instantiate.
	}

	/**
	 * Create an empty bitmask. Technically identical to calling Bitmask's
	 * constructor.
	 * 
	 * @param width
	 *            width of the new bitmask.
	 * @param height
	 *            height of the new bitmask.
	 * @return a new, empty bitmask.
	 */
	public static Bitmask createEmptyBitmask(final int width, final int height) {
		return new Bitmask(width, height);
	}

	/**
	 * Create a bitmask with bits set for every pixel in the image that has an
	 * alpha value of more than threshold.
	 * 
	 * @param image
	 *            source image.
	 * @param threshold
	 *            a value in the range of 0..1, the higher it is, the bigger the
	 *            range of alpha values that is considered transparent.
	 * @return a new bitmask for the source image.
	 * @throws IllegalArgumentException
	 *             when the image has no alpha channel.
	 */
	public static Bitmask createBitmaskFromAlphaChannel(final BufferedImage image, final double threshold) {
		final int intThreshold = (int) (threshold * 255);
		final Bitmask bitmask = createEmptyBitmask(image.getWidth(), image.getHeight());
		final Raster alphaRaster = image.getAlphaRaster();
		if (alphaRaster == null) {
			throw new IllegalArgumentException("Image has no alpha channel");
		}
		final int[] pixelData = new int[1];
		for (int x = 0; x < alphaRaster.getWidth(); x++) {
			for (int y = 0; y < alphaRaster.getHeight(); y++) {
				alphaRaster.getPixel(x, y, pixelData);
				if (pixelData[0] > intThreshold) {
					bitmask.setBit(x, y);
				}
			}
		}
		return bitmask;
	}

	/**
	 * Create a bitmask with bits set for every pixel in the image that does not
	 * exactly match the color key.
	 * 
	 * @param image
	 *            source image.
	 * @param key
	 *            the color which is used in the image to indicate transparency.
	 * @return a new bitmask for the source image.
	 */
	public static Bitmask createBitmaskFromColorKey(final BufferedImage image, final Color key) {
		final Bitmask bitmask = createEmptyBitmask(image.getWidth(), image.getHeight());
		final Raster raster = image.getRaster();
		final ColorModel colorModel = image.getColorModel();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				final Object pixel = raster.getDataElements(x, y, null);
				if (!(colorModel.getBlue(pixel) == key.getBlue() && colorModel.getRed(pixel) == key.getRed() && colorModel
						.getGreen(pixel) == key.getGreen())) {
					bitmask.setBit(x, y);
				}
			}
		}
		return bitmask;
	}

	/**
	 * Create a bitmask with bits set for every character in the source string
	 * that matches oneBitCharacter.
	 * 
	 * @param art
	 *            a string containing lines of the bitmask, separated by EOLs.
	 *            All lines should be of equal length.
	 * @param oneBitCharacter
	 *            the character that is interpreted as a set bit (a pixel that
	 *            will collide.)
	 * @return a new bitmask for the source image.
	 */
	public static Bitmask createBitmaskFromAsciiArt(final String art, final char oneBitCharacter) {
		final String[] split = art.split("\n");
		final Bitmask bitmask = new Bitmask(split[0].length(), split.length);
		for (int y = 0; y < split.length; y++) {
			final String line = split[y];
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == oneBitCharacter) {
					bitmask.setBit(x, y);
				}
			}
		}
		return bitmask;
	}
}
