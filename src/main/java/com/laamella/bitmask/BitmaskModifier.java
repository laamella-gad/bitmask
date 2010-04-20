package com.laamella.bitmask;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

/**
 * Various drawing tools that were separated from Bitmask to prevent bloat.
 */
public final class BitmaskModifier {
	private BitmaskModifier() {
		// can't instantiate
	}

	private static final int AND = 0;
	private static final int ERASE = 1;

	/**
	 * Apply some function to the bits in a with the bits in b that overlap them
	 * using an offset.
	 * 
	 * @param b
	 * @param xOffset
	 * @param yOffset
	 * @param operation
	 */
	private static final void apply(final Bitmask a, final Bitmask b, final int xOffset, final int yOffset,
			final int operation) {
		if (!a.overlapsBoundingRectangleOf(b, xOffset, yOffset)) {
			return;
		}
		// Find out what area in this bitmask will be combined with b
		int left = xOffset;
		int right = xOffset + b.getWidth();
		int top = yOffset;
		int bottom = yOffset + b.getHeight();
		if (left < 0) {
			left = 0;
		}
		if (right > a.getWidth()) {
			right = a.getWidth();
		}
		if (top < 0) {
			top = 0;
		}
		if (bottom > a.getHeight()) {
			bottom = a.getHeight();
		}
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				final boolean aBit = a.getBit(x, y);
				final boolean bBit = b.getBit(x - xOffset, y - yOffset);
				boolean resultBit;
				switch (operation) {
				case AND:
					resultBit = aBit | bBit;
					break;
				case ERASE:
					resultBit = aBit ^ bBit;
					break;
				default:
					throw new IllegalArgumentException("Don't know operation " + operation);
				}
				if (resultBit) {
					a.setBit(x, y);
				} else {
					a.clearBit(x, y);
				}
			}
		}
	}

	/**
	 * Draws mask b onto mask a (bitwise OR). Can be used to compose large (game
	 * background?) mask from several submasks, which may speed up the testing.
	 * 
	 * @param bitmaskModifier
	 */
	public final static void draw(final Bitmask a, final Bitmask b, final int xOffset, final int yOffset) {
		apply(a, b, xOffset, yOffset, AND);
	}

	/**
	 * @see Bitmask#draw(Bitmask, int, int)
	 */
	public final static void draw(final Bitmask a, final Bitmask b, final Point2D offset) {
		draw(a, b, (int) offset.getX(), (int) offset.getY());
	}

	/**
	 * Erase any set bits on this bitmask that are set in bitmask b.
	 */
	public final static void erase(final Bitmask a, final Bitmask b, final int xOffset, final int yOffset) {
		apply(a, b, xOffset, yOffset, ERASE);
	}

	/**
	 * @see Bitmask#erase(Bitmask, int, int)
	 */
	public final static void erase(final Bitmask a, final Bitmask b, final Point2D offset) {
		erase(a, b, (int) offset.getX(), (int) offset.getY());
	}

	/**
	 * Return a new scaled Bitmask, with dimensions w x h. The algorithm makes
	 * no attempt at smoothing the result. If either w or h is less than one, a
	 * clear 1x1 mask is returned.
	 */
	public final static Bitmask scale(final Bitmask source, final int scaledWidth, final int scaledHeight) {
		if (scaledWidth < 1 || scaledHeight < 1) {
			return new Bitmask(1, 1);
		}
		final Bitmask newMask = new Bitmask(scaledWidth, scaledHeight);
		final double xFactor = (double) source.getWidth() / scaledWidth;
		final double yFactor = (double) source.getHeight() / scaledHeight;
		for (int x = 0; x < scaledWidth; x++) {
			for (int y = 0; y < scaledHeight; y++) {
				if (source.getBit((int) ((x + 0.5) * xFactor), (int) ((y + 0.5) * yFactor))) {
					newMask.setBit(x, y);
				}
			}
		}
		return newMask;
	}

	/**
	 * @see Bitmask#scale(int, int)
	 */
	public static Bitmask scale(final Bitmask source, final Dimension2D size) {
		return scale(source, (int) size.getWidth(), (int) size.getHeight());
	}

	/**
	 * Convolve b into a, drawing the output into o, shifted by offset. If
	 * offset is 0, then the (x,y) bit will be set if and only if
	 * Bitmask_overlap(a, b, x - b->w - 1, y - b->h - 1) returns true.
	 * 
	 * <pre>
	 * Modifies bits o[xoffset ... xoffset + a->w + b->w - 1)
	 *                  [yoffset ... yoffset + a->h + b->h - 1).
	 * </pre>
	 */
	public static void convolve(final Bitmask a, final Bitmask b, final Bitmask o, int xoffset, int yoffset) {
		xoffset += b.getWidth() - 1;
		yoffset += b.getHeight() - 1;
		for (int y = 0; y < b.getHeight(); y++) {
			for (int x = 0; x < b.getWidth(); x++) {
				if (b.getBit(x, y)) {
					// FIXME not sure what this is supposed to do
					draw(a, o, xoffset - x, yoffset - y);
				}
			}
		}
	}

	/**
	 * @see Bitmask#convolve(Bitmask, Bitmask, int, int)
	 */
	public static void convolve(final Bitmask a, final Bitmask b, final Bitmask o, final Point2D offset) {
		convolve(a, b, o, (int) offset.getX(), (int) offset.getY());
	}

}
