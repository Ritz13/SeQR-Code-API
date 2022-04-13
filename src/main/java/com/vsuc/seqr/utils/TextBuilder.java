package com.vsuc.seqr.utils;

public class TextBuilder {

    public static String merge(String[] texts) {

        StringBuilder mergedText = new StringBuilder();
        for(int index = 0; index < texts[0].length(); index++) {
            mergedText.insert(0, texts[0].charAt(index));
            mergedText.insert(0, texts[1].charAt(index));
            mergedText.insert(0, texts[2].charAt(index));
        }

        return mergedText.toString();
    }

}
