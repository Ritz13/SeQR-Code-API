package com.vsuc.seqr.core;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import com.vsuc.seqr.utils.CodeFormat;
import com.vsuc.seqr.utils.DecodeHintType;

import java.util.*;

final class DecoderConfig {

    @Parameter(names = "--crop",
            description = " Only examine cropped region of input image(s)",
            arity = 4,
            validateWith = PositiveInteger.class)
    List<Integer> crop;

    @Parameter(names = "--brief",
            description = "Only output one line per file, omitting the contents")
    boolean brief;

    @Parameter(names = "--possible_formats",
            description = "Formats to decode, where format is any value in BarcodeFormat",
            variableArity = true)
    List<CodeFormat> possibleFormats;

    @Parameter(names = "--help",
            description = "Prints this help message",
            help = true)
    boolean help;

    @Parameter(description = "(URIs to decode)", required = true, variableArity = true)
    List<String> inputPaths;

    Map<DecodeHintType,?> buildHints() {
        List<CodeFormat> finalPossibleFormats = possibleFormats;
        if (finalPossibleFormats == null || finalPossibleFormats.isEmpty()) {
            finalPossibleFormats = new ArrayList<>(Arrays.asList(
                    CodeFormat.SEQR_CODE
            ));
        }

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, finalPossibleFormats);
        return Collections.unmodifiableMap(hints);
    }

}
