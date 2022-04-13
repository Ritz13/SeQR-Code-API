package com.vsuc.seqr.core;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import com.vsuc.seqr.utils.CodeFormat;

import java.util.List;

final class EncoderConfig {

    static final String DEFAULT_OUTPUT_FILE_BASE = "out";

    @Parameter(names = "--code_format",
            description = "Format to encode, from CodeFormat class")
    CodeFormat codeFormat = CodeFormat.SEQR_CODE;

    @Parameter(names = "--image_format",
            description = "Image output format, such as PNG, JPG")
    String imageFormat = "PNG";

    @Parameter(names = "--output",
            description = "File to write to. Defaults to out.png")
    String outputFileBase = DEFAULT_OUTPUT_FILE_BASE;

    @Parameter(names = "--width",
            description = "Image width",
            validateWith = PositiveInteger.class)
    int width = 500;

    @Parameter(names = "--height",
            description = "Image height",
            validateWith = PositiveInteger.class)
    int height = 500;

    @Parameter(names = "--error_correction_level",
            description = "Error correction level for the encoding")
    String errorCorrectionLevel = null;

    @Parameter(names = "--help",
            description = "Prints this help message",
            help = true)
    boolean help;

    @Parameter(description = "(Text to encode)", required = true)
    List<String> contents;

}
