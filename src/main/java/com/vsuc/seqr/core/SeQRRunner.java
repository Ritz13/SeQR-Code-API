package com.vsuc.seqr.core;

import com.beust.jcommander.JCommander;
import com.vsuc.seqr.utils.Result;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class SeQRRunner {

    public SeQRRunner() {
    }

    public String SeQRDecode(String[] args, BufferedImage image) throws Exception {
        DecoderConfig config = new DecoderConfig();
        JCommander jCommander = new JCommander(config);
        jCommander.parse(args);
        jCommander.setProgramName(SeQRRunner.class.getSimpleName());
        if (config.help) {
            jCommander.usage();
        }

//        List<URI> inputs = new ArrayList<>(config.inputPaths.size());
//        for (String inputPath : config.inputPaths) {
//            URI uri;
//            try {
//                uri = new URI(inputPath);
//            } catch (URISyntaxException use) {
//                // Assume it must be a file
//                if (!Files.exists(Paths.get(inputPath))) {
//                    throw use;
//                }
//                uri = new URI("file", inputPath, null);
//            }
//            inputs.add(uri);
//        }

//        do {
//            inputs = expand(inputs);
//        } while (false);
//
//        int numInputs = inputs.size();
//        if (numInputs == 0) {
//            jCommander.usage();
//            return;
//        }

//        Queue<URI> syncInputs = new ConcurrentLinkedQueue<>(inputs);
//        int numThreads = Math.min(numInputs, Runtime.getRuntime().availableProcessors());
        int successful = 0;
//        if (numThreads > 1) {
//            // do something
//        } else {
//            successful = new DecodeWorker(config, image).call();
//        }

        DecodeWorker worker = new DecodeWorker(config, image);
        successful = worker.call();

        if(successful > 0) {
            System.out.println("\nDecoded the image file\n");
            return worker.seqrResult;
        }
        return null;
    }

    private static List<URI> expand(Iterable<URI> inputs) throws IOException {
        List<URI> expanded = new ArrayList<>();
        for (URI input : inputs) {
            if (isFileOrDir(input)) {
                Path inputPath = Paths.get(input);
                if (Files.isDirectory(inputPath)) {
                    try (DirectoryStream<Path> childPaths = Files.newDirectoryStream(inputPath)) {
                        for (Path childPath : childPaths) {
                            expanded.add(childPath.toUri());
                        }
                    }
                } else {
                    expanded.add(input);
                }
            } else {
                expanded.add(input);
            }
        }
        for (int i = 0; i < expanded.size(); i++) {
            URI input = expanded.get(i);
            if (input.getScheme() == null) {
                expanded.set(i, Paths.get(input.getRawPath()).toUri());
            }
        }
        return expanded;
    }

    private static boolean isFileOrDir(URI uri) {
        return "file".equals(uri.getScheme());
    }

}
