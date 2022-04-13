package com.vsuc.seqr.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TriSeparator {

    private final BufferedImage image;
    private final String text;

    public TriSeparator(BufferedImage imageToDivide) {
        this.image = imageToDivide;
        this.text = null;
    }
    public TriSeparator(String textToDivide) {
        this.image = null;
        this.text = textToDivide;
    }

    public BufferedImage[] separateImage() {
        return this.separateImage(this.image);
    }

    private BufferedImage[] separateImage(BufferedImage source) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        BufferedImage[] separatedImages = new BufferedImage[3];

        BufferedImage imageR = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage imageG = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage imageB = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
        int red,green,blue;
        for(int x = 0; x < sourceHeight; x++) {
            for(int y = 0; y < sourceWidth; y++) {
                int pixelRGB = source.getRGB(x, y);
                red = ((pixelRGB >> 16) & 0xff) > 128 ? 255 : 0;
                green = ((pixelRGB >> 8) & 0xff) > 128 ? 255 : 0;
                blue = ((pixelRGB) & 0xff) > 128 ? 255 : 0;
                imageR.setRGB(x, y, new Color(red, red, red).getRGB());
                imageG.setRGB(x, y, new Color(green, green, green).getRGB());
                imageB.setRGB(x, y, new Color(blue, blue, blue).getRGB());
            }
        }
        separatedImages[0]=imageR;
        separatedImages[1]=imageG;
        separatedImages[2]=imageB;

        return separatedImages;
    }

    public String[] separateText() {
        return this.separateText(this.text);
    }

    private String[] separateText(String source) {
        if (source.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }

        String[] separatedTexts = new String[3];
        int textLength = source.length();
        int spacesToAdd = 3-textLength%3;
        if (spacesToAdd == 3) spacesToAdd=0;

        StringBuilder sourceBuilder = new StringBuilder(source);
        for(int space = 0; space<spacesToAdd; space++) {
            sourceBuilder.append(" ");
        }
        source = sourceBuilder.toString();
        textLength = source.length();

        StringBuilder textR = new StringBuilder();
        StringBuilder textG = new StringBuilder();
        StringBuilder textB = new StringBuilder();
        for(int i = 0; i < textLength; i++)
        {
            switch (i%3) {
                case 0:
                    textR.append(source.charAt(textLength-1-i));
                    break;

                case 1:
                    textG.append(source.charAt(textLength-1-i));
                    break;

                case 2:
                    textB.append(source.charAt(textLength-1-i));

            }
        }
        separatedTexts[0] = textR.toString();
        separatedTexts[1] = textG.toString();
        separatedTexts[2] = textB.toString();
        return separatedTexts;
    }

}
