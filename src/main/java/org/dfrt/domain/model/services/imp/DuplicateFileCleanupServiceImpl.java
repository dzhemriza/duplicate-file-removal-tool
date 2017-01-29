/*
 * org.dfrm
 *
 * File Name: DuplicateFileCleanupServiceImpl.java
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dfrt.domain.model.exceptions.DomainException;
import org.dfrt.domain.model.services.DuplicateFileCleanupService;

public class DuplicateFileCleanupServiceImpl implements DuplicateFileCleanupService {

    private final static Logger LOG = Logger.getLogger(DuplicateFileCleanupServiceImpl.class);

    @Override
    public void cleanup(Path source, Map<String, List<Path>> files, Path backup) {
        files.entrySet().forEach((entry) -> {
            try {

                List<Path> duplicates = entry.getValue();
                if (duplicates.size() > 1) {
                    // We have more than 1 file with the same checksum

                    // Iterate through all the duplicate files skipping the
                    // first element
                    for (int i = 1; i < duplicates.size(); ++i) {
                        Path fileToMove = duplicates.get(i);

                        Path relativePath = source.relativize(fileToMove);
                        Path destFileLocation = Paths.get(backup.toString(), relativePath.toString());

                        Path parentDir = destFileLocation.getParent();
                        if (!parentDir.toFile().exists() && !parentDir.toFile().mkdirs()) {
                            String m = "Unable to create directory: " + parentDir;
                            LOG.error(m);
                            throw new DomainException(m);
                        }

                        LOG.debug("Moving file: " + fileToMove + " to: " + destFileLocation);
                        Files.copy(fileToMove, destFileLocation, StandardCopyOption.COPY_ATTRIBUTES,
                                StandardCopyOption.REPLACE_EXISTING);

                        LOG.debug("Deleting file: " + fileToMove);
                        Files.delete(fileToMove);
                    }
                }

            } catch (IOException ioException) {
                String m = "Unable to move file: " + ioException.getMessage();
                LOG.error(m, ioException);
                throw new DomainException(m, ioException);
            }
        });
    }
}
