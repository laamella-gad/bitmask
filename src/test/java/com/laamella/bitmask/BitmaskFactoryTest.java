package com.laamella.bitmask;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.laamella.bitmask.Tools.readStringResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BitmaskFactoryTest {

    @Test
    void testAlpha05() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 0.5);
        assertEquals(readStringResource("/circle_with_alpha_at_0.5.txt"), bitmask.toString());
    }

    @Test
    void testAlpha0() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 0);
        assertEquals(readStringResource("/circle_with_alpha_at_0.txt"), bitmask.toString());
    }

    @Test
    void testAlpha1() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/circle_with_alpha.png"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromAlphaChannel(image, 1);
        assertEquals(readStringResource("/circle_with_alpha_at_1.txt"), bitmask.toString());
    }

    @Test
    void testNoAlphaBreaks() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
        try {
            BitmaskFactory.createBitmaskFromAlphaChannel(image, 1);
        } catch (final IllegalArgumentException exception) {
            return;
        }
        fail("");
    }

    @Test
    void testColorKeyWhite() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, Color.WHITE);
        assertEquals(readStringResource("/color_keyed_white.txt"), bitmask.toString());
    }

    @Test
    void testColorKeyRed() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, new Color(232, 20, 20));
        assertEquals(readStringResource("/color_keyed_red.txt"), bitmask.toString());
    }

    @Test
    void testColorKeyBlack() throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/color_keyed.gif"));
        final Bitmask bitmask = BitmaskFactory.createBitmaskFromColorKey(image, Color.BLACK);
        assertEquals(readStringResource("/color_keyed_black.txt"), bitmask.toString());
    }
}
