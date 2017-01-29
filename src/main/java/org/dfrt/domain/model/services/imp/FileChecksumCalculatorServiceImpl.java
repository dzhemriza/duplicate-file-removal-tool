/*
 * org.dfrm
 *
 * File Name: FileChecksumCalculatorServiceImpl.java
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
package org.dfrt.domain.model.services.imp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dfrt.domain.model.exceptions.DomainException;
import org.dfrt.domain.model.services.FileChecksumCalculatorService;

public class FileChecksumCalculatorServiceImpl implements FileChecksumCalculatorService {

    private static final Logger LOG = Logger.getLogger(FileChecksumCalculatorServiceImpl.class);

    @Override
    public Map<String, List<Path>> traverse(Path path) {
        Map<String, List<Path>> result = new HashMap<>();

        try {

            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    LOG.debug("Start processing file: " + file);
                    try (FileInputStream fileInputStream = new FileInputStream(file.toFile())) {
                        String sha512sum = Hex.encodeHexString(DigestUtils.sha1(fileInputStream));

                        if (result.containsKey(sha512sum)) {
                            // We found a duplicate
                            List<Path> files = result.get(sha512sum);
                            files.add(file);
                        } else {
                            ArrayList<Path> files = new ArrayList<>();
                            files.add(file);
                            result.put(sha512sum, files);
                        }
                    }

                    return super.visitFile(file, attrs);
                }
            });

        } catch (IOException ioException) {
            final String m = "Error while walking through the directory tree: " + ioException.getMessage();
            LOG.error(m, ioException);
            throw new DomainException(m, ioException);
        }

        return result;
    }

}
