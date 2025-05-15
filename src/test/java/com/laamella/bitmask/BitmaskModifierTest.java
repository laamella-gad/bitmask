package com.laamella.bitmask;

import org.junit.jupiter.api.Test;

import static com.laamella.bitmask.Tools.readStringResource;
import static org.junit.jupiter.api.Assertions.*;

public class BitmaskModifierTest {
    @Test
    void testScaleDown() {
        final String pattern = readStringResource("/test_pattern.txt");
        final String scaledPattern = readStringResource("/scaled_down_test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask actual = BitmaskModifier.scale(bitmask, 10, 10);
        assertEquals(scaledPattern, actual.toString());
    }

    @Test
    void testScaleEqual() {
        final String pattern = readStringResource("/test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask actual = BitmaskModifier.scale(bitmask, bitmask.getWidth(), bitmask.getHeight());
        assertEquals(pattern + "\n", actual.toString());
    }

    @Test
    void testScaleUp() {
        final String pattern = readStringResource("/test_pattern.txt");
        final String scaledPattern = readStringResource("/scaled_up_test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask actual = BitmaskModifier.scale(bitmask, 100, 63);
        assertEquals(scaledPattern, actual.toString());
    }

    @Test
    void testScaleTo0() {
        final String pattern = readStringResource("/test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask actual = BitmaskModifier.scale(bitmask, 0, 0);
        assertEquals(1, actual.getWidth());
        assertEquals(1, actual.getHeight());
        assertFalse(actual.getBit(0, 0));
    }

    @Test
    void testCopyConstructorCreatesEqualBitmasks() {
        final String pattern = readStringResource("/test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask bitmask2 = new Bitmask(bitmask);
        assertEquals(bitmask.toString(), bitmask2.toString());
    }

    @Test
    void testCopyConstructorDoesNotShareData() {
        final String pattern = readStringResource("/test_pattern.txt");
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAsciiArt(pattern, 'o');
        final Bitmask bitmask2 = new Bitmask(bitmask);
        bitmask.setBit(0, 0);
        assertNotEquals(bitmask.toString(), bitmask2.toString());
    }

    @Test
    void testDrawAt0_0() {
        final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
        final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
        final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
        BitmaskModifier.draw(bitmask1, bitmask2, 0, 0);
        assertEquals(expected, bitmask1);
    }

    @Test
    void testDrawLargeBitmaskOnTop() {
        final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
        final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
        final Bitmask scaled = BitmaskModifier.scale(bitmask2, bitmask1.getWidth() + 4, bitmask1.getHeight() + 4);
        final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
        BitmaskModifier.draw(bitmask1, scaled, -2, -2);
        System.out.println(bitmask1);
//        assertEquals(expected, bitmask1);
    }

    @Test
    void testEraseLargeBitmaskOnTop() {
        final Bitmask bitmask1 = Tools.createBitmaskFromResource("/test_pattern.txt");
        final Bitmask bitmask2 = Tools.makeOnOffPatternBitmask(10, 10);
        final Bitmask scaled = BitmaskModifier.scale(bitmask2, bitmask1.getWidth() + 4, bitmask1.getHeight() + 4);
        final Bitmask expected = Tools.createBitmaskFromResource("/draw_at_0_0.txt");
        BitmaskModifier.erase(bitmask1, scaled, -2, -2);
        System.out.println(bitmask1);
//        assertEquals(expected, bitmask1);
    }
}
