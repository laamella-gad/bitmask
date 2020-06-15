package com.laamella.bitmask;

import org.junit.jupiter.api.Test;

import static com.laamella.bitmask.Tools.*;
import static org.junit.jupiter.api.Assertions.*;

public class BitmaskTest {
    private final Bitmask tenByTenBitmask = new Bitmask(10, 10);

    @Test
    void testDataSize1() {
        assertEquals(10, getBits(tenByTenBitmask).length);
    }

    @Test
    void testDataSize2() {
        assertEquals(3, getBits(new Bitmask(64, 3)).length);
    }

    @Test
    void testDataSize3() {
        assertEquals(6, getBits(new Bitmask(65, 3)).length);
    }

    @Test
    void testDataSize4() {
        assertEquals(6, getBits(new Bitmask(128, 3)).length);
    }

    @Test
    void testEmptyAfterConstruction() {
        assertEquals(0, tenByTenBitmask.countBits());
    }

    @Test
    void testGetSetBit1() {
        assertFalse(tenByTenBitmask.getBit(3, 6));
        tenByTenBitmask.setBit(3, 6);
        assertTrue(tenByTenBitmask.getBit(3, 6));
    }

    @Test
    void testGetSetBit2() {
        final Bitmask bitMask = new Bitmask(64, 2);
        assertFalse(bitMask.getBit(63, 0));
        bitMask.setBit(63, 0);
        assertTrue(bitMask.getBit(63, 0));
    }

    @Test
    void testGetSetBit3() {
        assertFalse(tenByTenBitmask.getBit(0, 1));
        tenByTenBitmask.setBit(0, 1);
        assertTrue(tenByTenBitmask.getBit(0, 1));
    }

    @Test
    void testSetClearBit1() {
        tenByTenBitmask.setBit(2, 1);
        assertTrue(tenByTenBitmask.getBit(2, 1));
        tenByTenBitmask.clearBit(2, 1);
        assertFalse(tenByTenBitmask.getBit(2, 1));
    }

    @Test
    void testFill() {
        final Bitmask bitMask = new Bitmask(200, 3);
        bitMask.fill();
        assertEquals(600, bitMask.countBits());
    }

    @Test
    void testInvert1() {
        final Bitmask bitMask = new Bitmask(200, 3);
        bitMask.invert();
        assertEquals(600, bitMask.countBits());
        bitMask.invert();
        assertEquals(0, bitMask.countBits());
    }

    @Test
    void testInvert2() {
        final Bitmask bitMask = new Bitmask(200, 3);
        bitMask.setBit(0, 2);
        bitMask.setBit(199, 0);
        bitMask.invert();
        assertEquals(598, bitMask.countBits());
        bitMask.invert();
        assertEquals(2, bitMask.countBits());
    }

    @Test
    void testOverlap1() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertTrue(bitMask1.overlaps(bitMask2, 0, 0));
    }

    @Test
    void testOverlap2() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertFalse(bitMask1.overlaps(bitMask2, 1, 0));
    }

    @Test
    void testOverlap3() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertFalse(bitMask1.overlaps(bitMask2, -1, 0));
    }

    @Test
    void testOverlap4() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertFalse(bitMask1.overlaps(bitMask2, 0, 1));
    }

    @Test
    void testOverlap5() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertFalse(bitMask1.overlaps(bitMask2, 0, -1));
    }

    @Test
    void testOverlap6() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        bitMask1.setBit(0, 0);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        assertFalse(bitMask1.overlaps(bitMask2, 0, 0));
    }

    @Test
    void testOverlap7() {
        final Bitmask bitMask1 = new Bitmask(1, 1);
        final Bitmask bitMask2 = new Bitmask(1, 1);
        bitMask2.setBit(0, 0);
        assertFalse(bitMask1.overlaps(bitMask2, 0, 0));
    }

    @Test
    void testOverlap8() {
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

    @Test
    void testOverlap9() {
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

    @Test
    void testOverlap10() {
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

    @Test
    void testOverlap11() {
        final Bitmask bitMask1 = makeOnOffPatternBitmask(1000, 10);
        final Bitmask bitMask2 = makeOnOffPatternBitmask(1000, 10);
        bitMask2.invert();
        assertFalse(bitMask2.overlaps(bitMask1, 0, 0));
    }

    @Test
    void testOverlap12() {
        final Bitmask bitMask1 = makeOnOffPatternBitmask(1000, 10);
        final Bitmask bitMask2 = makeOnOffPatternBitmask(1000, 10);
        bitMask2.invert();
        assertFalse(bitMask2.overlaps(bitMask1, 1, 1));
    }

    @Test
    void testOverlap13() {
        final Bitmask bitMask1 = makeOnOffPatternBitmask(1000, 10);
        final Bitmask bitMask2 = makeOnOffPatternBitmask(1000, 10);
        bitMask2.invert();
        assertFalse(bitMask2.overlaps(bitMask1, 1, -1));
    }

    @Test
    void testOverlap14() {
        final Bitmask bitMask1 = makeOnOffPatternBitmask(1000, 10);
        final Bitmask bitMask2 = makeOnOffPatternBitmask(1000, 10);
        bitMask2.invert();
        assertTrue(bitMask2.overlaps(bitMask1, 1, 0));
    }

    @Test
    void sillyBenchmark() {
        final Bitmask bitMask1 = makeOnOffPatternBitmask(100, 100);
        final Bitmask bitMask2 = makeOnOffPatternBitmask(100, 100);
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
