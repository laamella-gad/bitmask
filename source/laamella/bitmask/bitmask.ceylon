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
import java.awt {Dimension}
import java.awt.geom {Dimension2D, Point2D}
import java.io {Serializable}
import java.util {Arrays}
import java.lang {
	Long
}

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
/**
 * Creates a Bitmask of width w and height h, where w and h must both be
 * greater than 0. The mask is automatically cleared when created.
 */
shared class Bitmask(Integer w, Integer h) satisfies Serializable {
	Integer \iBITMASK_W_LEN = 64;
	Integer \iBITMASK_W_MASK = \iBITMASK_W_LEN - 1;
	{Integer+} \iBITMASK_N = { #1, #2, #4, #8, #10, #20, #40, #80, #100, #200,
		#400, #800, #1000, #2000, #4000, #8000, #10000, #20000, #40000, #80000, #100000,
		#200000, #400000, #800000, #1000000, #2000000, #4000000, #8000000, #10000000, #20000000,
		#40000000, #80000000, #100000000, #200000000, #400000000, #800000000, #1000000000,
		#2000000000, #4000000000, #8000000000, #10000000000, #20000000000, #40000000000,
		#80000000000, #100000000000, #200000000000, #400000000000, #800000000000, #1000000000000,
		#2000000000000, #4000000000000, #8000000000000, #10000000000000, #20000000000000,
		#40000000000000, #80000000000000, #100000000000000, #200000000000000, #400000000000000,
		#800000000000000, #1000000000000000, #2000000000000000, #4000000000000000, #8000000000000000 };
		Integer \iALL_BITS_SET = 0.not;
		
		// Storage is column by column.
		Array<Integer> bits = arrayOfSize(h * ((w - 1) / \iBITMASK_W_LEN + 1), 0);
		//clear();
		
		//public Bitmask(final Bitmask bitmask) {
		//	this.w = bitmask.w;
		//	this.h = bitmask.h;
		//	this.bits = Arrays.copyOf(bitmask.bits, bitmask.bits.length);
		//}
		
		shared Dimension2D getSize() {
			return Dimension(w, h);
		}
		
		shared Integer getWidth() {
			return w;
		}
		
		shared Integer getHeight() {
			return h;
		}
		
		//"Clears all bits in the mask"
		//shared void clear() {
		//	Arrays.fill(bits, 0);
		//}
		
		Integer sizeInLongs(Integer w, Integer h) {
			return h * (w / \iBITMASK_W_LEN);
		}
		
		/** Sets all bits in the mask */
		shared void fill() {
			Integer len = sizeInLongs(w - 1, h);
			Integer shift = \iBITMASK_W_LEN - (w % \iBITMASK_W_LEN);
			Integer cmask = \iALL_BITS_SET.rightLogicalShift(shift);
			
			for (pixel in 0.. len) {
				bits.set(pixel, \iALL_BITS_SET);
			}
			for (pixel in len..len + h) {
				bits.set(pixel, cmask);
			}
		}
		
		"Flips all bits in the mask"
		shared void invert() {
			Integer len = sizeInLongs(w - 1, h);
			Integer shift = \iBITMASK_W_LEN - (w % \iBITMASK_W_LEN);
			Integer cmask = ALL_BITS_SET >>> shift;
			
			for (int pixel = 0; pixel < len; pixel++) {
				bits[pixel] = ~bits[pixel];
			}
			for (int pixel = (int) len; pixel < len + h; pixel++) {
				bits[pixel] = cmask & ~bits[pixel];
			}
		}
		
		/** Counts the bits in the mask */
		shared Integer countBits() {
			Integer total = 0;
			for (long pixel = 0; pixel < (h * ((w - 1) / BITMASK_W_LEN + 1)); pixel++) {
				total += Long.bitCount(bits[(int) pixel]);
				
			}
			return total;
		}
		
		"true if the bit at (x,y) is set. Coordinates start at (0,0)"
		shared Boolean getBit(Integer x, Integer y) {
			return (bits[(int) (x / BITMASK_W_LEN * h + y)] & BITMASK_N[(int) (x & BITMASK_W_MASK)]) != 0;
		}
		
		"see Bitmask#getBit(int, int)"
		share Boolean getBit(Point2D point) {
			return getBit((int) point.getX(), (int) point.getY());
		}
		
		/**
		 * Sets the bit at (x,y)
		 */
		shared void setBit(Integer x, Integer y) {
			bits[(int) (x / BITMASK_W_LEN * h + y)] |= BITMASK_N[(int) (x & BITMASK_W_MASK)];
		}
		
		/**
		 * @see Bitmask#setBit(int, int)
		 */
		shared void setBit(Point2D point) {
			setBit((int) point.getX(), (int) point.getY());
		}
		
		/**
		 * Clears the bit at (x,y)
		 */
		shared void clearBit(Integer x, Integer y) {
			bits[(int) (x / BITMASK_W_LEN * h + y)] &= ~BITMASK_N[(int) (x & BITMASK_W_MASK)];
		}
		
		/**
		 * @see Bitmask#clearBit(int, int)
		 */
		shared void clearBit(Point2D point) {
			clearBit((int) point.getX(), (int) point.getY());
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
		shared Boolean overlapsBoundingRectangleOf(Bitmask b, Integer xOffset, Integer yOffset) {
			return !(xOffset >= w || yOffset >= h || b.h + yOffset <= 0 || b.w + xOffset <= 0);
		}
		
		shared Boolean overlapsBoundingRectangleOf(Bitmask b, Point2D offset) {
			return overlapsBoundingRectangleOf(b, (int) offset.getX(), (int) offset.getY());
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
		shared Boolean overlaps(Bitmask b, Integer xoffset, Integer yoffset) {
			Bitmask a = this;
			
			if (!overlapsBoundingRectangleOf(b, xoffset, yoffset)) {
				return false;
			}
			
			while (true) {
				if (xoffset >= 0) {
					
					Integer a_entry;
					Integer a_end;
					Integer b_entry;
					
					if (yoffset >= 0) {
						a_entry = a.h * (xoffset / BITMASK_W_LEN) + yoffset;
						a_end = a_entry + Math.min(b.h, a.h - yoffset);
						b_entry = 0;
					} else {
						a_entry = a.h * (xoffset / BITMASK_W_LEN);
						a_end = a_entry + Math.min(b.h + yoffset, a.h);
						b_entry = -yoffset;
					}
					Integer shift;
					shift = xoffset & BITMASK_W_MASK;
					if (shift != 0) {
						
						Integer rshift = BITMASK_W_LEN - shift;
						Integer astripes = ((a.w - 1)) / BITMASK_W_LEN - xoffset / BITMASK_W_LEN;
						Integer bstripes = ((b.w - 1)) / BITMASK_W_LEN + 1;
						
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
		
		/**
		 * @see Bitmask#overlaps(Bitmask, int, int)
		 */
		shared Boolean overlaps(final Bitmask b, final Point2D offset) {
			return overlaps(b, (int) offset.getX(), (int) offset.getY());
		}
		
		/**
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
			final StringBuffer dump = new StringBuffer();
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
