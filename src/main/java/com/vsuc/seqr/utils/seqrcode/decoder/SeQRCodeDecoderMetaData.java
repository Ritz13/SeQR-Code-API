package com.vsuc.seqr.utils.seqrcode.decoder;

import com.vsuc.seqr.utils.ResultPoint;

public final class SeQRCodeDecoderMetaData {

    private final boolean mirrored;

    SeQRCodeDecoderMetaData(boolean mirrored) {
        this.mirrored = mirrored;
    }

    /**
     * @return true if the QR Code was mirrored.
     */
    public boolean isMirrored() {
        return mirrored;
    }

    /**
     * Apply the result points' order correction due to mirroring.
     *
     * @param points Array of points to apply mirror correction to.
     */
    public void applyMirroredCorrection(ResultPoint[] points) {
        if (!mirrored || points == null || points.length < 3) {
            return;
        }
        ResultPoint bottomLeft = points[0];
        points[0] = points[2];
        points[2] = bottomLeft;
        // No need to 'fix' top-left and alignment pattern.
    }

}
