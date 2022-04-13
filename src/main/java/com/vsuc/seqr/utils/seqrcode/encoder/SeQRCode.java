package com.vsuc.seqr.utils.seqrcode.encoder;

import com.vsuc.seqr.utils.seqrcode.decoder.ErrorCorrectionLevel;
import com.vsuc.seqr.utils.seqrcode.decoder.Mode;

public final class SeQRCode {

    public static final int NUM_MASK_PATTERNS = 8;

    private static Mode mode;
    private static ErrorCorrectionLevel ecLevel;
//    private Version version;
    private int maskPattern;
    private ByteMatrix matrix;

    public SeQRCode() {
        maskPattern = -1;
    }

    public Mode getMode() {
        return mode;
    }

    public ErrorCorrectionLevel getECLevel() {
        return ecLevel;
    }

    public int getMaskPattern() {
        return maskPattern;
    }

    public ByteMatrix getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(200);
        result.append("<<\n");
        result.append(" mode: ");
        result.append(mode);
        result.append("\n ecLevel: ");
        result.append(ecLevel);
        result.append("\n maskPattern: ");
        result.append(maskPattern);
        if (matrix == null) {
            result.append("\n matrix: null\n");
        } else {
            result.append("\n matrix:\n");
            result.append(matrix);
        }
        result.append(">>\n");
        return result.toString();
    }

    public static void setMode(Mode value) {
        mode = value;
    }

    public static void setECLevel(ErrorCorrectionLevel value) {
        ecLevel = value;
    }

    public void setMaskPattern(int value) {
        maskPattern = value;
    }

    public void setMatrix(ByteMatrix value) {
        matrix = value;
    }

    public static boolean isValidMaskPattern(int maskPattern) {
        return maskPattern >= 0 && maskPattern < NUM_MASK_PATTERNS;
    }

}
