/*
 * org.dfrm
 *
 * File Name: App.java
 *
 * Copyright 2017 Dzhem Riza
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dfrt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dfrt.domain.model.services.DuplicateFileCleanupService;
import org.dfrt.domain.model.services.FileChecksumCalculatorService;
import org.dfrt.domain.model.services.imp.DuplicateFileCleanupServiceImpl;
import org.dfrt.domain.model.services.imp.FileChecksumCalculatorServiceImpl;

/**
 * Main program entry point
 */
public class App {

    private static final Logger LOG = Logger.getLogger(App.class);

    private static void printUsage() {
        LOG.info("Usage:");
        LOG.info("duplicate-file-removal-tool <source-path> <target-path>");
        LOG.info("  <source-path> - Source directory containing duplicate files.");
        LOG.info("  <target-path> - Target directory where the duplicates are moved.");
    }

    public static void main(String[] args) throws Exception {
        printUsage();
        LOG.info(Arrays.toString(args));

        if (args.length != 2) {
            LOG.info(" Insufficient program arguments.");
            return;
        }

        try {
            final Path source = Paths.get(args[0]);
            final Path target = Paths.get(args[1]);

            FileChecksumCalculatorService fileChecksumCalculatorService = new FileChecksumCalculatorServiceImpl();

            Map<String, List<Path>> allFiles = fileChecksumCalculatorService.traverse(source);

            // Print out all files
            LOG.debug("Result Dump:");
            allFiles.entrySet().stream().forEach((entry) -> {
                LOG.debug("Checksum: [" + entry.getKey() + "], Path: [" + entry.getValue() + "]");
            });

            DuplicateFileCleanupService duplicateFileCleanupService = new DuplicateFileCleanupServiceImpl();

            duplicateFileCleanupService.cleanup(source, allFiles, target);
        } catch (Exception exception) {
            LOG.error("Unknown error: " + exception.getMessage(), exception);
        }

        LOG.info("...................done.");
    }
}
