/**
 * Bitmask 1.7 - A pixel-perfect collision detection library.
 * 
 * Copyright (C) 2002-2005 Ulf Ekstrom except for the bitcount function
 * which is copyright (C) Donald W. Gillies, 1992.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.laamella.bitmask;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A Bitmask is a simple array of bits, which can be used for highly efficient
 * 2d collision detection. Set 'unoccupied' area to zero and occupies areas to
 * one and use the overlap*() functions to check for collisions.
 * <p/>
 * All methods come in two variants: one with a separate x and y parameter, and
 * one with a Point2d, which is the superclass of all Java Point classes. The
 * Point2d version always calls the x and y version of the method.
 * <p/>
 * For various ways to create bitmasks, see {@link BitmaskFactory}.
 */
public final class Bitmask implements Serializable {
	private static final long BITMASK_W_LEN = Long.SIZE;
	private static final long BITMASK_W_MASK = BITMASK_W_LEN - 1;
	private static final long[] BITMASK_N = { 0x1L, 0x2L, 0x4L, 0x8L, 0x10L, 0x20L, 0x40L, 0x80L, 0x100L, 0x200L,
			0x400L, 0x800L, 0x1000L, 0x2000L, 0x4000L, 0x8000L, 0x10000L, 0x20000L, 0x40000L, 0x80000L, 0x100000L,
			0x200000L, 0x400000L, 0x800000L, 0x1000000L, 0x2000000L, 0x4000000L, 0x8000000L, 0x10000000L, 0x20000000L,
			0x40000000L, 0x80000000L, 0x100000000L, 0x200000000L, 0x400000000L, 0x800000000L, 0x1000000000L,
			0x2000000000L, 0x4000000000L, 0x8000000000L, 0x10000000000L, 0x20000000000L, 0x40000000000L,
			0x80000000000L, 0x100000000000L, 0x200000000000L, 0x400000000000L, 0x800000000000L, 0x1000000000000L,
			0x2000000000000L, 0x4000000000000L, 0x8000000000000L, 0x10000000000000L, 0x20000000000000L,
			0x40000000000000L, 0x80000000000000L, 0x100000000000000L, 0x200000000000000L, 0x400000000000000L,
			0x800000000000000L, 0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, 0x8000000000000000L };
	private static final long ALL_BITS_SET = ~0L;

	private final int w, h;
	// Storage is column by column.
	private final long[] bits;

	/**
	 * Creates a Bitmask of width w and height h, where w and h must both be
	 * greater than 0. The mask is automatically cleared when created.
	 */
	public Bitmask(final int w, final int h) {
		this.w = w;
		this.h = h;
		bits = new long[(int) (h * ((w - 1) / BITMASK_W_LEN + 1))];
		clear();
	}

	public Bitmask(final Bitmask bitmask) {
		this.w = bitmask.w;
		this.h = bitmask.h;
		this.bits = Arrays.copyOf(bitmask.bits, bitmask.bits.length);
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	/** Clears all bits in the mask */
	public void clear() {
		Arrays.fill(bits, 0);
	}

	/** Sets all bits in the mask */
	public void fill() {
		final long len = sizeInLongs(w - 1, h);
		final long shift = BITMASK_W_LEN - (w % BITMASK_W_LEN);
		final long cmask = ALL_BITS_SET >>> shift;

		for (int pixel = 0; pixel < len; pixel++) {
			bits[pixel] = ALL_BITS_SET;
		}
		for (int pixel = (int) len; pixel < len + h; pixel++) {
			bits[pixel] = cmask;
		}
	}

	private long sizeInLongs(final int w, final int h) {
		return h * (w / BITMASK_W_LEN);
	}

	/** Flips all bits in the mask */
	public void invert() {
		final long len = sizeInLongs(w - 1, h);
		final long shift = BITMASK_W_LEN - (w % BITMASK_W_LEN);
		final long cmask = ALL_BITS_SET >>> shift;

		for (int pixel = 0; pixel < len; pixel++) {
			bits[pixel] = ~bits[pixel];
		}
		for (int pixel = (int) len; pixel < len + h; pixel++) {
			bits[pixel] = cmask & ~bits[pixel];
		}
	}

	/** Counts the bits in the mask */
	public int countBits() {
		int total = 0;
		for (long pixel = 0; pixel < (h * ((w - 1) / BITMASK_W_LEN + 1)); pixel++) {
			total += Long.bitCount(bits[(int) pixel]);

		}
		return total;
	}

	/**
	 * @return true if the bit at (x,y) is set. Coordinates start at (0,0)
	 */
	public boolean getBit(final int x, final int y) {
		return (bits[(int) (x / BITMASK_W_LEN * h + y)] & BITMASK_N[(int) (x & BITMASK_W_MASK)]) != 0;
	}
	
	/**
	 * Sets the bit at (x,y)
	 */
	public void setBit(final int x, final int y) {
		bits[(int) (x / BITMASK_W_LEN * h + y)] |= BITMASK_N[(int) (x & BITMASK_W_MASK)];
	}

	/**
	 * Clears the bit at (x,y)
	 */
	public void clearBit(final int x, final int y) {
		bits[(int) (x / BITMASK_W_LEN * h + y)] &= ~BITMASK_N[(int) (x & BITMASK_W_MASK)];
	}

	/**
	 * A crude bounding rectangle overlap check. All other overlap methods have
	 * a call to this built in.
	 * 
	 * @param b
	 *            another bitmask.
	 * @param xOffset
	 *            relative position of bitmask b.
	 * @param yOffset
	 *            relative position of bitmask b.
	 * @return whether the bitmask bounding rectangles overlap.
	 */
	public boolean overlapsBoundingRectangleOf(final Bitmask b, final int xOffset, final int yOffset) {
		return !(xOffset >= w || yOffset >= h || b.h + yOffset <= 0 || b.w + xOffset <= 0);
	}

	/**
	 * Returns nonzero if the masks overlap with the given offset. The overlap
	 * tests uses the following offsets (which may be negative):
	 * 
	 * <pre>
	 * +----+----------..
	 * |this| yoffset   
	 * |  +-+----------..
	 * +--|b        
	 * |xoffset      
	 * |  |
	 * :  :
	 * </pre>
	 */
	public boolean overlaps(Bitmask b, int xoffset, int yoffset) {
		Bitmask a = this;

		if (!overlapsBoundingRectangleOf(b, xoffset, yoffset)) {
			return false;
		}

		while (true) {
			if (xoffset >= 0) {

				long a_entry;
				long a_end;
				long b_entry;

				if (yoffset >= 0) {
					a_entry = a.h * (xoffset / BITMASK_W_LEN) + yoffset;
					a_end = a_entry + Math.min(b.h, a.h - yoffset);
					b_entry = 0;
				} else {
					a_entry = a.h * (xoffset / BITMASK_W_LEN);
					a_end = a_entry + Math.min(b.h + yoffset, a.h);
					b_entry = -yoffset;
				}
				long shift;
				shift = xoffset & BITMASK_W_MASK;
				if (shift != 0) {

					final long rshift = BITMASK_W_LEN - shift;
					final long astripes = ((a.w - 1)) / BITMASK_W_LEN - xoffset / BITMASK_W_LEN;
					final long bstripes = ((b.w - 1)) / BITMASK_W_LEN + 1;

					if (bstripes > astripes) { /* zig-zag .. zig */
						for (long i = 0; i < astripes; i++) {
							for (long ap = a_entry, app = ap + a.h, bp = b_entry; ap < a_end;) {
								if (((a.bits[(int) ap++] >>> shift) & b.bits[(int) bp]) != 0
										|| ((a.bits[(int) app++] << rshift) & b.bits[(int) bp++]) != 0) {
									return true;
								}
							}
							a_entry += a.h;
							a_end += a.h;
							b_entry += b.h;
						}
						for (long ap = a_entry, bp = b_entry; ap < a_end;) {
							if (((a.bits[(int) ap++] >>> shift) & b.bits[(int) bp++]) != 0) {
								return true;
							}
						}
						return false;
					}
					// zig-zag
					for (long i = 0; i < bstripes; i++) {
						for (long ap = a_entry, app = ap + a.h, bp = b_entry; ap < a_end;) {
							if (((a.bits[(int) ap++] >>> shift) & b.bits[(int) bp]) != 0
									|| ((a.bits[(int) app++] << rshift) & b.bits[(int) bp++]) != 0) {
								return true;
							}
						}
						a_entry += a.h;
						a_end += a.h;
						b_entry += b.h;
					}
					return false;
				}
				// xoffset is a multiple of the stripe width, and the above routines wont work 
				final long astripes = (Math.min(b.w, a.w - xoffset) - 1) / BITMASK_W_LEN + 1;
				for (long i = 0; i < astripes; i++) {
					for (long ap = a_entry, bp = b_entry; ap < a_end;) {
						if ((a.bits[(int) ap++] & b.bits[(int) bp++]) != 0) {
							return true;
						}
					}
					a_entry += a.h;
					a_end += a.h;
					b_entry += b.h;
				}
				return false;
			}
			final Bitmask c = a;
			a = b;
			b = c;
			xoffset *= -1;
			yoffset *= -1;
		}
	}

	/*
	 * Like Bitmask_overlap(), but will also give a point of intersection. x and
	 * y are given in the coordinates of mask a, and are untouched if there is
	 * no overlap.
	 */
	//	public final Point2D overlapPos(final Bitmask b, final int xoffset, final int yoffset) {
	//		return null;
	//	  const long *a_entry,*a_end, *b_entry, *ap, *bp;
	//	  unsigned int shift,rshift,i,astripes,bstripes,xbase;
	//	  
	//	  if ((xoffset >= a->w) || (yoffset >= a->h) || (yoffset <= - b->h)) 
	//	      return 0;
	//	  
	//	  if (xoffset >= 0) 
	//	    {
	//	      xbase = xoffset/Bitmask_W_LEN; /* first stripe from mask a */
	//	      if (yoffset >= 0)
	//		{
	//		  a_entry = a->bits + a->h*xbase + yoffset;
	//		  a_end = a_entry + MIN(b->h,a->h - yoffset);
	//		  b_entry = b->bits;
	//		}
	//	      else
	//		{
	//		  a_entry = a->bits + a->h*xbase;
	//		  a_end = a_entry + MIN(b->h + yoffset,a->h);
	//		  b_entry = b->bits - yoffset;
	//		  yoffset = 0; /* relied on below */
	//		}
	//	      shift = xoffset & Bitmask_W_MASK;
	//	      if (shift)
	//		{
	//		  rshift = Bitmask_W_LEN - shift;
	//		  astripes = (a->w - 1)/Bitmask_W_LEN - xoffset/Bitmask_W_LEN;
	//		  bstripes = (b->w - 1)/Bitmask_W_LEN + 1;
	//		  if (bstripes > astripes) /* zig-zag .. zig*/
	//		    {
	//		      for (i=0;i<astripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			      if (*ap & (*bp << shift)) 
	//				{
	//				  *y = ap - a_entry + yoffset;
	//				  *x = (xbase + i)*Bitmask_W_LEN + firstsetbit(*ap & (*bp << shift));
	//				  return 1;
	//				}
	//			  a_entry += a->h;
	//			  a_end += a->h;
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			      if (*ap & (*bp >> rshift)) 
	//				{
	//				  *y = ap - a_entry + yoffset;
	//				  *x = (xbase + i + 1)*Bitmask_W_LEN + firstsetbit(*ap & (*bp >> rshift));
	//				  return 1;
	//				}
	//			  b_entry += b->h;
	//			}
	//		      for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			if (*ap & (*bp << shift)) 
	//			  {
	//			    *y = ap - a_entry + yoffset;
	//			    *x = (xbase + astripes)*Bitmask_W_LEN + firstsetbit(*ap & (*bp << shift));
	//			    return 1;
	//			  }
	//		      return 0;
	//		    }
	//		  else /* zig-zag */
	//		    {
	//		      for (i=0;i<bstripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			      if (*ap & (*bp << shift)) 
	//				{
	//				  *y = ap - a_entry + yoffset;
	//				  *x = (xbase + i)*Bitmask_W_LEN + firstsetbit(*ap & (*bp << shift));
	//				  return 1;
	//				}
	//			  a_entry += a->h;
	//			  a_end += a->h;
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			      if (*ap & (*bp >> rshift)) 
	//				{
	//				  *y = ap - a_entry + yoffset;
	//				  *x = (xbase + i + 1)*Bitmask_W_LEN + firstsetbit(*ap & (*bp >> rshift));
	//				  return 1;
	//				}
	//			  b_entry += b->h;
	//			}
	//		      return 0;
	//		    }
	//		}
	//	      else 
	//	/* xoffset is a multiple of the stripe width, and the above routines
	//	   won't work. This way is also slightly faster. */
	//		{
	//		  astripes = (MIN(b->w,a->w - xoffset) - 1)/Bitmask_W_LEN + 1;
	//		  for (i=0;i<astripes;i++)
	//		    {
	//		      for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			{
	//			  if (*ap & *bp)
	//			    {
	//			      *y = ap - a_entry + yoffset;
	//			      *x = (xbase + i)*Bitmask_W_LEN + firstsetbit(*ap & *bp); 
	//			      return 1;
	//			    }
	//			}
	//		      a_entry += a->h;
	//		      a_end += a->h;
	//		      b_entry += b->h;
	//		    }
	//		  return 0;
	//		}
	//	    }
	//	  else  
	//	    {
	//	      if (Bitmask_overlap_pos(b,a,-xoffset,-yoffset,x,y))
	//		{
	//		  *x += xoffset;
	//		  *y += yoffset;
	//		  return 1;
	//		}
	//	      else
	//		return 0;
	//	    }
	//	}

	/** Returns the number of overlapping 'pixels' */
	//	public final int overlapArea(final Bitmask b, final int xoffset, final int yoffset) {
	//	  const long *a_entry,*a_end, *b_entry, *ap,*bp;
	//	  unsigned int shift,rshift,i,astripes,bstripes;
	//	  unsigned int count = 0;
	//
	//	  if ((xoffset >= a->w) || (yoffset >= a->h) || (b->h + yoffset <= 0) || (b->w + xoffset <= 0)) 
	//	      return 0;
	//	  
	//	  if (xoffset >= 0) 
	//	    {
	//	    swapentry:
	//	      if (yoffset >= 0)
	//		{
	//		  a_entry = a->bits + a->h*(xoffset/Bitmask_W_LEN) + yoffset;
	//		  a_end = a_entry + MIN(b->h,a->h - yoffset);
	//		  b_entry = b->bits;
	//		}
	//	      else
	//		{
	//		  a_entry = a->bits + a->h*(xoffset/Bitmask_W_LEN);
	//		  a_end = a_entry + MIN(b->h + yoffset,a->h);
	//		  b_entry = b->bits - yoffset;
	//		}
	//	      shift = xoffset & Bitmask_W_MASK;
	//	      if (shift)
	//		{
	//		  rshift = Bitmask_W_LEN - shift;
	//		  astripes = (a->w - 1)/Bitmask_W_LEN - xoffset/Bitmask_W_LEN;
	//		  bstripes = (b->w - 1)/Bitmask_W_LEN + 1;
	//		  if (bstripes > astripes) /* zig-zag .. zig*/
	//		    {
	//		      for (i=0;i<astripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			    count += bitcount(((*ap >> shift) | (*(ap + a->h) << rshift)) & *bp);
	//			  a_entry += a->h;
	//			  a_end += a->h;
	//			  b_entry += b->h;
	//			}
	//		      for (ap = a_entry,bp = b_entry;ap < a_end;)
	//			count += bitcount((*ap++ >> shift) & *bp++);
	//		      return count;
	//		    }
	//		  else /* zig-zag */
	//		    {
	//		      for (i=0;i<bstripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry;ap < a_end;ap++,bp++)
	//			    count += bitcount(((*ap >> shift) | (*(ap + a->h) << rshift)) & *bp);
	//			  a_entry += a->h;
	//			  a_end += a->h;
	//			  b_entry += b->h;
	//			}
	//		      return count;
	//		    }
	//		}
	//	      else /* xoffset is a multiple of the stripe width, and the above routines wont work */
	//		{
	//		  astripes = (MIN(b->w,a->w - xoffset) - 1)/Bitmask_W_LEN + 1;
	//		  for (i=0;i<astripes;i++)
	//		    {
	//		      for (ap = a_entry,bp = b_entry;ap < a_end;)
	//			count += bitcount(*ap++ & *bp++);
	//
	//		      a_entry += a->h;
	//		      a_end += a->h;
	//		      b_entry += b->h;
	//		    }
	//		  return count;
	//		}
	//	    }
	//	  else  
	//	    {
	//	      const Bitmask *c = a;
	//	      a = b;
	//	      b = c;
	//	      xoffset *= -1;
	//	      yoffset *= -1;
	//	      goto swapentry;
	//	    }
	//	}

	/** Fills a mask with the overlap of two other masks. A bitwise AND. */
	//	public final void overlapMask(final Bitmask b, final Bitmask c, final int xoffset, final int yoffset) {
	//	  const long *a_entry,*a_end, *ap;
	//	  const long *b_entry, *b_end, *bp;
	//	  long *c_entry, *c_end, *cp;
	//	  int shift,rshift,i,astripes,bstripes;
	//	  
	//	  if ((xoffset >= a->w) || (yoffset >= a->h) || (yoffset <= - b->h)) 
	//	      return;
	//	  
	//	  if (xoffset >= 0) 
	//	    {
	//	      if (yoffset >= 0)
	//		{
	//		  a_entry = a->bits + a->h*(xoffset/Bitmask_W_LEN) + yoffset;
	//		  c_entry = c->bits + c->h*(xoffset/Bitmask_W_LEN) + yoffset;
	//		  a_end = a_entry + MIN(b->h,a->h - yoffset);
	//		  b_entry = b->bits;
	//		}
	//	      else
	//		{
	//		  a_entry = a->bits + a->h*(xoffset/Bitmask_W_LEN);
	//		  c_entry = c->bits + c->h*(xoffset/Bitmask_W_LEN);
	//		  a_end = a_entry + MIN(b->h + yoffset,a->h);
	//		  b_entry = b->bits - yoffset;
	//		}
	//	      shift = xoffset & Bitmask_W_MASK;
	//	      if (shift)
	//		{
	//		  rshift = Bitmask_W_LEN - shift;
	//		  astripes = (a->w - 1)/Bitmask_W_LEN - xoffset/Bitmask_W_LEN;
	//		  bstripes = (b->w - 1)/Bitmask_W_LEN + 1;
	//		  if (bstripes > astripes) /* zig-zag .. zig*/
	//		    {
	//		      for (i=0;i<astripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			    *cp = *ap & (*bp << shift);
	//			  a_entry += a->h;
	//			  c_entry += c->h;
	//			  a_end += a->h;
	//			  for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			    *cp = *ap & (*bp >> rshift);
	//			  b_entry += b->h;
	//			}
	//		      for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			*cp = *ap & (*bp << shift);
	//		    }
	//		  else /* zig-zag */
	//		    {
	//		      for (i=0;i<bstripes;i++)
	//			{
	//			  for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			    *cp = *ap & (*bp << shift);
	//			  a_entry += a->h;
	//			  c_entry += c->h;
	//			  a_end += a->h;
	//			  for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			    *cp = *ap & (*bp >> rshift);
	//			  b_entry += b->h;
	//			}
	//		    }
	//		}
	//	      else /* xoffset is a multiple of the stripe width, 
	//		      and the above routines won't work. */
	//		{
	//		  astripes = (MIN(b->w,a->w - xoffset) - 1)/Bitmask_W_LEN + 1;
	//		  for (i=0;i<astripes;i++)
	//		    {
	//		      for (ap = a_entry,bp = b_entry,cp = c_entry;ap < a_end;ap++,bp++,cp++)
	//			{
	//			  *cp = *ap & *bp;
	//			}
	//		      a_entry += a->h;
	//		      c_entry += c->h;
	//		      a_end += a->h;
	//		      b_entry += b->h;
	//		    }
	//		}
	//	    }
	//	  else  
	//	    {
	//	      xoffset *= -1;
	//	      yoffset *= -1;
	//
	//	      if (yoffset >= 0)
	//		{
	//		  b_entry = b->bits + b->h*(xoffset/Bitmask_W_LEN) + yoffset;
	//		  b_end = b_entry + MIN(a->h,b->h - yoffset);
	//		  a_entry = a->bits;
	//		  c_entry = c->bits;
	//		}
	//	      else
	//		{
	//		  b_entry = b->bits + b->h*(xoffset/Bitmask_W_LEN);
	//		  b_end = b_entry + MIN(a->h + yoffset,b->h);
	//		  a_entry = a->bits - yoffset;
	//		  c_entry = c->bits - yoffset;
	//		}
	//	      shift = xoffset & Bitmask_W_MASK;
	//	      if (shift)
	//		{
	//		  rshift = Bitmask_W_LEN - shift;
	//		  astripes = (b->w - 1)/Bitmask_W_LEN - xoffset/Bitmask_W_LEN;
	//		  bstripes = (a->w - 1)/Bitmask_W_LEN + 1;
	//		  if (bstripes > astripes) /* zig-zag .. zig*/
	//		    {
	//		      for (i=0;i<astripes;i++)
	//			{
	//			  for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			    *cp = *ap & (*bp >> shift);
	//			  b_entry += b->h;
	//			  b_end += b->h;
	//			  for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			    *cp = *ap & (*bp <<rshift); 
	//			  a_entry += a->h;
	//			  c_entry += c->h;
	//			}
	//		      for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			*cp = *ap & (*bp >> shift);
	//		    }
	//		  else /* zig-zag */
	//		    {
	//		      for (i=0;i<bstripes;i++)
	//			{
	//			  for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			    *cp = *ap & (*bp >> shift);
	//			  b_entry += b->h;
	//			  b_end += b->h;
	//			  for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			    *cp = *ap & (*bp << rshift);
	//			  a_entry += a->h;
	//			  c_entry += c->h;
	//			}
	//		    }
	//		}
	//	      else /* xoffset is a multiple of the stripe width, and the above routines won't work. */
	//		{
	//		  astripes = (MIN(a->w,b->w - xoffset) - 1)/Bitmask_W_LEN + 1;
	//		  for (i=0;i<astripes;i++)
	//		    {
	//		      for (bp = b_entry,ap = a_entry,cp = c_entry;bp < b_end;bp++,ap++,cp++)
	//			{
	//			  *cp = *ap & *bp;
	//			}
	//		      b_entry += b->h;
	//		      b_end += b->h;
	//		      a_entry += a->h;
	//		      c_entry += c->h;
	//		    }
	//		}
	//	      xoffset *= -1;
	//	      yoffset *= -1;
	//	    }	
	//	  /* Zero out bits outside the mask rectangle (to the right), if there
	//	   is a chance we were drawing there. */
	//	  if (xoffset + b->w > c->w)
	//	    {
	//	      long edgemask;
	//	      int n = c->w/Bitmask_W_LEN;
	//	      shift = (n + 1)*Bitmask_W_LEN - c->w;
	//	      edgemask = (~(long)0) >> shift;
	//	      c_end = c->bits + n*c->h + MIN(c->h,b->h + yoffset);
	//	      for (cp = c->bits + n*c->h + MAX(yoffset,0);cp<c_end;cp++)
	//		*cp &= edgemask;
	//	    }
	//	}

	/**
	 * @return a copy of this bitmask. It uses the copy constructor.
	 */
	public Object clone() {
		return new Bitmask(this);
	}

	/**
	 * @return an ASCII art representation of the content of this Bitmask.
	 */
	public String toString() {
		final StringBuilder dump = new StringBuilder();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				dump.append(getBit(x, y) ? 'o' : '.');
			}
			dump.append("\n");
		}
		return dump.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(final Object obj) {
		if (!(obj instanceof Bitmask)) {
			return false;
		}
		final Bitmask otherBitmask = (Bitmask) obj;
		if (w != otherBitmask.w || h != otherBitmask.h) {
			return false;
		}
		for (int i = 0; i < bits.length; i++) {
			if (bits[i] != otherBitmask.bits[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return w << 256 + 17 * h;
	}
}
