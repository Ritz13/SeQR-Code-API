package com.vsuc.seqr.core;

import com.vsuc.seqr.utils.classes.BitArray;
import com.vsuc.seqr.utils.classes.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public final class MatrixToImageWriter {

    private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

    private MatrixToImageWriter() {}

    /**
     * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
     * as white, and "true" bits are rendered as black. Uses default configuration.
     *
     * @param matrices {@link BitMatrix} to write
     * @return {@link BufferedImage} representation of the input
     */
    public static BufferedImage toBufferedImage(BitMatrix[] matrices) {
        return toBufferedImage(matrices, DEFAULT_CONFIG);
    }

    /**
     * As {@link #toBufferedImage(BitMatrix[])}, but allows customization of the output.
     *
     * @param matrices {@link BitMatrix} to write
     * @param config output configuration
     * @return {@link BufferedImage} representation of the input
     */
    public static BufferedImage toBufferedImage(BitMatrix[] matrices, MatrixToImageConfig config) {
        int width = matrices[0].getWidth();
        int height = matrices[0].getHeight();
        BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
        int[] rowPixels = new int[width];
        BitArray rowR = new BitArray(width);
        BitArray rowG = new BitArray(width);
        BitArray rowB = new BitArray(width);
        for (int y = 0; y < height; y++) {
            rowR = matrices[0].getRow(y, rowR);
            rowG = matrices[1].getRow(y, rowG);
            rowB = matrices[2].getRow(y, rowB);
            for (int x = 0; x < width; x++) {
                rowPixels[x] = config.getColorInt(rowR.get(x), rowG.get(x), rowB.get(x));
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }
        return image;
    }

    /**
     * Writes a {@link BitMatrix} to a file with default configuration.
     *
     * @param matrices {@link BitMatrix} to write
     * @param format image format
     * @param file file {@link Path} to write image to
     * @throws IOException if writes to the stream fail
     * @see #toBufferedImage(BitMatrix[])
     */
    public static BufferedImage writeToPath(BitMatrix[] matrices, String format, Path file) throws IOException {
        return writeToPath(matrices, format, file, DEFAULT_CONFIG);
    }

    /**
     * As {@link #writeToPath(BitMatrix[], String, Path)}, but allows customization of the output.
     *
     * @param matrices {@link BitMatrix} to write
     * @param format image format
     * @param file file {@link Path} to write image to
     * @param config output configuration
     * @throws IOException if writes to the file fail
     */
    public static BufferedImage writeToPath(BitMatrix[] matrices, String format, Path file, MatrixToImageConfig config)
            throws IOException {
        return toBufferedImage(matrices, config);
//        if (!ImageIO.write(image, format, file.toFile())) {
//            throw new IOException("Could not write an image of format " + format + " to " + file);
//        }
    }
}
