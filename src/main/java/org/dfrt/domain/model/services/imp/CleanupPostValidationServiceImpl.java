/*
 * org.dfrm
 *
 * File Name: CleanupPostValidationServiceImpl.java
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

import org.apache.log4j.Logger;
import org.dfrt.domain.model.exceptions.DomainException;
import org.dfrt.domain.model.services.CleanupPostValidationService;
import org.dfrt.domain.model.services.FileChecksumCalculatorService;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class CleanupPostValidationServiceImpl implements CleanupPostValidationService {

    private static final Logger LOG = Logger.getLogger(CleanupPostValidationServiceImpl.class);

    private FileChecksumCalculatorService fileChecksumCalculatorService;

    public CleanupPostValidationServiceImpl(FileChecksumCalculatorService fileChecksumCalculatorService) {
        this.fileChecksumCalculatorService = fileChecksumCalculatorService;
    }

    @Override
    public void validate(Path source, Path target) {
        Map<String, List<Path>> filesFromSource = fileChecksumCalculatorService.traverse(source);

        // fileFromSource value should contains only one element
        long count = filesFromSource.entrySet().stream().filter((entry) -> entry.getValue().size() > 1).count();
        if (0 < count) {
            filesFromSource.entrySet().stream().filter((entry) -> entry.getValue().size() > 1).forEach((entry) -> {
                LOG.warn("Checksum: [" + entry.getKey() + "], Path: [" + entry.getValue() + "]");
            });
            throw new DomainException("Source directory still has duplicates run this tool again.");
        }

        // filesFromTarget is a subset of fileFromSource
        Map<String, List<Path>> filesFromTarget = fileChecksumCalculatorService.traverse(target);

        count = filesFromTarget.keySet().stream().filter((key) -> !filesFromSource.containsKey(key)).count();
        if (0 < count) {
            throw new DomainException("Target directory contains files which doesn't exists in source directory.");
        }
    }
}
