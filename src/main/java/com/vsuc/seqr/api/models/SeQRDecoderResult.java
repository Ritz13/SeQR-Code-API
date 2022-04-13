package com.vsuc.seqr.api.models;

import java.awt.image.BufferedImage;

public class SeQRDecoderResult {

    private final String text;
    private final BufferedImage image;

    public SeQRDecoderResult(String text, BufferedImage image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public BufferedImage getImage() {
        return image;
    }

}
