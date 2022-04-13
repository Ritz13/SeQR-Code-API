package com.vsuc.seqr.core;

import com.vsuc.seqr.utils.*;
import com.vsuc.seqr.utils.classes.HybridBinarizer;
import com.vsuc.seqr.utils.exceptions.NotFoundException;
import com.vsuc.seqr.utils.result.ParsedResult;
import com.vsuc.seqr.utils.result.ResultParser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;

public class DecodeWorker implements Callable<Integer> {

    private static final int RED = 0xFFFF0000;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private final DecoderConfig config;
    private final BufferedImage bImage;
    private final Map<DecodeHintType,?> hints;

    public String seqrResult;

    DecodeWorker(DecoderConfig config, BufferedImage bImage) {
        this.config = config;
        this.bImage = bImage;
        hints = config.buildHints();
    }

    @Override
    public Integer call() throws IOException {
        int successful = 0;
        Result[][] results = decode(hints, bImage);
        if (results != null) {
            successful++;
        }
        return successful;
    }


    private Result[][] decode(Map<DecodeHintType,?> hints, BufferedImage image) {

        BufferedImage[] triImage = new TriSeparator(image).separateImage();
        Result[][] results = new Result[3][];

        for(int color = 0; color < 3; color++) {
            LuminanceSource source;
            if (config.crop == null) {
                source = new BufferedImageLuminanceSource(triImage[color]);
            } else {
                List<Integer> crop = config.crop;
                source = new BufferedImageLuminanceSource(
                        triImage[color], crop.get(0), crop.get(1), crop.get(2), crop.get(3));
            }

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            MultiFormatReader multiFormatReader = new MultiFormatReader();
            try {
                results[color] = new Result[]{multiFormatReader.decode(bitmap, hints)};
            } catch (NotFoundException ignored) {
                System.out.println("No barcode found");
                return null;
            }
        }

        StringWriter output = new StringWriter();
        String[] textParts = new String[3];
        String[] parsedParts = new String[3];
        for(int i=0; i<3; i++) {
            for (Result result : results[i]) {
                ParsedResult parsedResult = ResultParser.parseResult(result);
                textParts[i] = result.getText();
                parsedParts[i] = parsedResult.getDisplayResult();
                if(i==2) {
                    this.seqrResult = TextBuilder.merge(textParts);
                    ResultPoint[] resultPoints = result.getResultPoints();
                    int numResultPoints = resultPoints.length;
                    output.write(" (format: " + result.getCodeFormat() +
                            ", type: " + parsedResult.getType() + "):\n" +
                            "Raw result:\n" +
                            TextBuilder.merge(textParts) + "\n" +
                            "Parsed result:\n" +
                            TextBuilder.merge(parsedParts) + "\n" +
                            "Found " + numResultPoints + " result points.\n");

                    for (int pointIndex = 0; pointIndex < numResultPoints; pointIndex++) {
                        ResultPoint rp = resultPoints[pointIndex];
                        if (rp != null) {
                            output.write("  Point " + pointIndex + ": (" + rp.getX() + ',' + rp.getY() + ')');
                            if (pointIndex != numResultPoints - 1) {
                                output.write('\n');
                            }
                        }
                    }
                }
                output.write('\n');
            }
        }

        System.out.println(output);

        return results;
    }

}
