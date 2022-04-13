package com.vsuc.seqr.utils;

import com.vsuc.seqr.utils.classes.BitMatrix;

import java.util.Map;

public interface Writer {


    BitMatrix encode(String contents,
                     CodeFormat format,
                     int width,
                     int height)
            throws WriterException;

    BitMatrix encode(String contents,
                     CodeFormat format,
                     int width,
                     int height,
                     Map<EncodeHintType,?> hints)
            throws WriterException;
}
