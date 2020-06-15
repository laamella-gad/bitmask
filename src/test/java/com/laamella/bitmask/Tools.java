package com.laamella.bitmask;

import org.apache.commons.io.FileUtils;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static java.nio.charset.StandardCharsets.UTF_8;

class Tools {
    static String readStringResource(final String resourceName) {
        try {
            return FileUtils.readFileToString(new File(resourceName.getClass().getResource(resourceName).getFile()), UTF_8);
        } catch (final IOException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    static String dumpLongs(final Bitmask bitmask) {
        final StringBuilder dump = new StringBuilder();
        final long[] bits = getBits(bitmask);
        for (int i = 0; i < bits.length; i++) {
            dump.append(Long.toBinaryString(bits[i])).append("\n");
        }
        return dump.toString();
    }

    static Bitmask makeOnOffPatternBitmask(final int width, final int height) {
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

    static Bitmask createBitmaskFromResource(final String resourceName) {
        final String string = readStringResource(resourceName);
        return BitmaskFactory.createBitmaskFromAsciiArt(string, 'o');
    }

    static long[] getBits(final Bitmask bitmask) {
        try {
            Field bitsField = Bitmask.class.getDeclaredField("bits");
            bitsField.setAccessible(true);
            return (long[]) bitsField.get(bitmask);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionFailedError("For some reason, the bits field is inaccessible");
        }
    }

}
