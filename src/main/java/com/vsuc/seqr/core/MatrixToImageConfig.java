package com.vsuc.seqr.core;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MatrixToImageConfig {

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;

    private final int onColor;
    private final int offColor;

    /**
     * Creates a default config with on color {@link #BLACK} and off color {@link #WHITE}, generating normal
     * black-on-white barcodes.
     */
    public MatrixToImageConfig() {
        this(BLACK, WHITE);
    }

    /**
     * @param onColor pixel on color, specified as an ARGB value as an int
     * @param offColor pixel off color, specified as an ARGB value as an int
     */
    public MatrixToImageConfig(int onColor, int offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
    }

    public int getPixelOnColor() {
        return onColor;
    }

    public int getPixelOffColor() {
        return offColor;
    }

    int getBufferedImageColorModel() {
//        if (onColor == BLACK && offColor == WHITE) {
//            // Use faster BINARY if colors match default
//            return BufferedImage.TYPE_BYTE_BINARY;
//        }
        if (hasTransparency(onColor) || hasTransparency(offColor)) {
            // Use ARGB representation if colors specify non-opaque alpha
            return BufferedImage.TYPE_INT_ARGB;
        }
        // Default otherwise to RGB representation with ignored alpha channel
        return BufferedImage.TYPE_INT_RGB;
    }

    public int getColorInt(boolean redV, boolean greenV, boolean blueV) {
        int red = getColorValue(redV);
        int green = getColorValue(greenV);
        int blue = getColorValue(blueV);
        if(red == 0 && green == 0 && blue == 255)
            red = green = 40;
        else if(red == 255 && green == 0 && blue == 255)
            green = 40;
        return new Color(red, green, blue).getRGB();
    }

    private int getColorValue(boolean rgb) {
        if(rgb) return 0;
        return 255;
    }

    private static boolean hasTransparency(int argb) {
        return (argb & 0xFF000000) != 0xFF000000;
    }

}
